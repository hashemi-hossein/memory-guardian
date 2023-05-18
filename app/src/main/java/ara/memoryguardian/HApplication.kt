package ara.memoryguardian

import android.app.Application
import android.content.ClipboardManager
import android.content.Context

class HApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        clipboardManager = applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    companion object{
        lateinit var clipboardManager:ClipboardManager
    }
}
