package ara.memoryguardian.work

import android.content.ClipboardManager
import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import ara.memoryguardian.clear
import ara.memoryguardian.getClipboardManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HClipboard @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val clipboardManager: ClipboardManager by lazy {
        context.getClipboardManager()
    }

    fun clear(): Unit {
        clipboardManager.clear()
    }

    fun toggleAutoClearing(checked: Boolean): Unit {
        val workManager = WorkManager.getInstance(context)
        if (checked) {
            val workRequest: WorkRequest =
                PeriodicWorkRequestBuilder<ClipboardWorker>(15, TimeUnit.MINUTES)
                    .build()

            workManager.enqueue(workRequest)
        } else {
            workManager.cancelAllWork()
        }
    }
}
