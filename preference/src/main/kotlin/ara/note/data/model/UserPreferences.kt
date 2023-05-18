package ara.note.data.model

//import androidx.annotation.Keep
import kotlinx.serialization.Serializable

//@Keep
@Serializable
data class UserPreferences(
    val isAutoCleaningEnable: Boolean = false,
    val autoCleaningInterval: Int = 15,
)
