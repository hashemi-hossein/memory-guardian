package ara.memoryguardian.ui.screen.home

import androidx.lifecycle.ViewModel
import ara.memoryguardian.HApplication
import ara.memoryguardian.clear
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    fun clearClipBoard() {
        HApplication.clipboardManager.clear()
    }
}