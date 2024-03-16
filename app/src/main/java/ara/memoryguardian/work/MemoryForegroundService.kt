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

        return NotificationCompat.Builder(this, FOREGROUND_SERVICE_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.memory_guardian_is_running_in_the_background))
            .setSmallIcon(R.drawable.ic_clipboard)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
            })
            .setOngoing(true)
            .setSilent(true)
            .build()
    }
}
