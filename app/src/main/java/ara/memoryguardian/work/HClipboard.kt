package ara.memoryguardian.work

import android.content.ClipboardManager
import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import ara.memoryguardian.clear
import ara.memoryguardian.getClipboardManager
import ara.note.domain.usecase.userpreferences.ReadUserPreferencesUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HClipboard @Inject constructor(
    @ApplicationContext private val context: Context,
    private val readUserPreferencesUseCase: ReadUserPreferencesUseCase,
) {

    private val clipboardManager: ClipboardManager by lazy {
        context.getClipboardManager()
    }

    fun clear(): Unit {
        clipboardManager.clear()
    }

    suspend fun toggleAutoClearing(checked: Boolean): Unit {
        val workManager = WorkManager.getInstance(context)
        if (checked) {
            val interval = readUserPreferencesUseCase().autoCleaningInterval
            val workRequest: WorkRequest =
                PeriodicWorkRequestBuilder<ClipboardWorker>(interval.toLong(), TimeUnit.MINUTES)
                    .build()

            workManager.enqueue(workRequest)
        } else {
            workManager.cancelAllWork()
        }
    }
}
