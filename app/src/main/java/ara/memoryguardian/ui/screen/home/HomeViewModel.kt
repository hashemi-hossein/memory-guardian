package ara.memoryguardian.ui.screen.home

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
class HomeViewModel @Inject constructor(
    private val hClipboard: HClipboard,
    private val observeUserPreferencesUseCase: ObserveUserPreferencesUseCase,
    private val writeUserPreferencesUseCase: WriteUserPreferencesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeUserPreferencesUseCase().collect { preference ->
                _uiState.update {
                    it.copy(
                        isNotificationEnable = preference.isNotificationEnable,
                        isSmallPopupEnable = preference.isSmallPopupEnable,
                        isAutoCleaningEnable = preference.isAutoCleaningEnable,
                        autoCleaningInterval = preference.autoCleaningInterval.toString(),
                    )
                }
            }
        }
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
            hClipboard.toggleAutoClearing(isAutoCleaningEnable)
            writeUserPreferencesUseCase(UserPreferences::isAutoCleaningEnable, isAutoCleaningEnable)
        }
    }

    fun changeInterval(value: String) {
        _uiState.update { it.copy(autoCleaningInterval = value) }
        if (isIntervalError(autoCleaningInterval = value).not()) {
            viewModelScope.launch {
                writeUserPreferencesUseCase(UserPreferences::autoCleaningInterval, value.toInt())
            }
        }
    }

    fun isIntervalError(autoCleaningInterval: String = uiState.value.autoCleaningInterval) =
        autoCleaningInterval.isBlank() || autoCleaningInterval.isDigitsOnly().not() || (autoCleaningInterval.toInt() < 15)

    fun emptySnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun toggleNotification(checked: Boolean): Unit {
        viewModelScope.launch {
            if (uiState.value.isAutoCleaningEnable)
                hClipboard.toggleAutoClearing(true)
            writeUserPreferencesUseCase(UserPreferences::isNotificationEnable, checked)
        }
    }

    fun toggleSmallPopup(checked: Boolean) {
        viewModelScope.launch {
            if (uiState.value.isAutoCleaningEnable)
                hClipboard.toggleAutoClearing(true)
            writeUserPreferencesUseCase(UserPreferences::isSmallPopupEnable, checked)
        }
    }
}
