package ara.memoryguardian.ui.screen

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ara.memoryguardian.work.HClipboard
import ara.note.data.model.UserPreferences
import ara.note.domain.usecase.userpreferences.ObserveUserPreferencesUseCase
import ara.note.domain.usecase.userpreferences.WriteUserPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val hClipboard: HClipboard,
    private val observeUserPreferencesUseCase: ObserveUserPreferencesUseCase,
    private val writeUserPreferencesUseCase: WriteUserPreferencesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUIState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeUserPreferencesUseCase().collect { preference ->
                _uiState.update {
                    it.copy(
                        isNotificationEnable = preference.isNotificationEnable,
                        isSmallPopupEnable = preference.isSmallPopupEnable,
                        isAutoCleaningEnable = preference.isAutoCleaningEnable,
                        autoCleaningIntervalSecond = preference.autoCleaningIntervalSecond.toString(),
                    )
                }
            }
        }
    }

    fun setContent(value: String) {
        _uiState.update { it.copy(clipboardContent = value) }
    }

    fun setClipboardContent() {
        hClipboard.setContent(uiState.value.clipboardContent)
    }

    fun getCurrentClipboardContent() = viewModelScope.launch {
        val clipboardContent = hClipboard.getContent()
        _uiState.update { it.copy(clipboardContent = clipboardContent) }
    }

    fun clearClipboard() = viewModelScope.launch {
        hClipboard.clear()
        getCurrentClipboardContent()
    }

    fun toggleAutoClearing(isAutoCleaningEnable: Boolean) {
        viewModelScope.launch {
//            hClipboard.toggleAutoClearing(isAutoCleaningEnable)
            writeUserPreferencesUseCase(UserPreferences::isAutoCleaningEnable, isAutoCleaningEnable)
        }
    }

    fun changeInterval(value: String) {
        _uiState.update { it.copy(autoCleaningIntervalSecond = value) }
        if (isIntervalError(autoCleaningInterval = value).not()) {
            viewModelScope.launch {
                writeUserPreferencesUseCase(UserPreferences::autoCleaningIntervalSecond, value.toInt())
            }
        }
    }

    fun isIntervalError(autoCleaningInterval: String = uiState.value.autoCleaningIntervalSecond) =
        autoCleaningInterval.isBlank() || autoCleaningInterval.isDigitsOnly().not() || (autoCleaningInterval.toInt() < 1)

    fun emptySnackBarMessage() {
        _uiState.update { it.copy(snackBarMessage = null) }
    }

    fun showSnackBar(message: String) {
        _uiState.update { it.copy(snackBarMessage = message) }
    }

    fun toggleNotification(checked: Boolean): Unit {
        viewModelScope.launch {
//            if (uiState.value.isAutoCleaningEnable)
//                hClipboard.toggleAutoClearing(true)
            writeUserPreferencesUseCase(UserPreferences::isNotificationEnable, checked)
        }
    }

    fun toggleSmallPopup(checked: Boolean) {
        viewModelScope.launch {
//            if (uiState.value.isAutoCleaningEnable)
//                hClipboard.toggleAutoClearing(true)
            writeUserPreferencesUseCase(UserPreferences::isSmallPopupEnable, checked)
        }
    }
}
