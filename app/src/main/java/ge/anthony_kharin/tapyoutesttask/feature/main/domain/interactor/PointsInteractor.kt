package ge.anthony_kharin.tapyoutesttask.feature.main.domain.interactor

import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointsContainerEntity
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.repository.PointsRepository
import javax.inject.Inject

// Интерактор сделан только для демонстрации архитектуры
// В реальном проекте я бы усомнился делать такой, достаточно было бы только репозитория
class PointsInteractor @Inject constructor(
    private val repository: PointsRepository
) {
    suspend fun execute(count: Int): PointsContainerEntity = repository.getPoints(count)
}
