package ara.memoryguardian.ui.screen.home

data class HomeState(
    val snackbarMessage: String? = null,
    val isNotificationEnable: Boolean = false,
    val isAutoCleaningEnable: Boolean = false,
    val autoCleaningInterval: String = "15",
)
