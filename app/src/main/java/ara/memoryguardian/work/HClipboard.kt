package ara.memoryguardian.work

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import ara.memoryguardian.R
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

    fun getContent() = clipboardManager.getContentText(context)

    suspend fun clear() {
        clipboardManager.clear()

        val userPreferences = readUserPreferencesUseCase()
        if (userPreferences.isNotificationEnable)
            context.showNotification(context.getString(R.string.clipboard_cleared), "")

        if (userPreferences.isSmallPopupEnable)
            Toast.makeText(context, context.getString(R.string.clipboard_cleared), Toast.LENGTH_LONG).show()
    }

    suspend fun toggleAutoClearing(isAutoCleaningEnable: Boolean) {
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
