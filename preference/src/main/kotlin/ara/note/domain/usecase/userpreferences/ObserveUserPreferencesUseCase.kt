package ara.note.domain.usecase.userpreferences

import ara.note.data.repository.UserPreferencesRepository
import javax.inject.Inject

class ObserveUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) {
    operator fun invoke() =
        userPreferencesRepository.observe()
}
