package ara.memoryguardian.work

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import ara.memoryguardian.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID
import ara.memoryguardian.FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_NAME
import ara.memoryguardian.FOREGROUND_SERVICE_NOTIFICATION_ID
import ara.memoryguardian.MainActivity
import ara.memoryguardian.R
import ara.note.domain.usecase.userpreferences.ReadUserPreferencesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
@AndroidEntryPoint
class MemoryForegroundService : Service() {

    companion object {
        fun start(context: Context) {
            Timber.d("MemoryForegroundService class # start fun")
            val serviceIntent = Intent(context, MemoryForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, MemoryForegroundService::class.java))
        }

        const val PAUSE_INTENT_EXTRA = "pause"
    }

    @Inject
    lateinit var readUserPreferencesUseCase: ReadUserPreferencesUseCase

    override fun onBind(intent: Intent): IBinder? = null

    private var clipboardManager: ClipboardManager? = null
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnableCode: Runnable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("MemoryForegroundService class # onStartCommand fun")
        super.onStartCommand(intent, flags, startId)

        val pause = intent?.getBooleanExtra(PAUSE_INTENT_EXTRA, false) ?: false
        Timber.d("MemoryForegroundService class # onStartCommand fun # pause=$pause")
        if (pause) {
            Toast.makeText(this, "Pause MemoryGuardian for 5 min", Toast.LENGTH_LONG).show()
            handler.removeCallbacks(runnableCode)
            handler.postDelayed(runnableCode, 5 * 60 * 1000)
            return START_STICKY
        }

        val notification: Notification = createNotification()
        ServiceCompat.startForeground(
            this,
            FOREGROUND_SERVICE_NOTIFICATION_ID,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                0
            }
        )

        runnableCode = Runnable {
            Timber.d("MemoryForegroundService class # onStartCommand fun # runnableCode.run")

            GlobalScope.launch {
                val userPreferences = readUserPreferencesUseCase()
                clipboardManager?.clearAndNotifyIfEnabled(context = this@MemoryForegroundService, userPreferences = userPreferences)
                    ?: run {
                        clipboardManager = getClipboardManager()
                        clipboardManager?.clearAndNotifyIfEnabled(context = this@MemoryForegroundService, userPreferences = userPreferences)
                    }

                handler.postDelayed(runnableCode, userPreferences.autoCleaningIntervalSecond.toLong() * 1000)
            }
        }
        handler.post(runnableCode)

        return START_STICKY
    }

    override fun onDestroy() {
        Timber.d("MemoryForegroundService class # onDestroy fun")
        super.onDestroy()

        clipboardManager = null
        handler.removeCallbacks(runnableCode)
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (notificationManager.getNotificationChannel(FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID, FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
        }

        val buttonIntent = Intent(this, MemoryForegroundService::class.java).putExtra(PAUSE_INTENT_EXTRA, true)
        val buttonPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(this, 5423, buttonIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getService(this, 5423, buttonIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        return NotificationCompat.Builder(this, FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.memory_guardian_is_running_in_the_background))
            .setSmallIcon(R.drawable.ic_clipboard)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 4814, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            })
            .setOngoing(true)
            .setSilent(true)
            .addAction(R.drawable.ic_pause, getString(R.string.pause_for_5_min), buttonPendingIntent)
            .build()
    }
}
