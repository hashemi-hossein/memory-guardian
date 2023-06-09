package ara.memoryguardian.work

import android.content.ClipboardManager
import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import ara.note.domain.usecase.userpreferences.ReadUserPreferencesUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun getContent() = withContext(Dispatchers.IO) {
        return@withContext clipboardManager.getContentText(context)
    }

    suspend fun clear() = withContext(Dispatchers.IO) {
        clipboardManager.clear()
        if (readUserPreferencesUseCase().isNotificationEnable)
            context.showNotification("Clipboard cleared", "")
    }

    suspend fun toggleAutoClearing(isAutoCleaningEnable: Boolean) = withContext(Dispatchers.IO) {
        val workManager = WorkManager.getInstance(context)
        if (isAutoCleaningEnable) {
            val userPreferences = readUserPreferencesUseCase()
            val workRequest: WorkRequest =
                PeriodicWorkRequestBuilder<ClipboardWorker>(userPreferences.autoCleaningInterval.toLong(), TimeUnit.MINUTES)
                    .setInputData(
                        workDataOf(
                            "isNotificationEnable" to userPreferences.isNotificationEnable
                        )
                    )
                    .build()

            if (userPreferences.isAutoCleaningEnable.not())
                workManager.enqueue(workRequest)
            else
                workManager.updateWork(workRequest)
        } else {
            workManager.cancelAllWork()
        }
    }
}
