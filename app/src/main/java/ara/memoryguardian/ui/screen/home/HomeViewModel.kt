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
                        isAutoCleaningEnable = preference.isAutoCleaningEnable,
                        autoCleaningInterval = preference.autoCleaningInterval.toString(),
                    )
                }
            }
        }
    }

    fun clearClipBoard() = hClipboard.clear()

    fun toggleAutoClearing(checked: Boolean) {
        viewModelScope.launch {
            writeUserPreferencesUseCase(UserPreferences::isAutoCleaningEnable, checked)
            hClipboard.toggleAutoClearing(checked)
        }
    }

    fun changeInterval(value: String) {
        if (value.isDigitsOnly()) {
            viewModelScope.launch {
                writeUserPreferencesUseCase(UserPreferences::autoCleaningInterval, value.toInt())
            }
        }
    }
}
