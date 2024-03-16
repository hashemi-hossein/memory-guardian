package ara.note.data.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UserPreferences(
    val isNotificationEnable: Boolean = false,
    val isSmallPopupEnable: Boolean = false,
    val isAutoCleaningEnable: Boolean = false,
    val autoCleaningIntervalSecond: Int = 60,
)
