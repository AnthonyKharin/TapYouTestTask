package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.mapper.PointDetailsChartItemUiModelMapper
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.mapper.PointDetailsTableItemUiModelMapper
import ge.anthony_kharin.tapyoutesttask.feature.saveChart.presentation.ChartSaver
import javax.inject.Inject

class PointDetailsViewModelFactory @Inject constructor(
    private val chartMapper: PointDetailsChartItemUiModelMapper,
    private val tableMapper: PointDetailsTableItemUiModelMapper,
    private val chartSaver: ChartSaver
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(PointDetailsViewModel::class.java)) {
            val data = extras.createSavedStateHandle()
            return PointDetailsViewModel(
                savedStateHandle = data,
                chartMapper = chartMapper,
                tableMapper = tableMapper,
                chartSaver = chartSaver
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}