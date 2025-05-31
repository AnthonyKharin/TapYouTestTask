package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.model

import com.github.mikephil.charting.data.Entry

data class PointDetailsScreenState(
    val useBezier: Boolean = false,
    val pointsTable: List<PointDetailsTableItemUiModel> = emptyList(),
    val pointsChart: List<Entry> = emptyList()
)