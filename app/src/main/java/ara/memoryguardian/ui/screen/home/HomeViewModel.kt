package ara.memoryguardian.ui.screen.home

import androidx.lifecycle.ViewModel
import ara.memoryguardian.work.HClipboard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val hClipboard: HClipboard,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    fun clearClipBoard() = hClipboard.clear()

    fun toggleAutoClearing(checked: Boolean) = hClipboard.toggleAutoClearing(checked)
}