package ge.anthony_kharin.tapyoutesttask.feature.main.domain.repository

import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointsContainerEntity

fun interface PointsRepository {
    suspend fun getPoints(count: Int): PointsContainerEntity
}
