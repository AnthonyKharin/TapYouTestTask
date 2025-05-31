package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointsContainerEntity
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.mapper.PointDetailsChartItemUiModelMapper
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.mapper.PointDetailsTableItemUiModelMapper
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.model.PointDetailsScreenState
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view.PointDetailsFragment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
        // Тут можно добавить сообщение об успешном скачивании и пр,
        // но нужно делать хитрее, чем просто показать тост
        // Связано это с тем, что сохранение может происходить дольше чем мы находимся на экране
        // По этой же причине, внутри chartSaver скоуп приложения
        viewModelScope.launch { chartSaver.saveChart(bitmap) }
    }
}
