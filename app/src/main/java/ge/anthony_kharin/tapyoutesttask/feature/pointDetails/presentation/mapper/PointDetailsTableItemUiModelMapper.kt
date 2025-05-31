package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.mapper

import android.content.res.Resources
import ge.anthony_kharin.tapyoutesttask.R
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointEntity
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.model.PointDetailsTableItemUiModel
import ge.anthony_kharin.tapyoutesttask.feature.utils.toList
import javax.inject.Inject

class PointDetailsTableItemUiModelMapper @Inject constructor(
    resources: Resources
) {
    // Можно добавить отдельную сущность чтобы выделить что это заголовок,
    // но выглядит перебором для тестового задания и в ТЗ не было указано
    private val pointsTitle = PointDetailsTableItemUiModel(
        index = resources.getString(R.string.index_title),
        x = resources.getString(R.string.x_coordinate),
        y = resources.getString(R.string.y_coordinate)
    )

    // Тут хочется добавить sortedBy { it.x }
    // но в ТЗ сказано что порядок должен быть на графике, про таблицу не было речь
    fun map(points: List<PointEntity>): List<PointDetailsTableItemUiModel> =
        pointsTitle.toList() + points.mapIndexed(::mapPoint)

    private fun mapPoint(index: Int, point: PointEntity): PointDetailsTableItemUiModel =
        PointDetailsTableItemUiModel(
            index = index.toString(),
            x = point.x.toString(),
            y = point.y.toString()
        )
}