package ara.memoryguardian.ui.screen.home

import ara.memoryguardian.DEFAULT_AUTO_CLEANING_INTERVAL

data class AppUIState(
    val snackBarMessage: String? = null,

    val isNotificationEnable: Boolean = false,
    val isSmallPopupEnable: Boolean = false,
    val isAutoCleaningEnable: Boolean? = null,
    val autoCleaningIntervalSecond: String = DEFAULT_AUTO_CLEANING_INTERVAL,

    val clipboardContent: String = "",
)
