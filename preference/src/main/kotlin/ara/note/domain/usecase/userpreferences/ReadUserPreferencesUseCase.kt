package ara.note.domain.usecase.userpreferences

import ara.note.data.repository.UserPreferencesRepository
import javax.inject.Inject

class ReadUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    suspend operator fun invoke() =
        userPreferencesRepository.read()
}
