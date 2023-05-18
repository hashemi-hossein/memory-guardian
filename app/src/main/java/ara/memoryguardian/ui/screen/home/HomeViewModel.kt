package ara.memoryguardian.ui.screen.home

import android.content.ClipData
import android.os.Build
import androidx.lifecycle.ViewModel
import ara.memoryguardian.HApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    fun clearClipBoard() {
        val alternativeCleanMethod = {
            HApplication.clipboardManager.setPrimaryClip(ClipData.newPlainText("", ""))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                HApplication.clipboardManager.clearPrimaryClip()
            } catch (e: Exception) {
                alternativeCleanMethod()
            }
        } else {
            alternativeCleanMethod()
        }
    }
}