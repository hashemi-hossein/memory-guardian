package ara.memoryguardian.work

//import android.content.Context
//import android.widget.Toast
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//import ara.memoryguardian.R
//import timber.log.Timber
//
//class ClipboardWorker(
//    private val context: Context,
//    private val workerParams: WorkerParameters,
//) : Worker(context, workerParams) {
//
//    override fun doWork(): Result {
//        Timber.d("ClipboardWorker class # doWork fun")
//        Timber.d("workerParams.id = ${workerParams.id}")
//        Timber.d("workerParams.inputData.keyValueMap = ${workerParams.inputData.keyValueMap}")
//
//        val clipboardManager = context.getClipboardManager()
//        clipboardManager.clear()
//
//        val isNotificationEnable = inputData.getBoolean("isNotificationEnable", false)
//        if (isNotificationEnable)
//            context.showNotification(context.getString(R.string.clipboard_cleared), "")
//
//        val isSmallPopupEnable = inputData.getBoolean("isSmallPopupEnable", false)
//        if (isSmallPopupEnable)
//            Toast.makeText(context, context.getString(R.string.clipboard_cleared), Toast.LENGTH_LONG).show()
//
//        return Result.success()
//    }
//}
