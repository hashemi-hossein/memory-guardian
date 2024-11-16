package ara.memoryguardian

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ara.memoryguardian.work.HClipboard
import ara.memoryguardian.work.MemoryForegroundService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskerReceiver : BroadcastReceiver() {

    @Inject
    lateinit var hClipboard: HClipboard

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            "ara.memoryguardian.CLEAR_CLIPBOARD" -> {
                hClipboard.clear()
            }
            "ara.memoryguardian.SHOW_CLIPBOARD" -> {
                val clipboardContent = hClipboard.getContent()
                // Show the clipboard content (e.g., using a Toast or Notification)
            }
            "ara.memoryguardian.PAUSE_SERVICE" -> {
                MemoryForegroundService.stop(context!!)
            }
        }
    }
}
