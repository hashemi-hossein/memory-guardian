package ara.note.data.repository

import androidx.datastore.core.DataStore
import ara.note.data.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KProperty1

/**
 * The repository which handles saving and retrieving user preferences
 */
@Singleton
class UserPreferencesRepository
@Inject constructor(private val userPreferencesStore: DataStore<UserPreferences>) {

    suspend fun read() = withContext(Dispatchers.IO) { userPreferencesStore.data.first() }

    suspend fun <T> write(kProperty: KProperty1<UserPreferences, T>, value: T) = withContext(Dispatchers.IO) {
        userPreferencesStore.updateData {
            Timber.d("$kProperty-- value= $value")
            when (kProperty) {
                UserPreferences::isNotificationEnable -> {
                    it.copy(isNotificationEnable = value as Boolean)
                }

                UserPreferences::isAutoCleaningEnable -> {
                    it.copy(isAutoCleaningEnable = value as Boolean)
                }

                UserPreferences::autoCleaningInterval -> {
                    it.copy(autoCleaningInterval = value as Int)
                }

                else -> {
                    error("wrong input")
                }
            }
        }
    }

    fun observe(): Flow<UserPreferences> = userPreferencesStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                Timber.e("Error reading preferences.")
                Timber.e(exception)
                emit(UserPreferences())
            } else {
                throw exception
            }
        }
}
