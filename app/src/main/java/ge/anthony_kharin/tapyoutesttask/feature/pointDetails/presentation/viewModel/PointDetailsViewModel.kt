package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointsContainerEntity
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.mapper.PointDetailsChartItemUiModelMapper
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.mapper.PointDetailsTableItemUiModelMapper
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.model.PointDetailsScreenState
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view.PointDetailsFragment
import ge.anthony_kharin.tapyoutesttask.feature.saveChart.presentation.ChartSaver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PointDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    chartMapper: PointDetailsChartItemUiModelMapper,
    tableMapper: PointDetailsTableItemUiModelMapper,
    private val chartSaver: ChartSaver
) : ViewModel() {

    private val pointsContainerEntity = savedStateHandle.get<PointsContainerEntity>(
        key = PointDetailsFragment.POINT_DETAILS_ARGUMENT_TAG
    )

    private val _screenState = MutableStateFlow(
        PointDetailsScreenState(
            pointsTable = tableMapper.map(pointsContainerEntity?.points.orEmpty()),
            pointsChart = chartMapper.map(pointsContainerEntity?.points.orEmpty())
        )
    )
    val screenState: StateFlow<PointDetailsScreenState> = _screenState.asStateFlow()

    fun onUseBezierSwitchChecked(checked: Boolean) {
        _screenState.value = _screenState.value.copy(useBezier = checked)
    }

    fun saveChart(bitmap: Bitmap) {
        chartSaver.saveChart(bitmap)
    }
}
