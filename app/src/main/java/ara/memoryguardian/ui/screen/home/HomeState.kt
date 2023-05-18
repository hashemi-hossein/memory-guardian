package ara.memoryguardian.ui.screen.home

data class HomeState(
    val isAutoCleaningEnable: Boolean = false,
    val autoCleaningInterval: String = "15",
)
