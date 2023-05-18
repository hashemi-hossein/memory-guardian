package ara.memoryguardian

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build

fun Context.getClipboardManager()=
    this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

fun ClipboardManager.clear() {
    val alternativeCleanMethod = {
        setPrimaryClip(ClipData.newPlainText("", ""))
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        try {
            clearPrimaryClip()
        } catch (e: Exception) {
            alternativeCleanMethod()
        }
    } else {
        alternativeCleanMethod()
    }
}
