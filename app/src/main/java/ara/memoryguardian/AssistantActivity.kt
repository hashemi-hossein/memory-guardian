package ara.memoryguardian

import android.os.Bundle
import androidx.activity.ComponentActivity
import ara.memoryguardian.work.clear
import ara.memoryguardian.work.getClipboardManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AssistantActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.getClipboardManager().clear()
        finish()
    }
}
