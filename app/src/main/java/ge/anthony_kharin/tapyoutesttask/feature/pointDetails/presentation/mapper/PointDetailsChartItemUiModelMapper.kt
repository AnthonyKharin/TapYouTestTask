package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.mapper

import com.github.mikephil.charting.data.Entry
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointEntity
import javax.inject.Inject

class PointDetailsChartItemUiModelMapper @Inject constructor() {
    fun map(points: List<PointEntity>): List<Entry> =
        points.sortedBy { it.x }.map(::mapPoint)

    private fun mapPoint(point: PointEntity): Entry =
        Entry(
            point.x.toFloat(),
            point.y.toFloat()
        )
}