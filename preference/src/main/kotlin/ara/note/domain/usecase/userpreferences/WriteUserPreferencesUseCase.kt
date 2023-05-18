package ara.note.domain.usecase.userpreferences

import ara.note.data.model.UserPreferences
import ara.note.data.repository.UserPreferencesRepository
import javax.inject.Inject
import kotlin.reflect.KProperty1

class WriteUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun <T> invoke(kProperty: KProperty1<UserPreferences, T>, value: T) =
        userPreferencesRepository.write(kProperty, value)
}
