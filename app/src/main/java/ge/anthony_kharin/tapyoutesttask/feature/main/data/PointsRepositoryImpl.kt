package ge.anthony_kharin.tapyoutesttask.feature.main.data

import ge.anthony_kharin.tapyoutesttask.feature.main.data.api.ApiService
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointsContainerEntity
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.repository.PointsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PointsRepositoryImpl @Inject constructor(
    private var api: ApiService
) : PointsRepository {
    override suspend fun getPoints(count: Int): PointsContainerEntity =
        withContext(Dispatchers.IO) {
            api.getPoints(count)
        }
}
