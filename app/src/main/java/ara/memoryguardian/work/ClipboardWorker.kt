package ara.memoryguardian.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import ara.memoryguardian.clear
import ara.memoryguardian.getClipboardManager
import timber.log.Timber

class ClipboardWorker(
    private val appContext: Context,
    private val workerParams: WorkerParameters,
) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        Timber.d("ClipboardWorker class # doWork fun")
        Timber.d("workerParams.id = ${workerParams.id}")
        Timber.d("workerParams.inputData.keyValueMap = ${workerParams.inputData.keyValueMap}")

        val clipboardManager = appContext.getClipboardManager()
        clipboardManager.clear()

        return Result.success()
    }
}
