package ara.note.data.datastore

import androidx.datastore.core.Serializer
import ara.note.data.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesSerializer @Inject constructor(
) : Serializer<UserPreferences> {

    override val defaultValue: UserPreferences
        get() = UserPreferences()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        return withContext(Dispatchers.IO + SupervisorJob()) {
            try {
                Json.decodeFromString(
                    deserializer = UserPreferences.serializer(),
                    string = input.readBytes().decodeToString(),
                )
            } catch (e: SerializationException) {
//                e.printStackTrace()
                defaultValue
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        withContext(Dispatchers.IO + SupervisorJob()) {
            output.write(
                Json.encodeToString(
                    serializer = UserPreferences.serializer(),
                    value = t,
                ).encodeToByteArray(),
            )
        }
    }
}
