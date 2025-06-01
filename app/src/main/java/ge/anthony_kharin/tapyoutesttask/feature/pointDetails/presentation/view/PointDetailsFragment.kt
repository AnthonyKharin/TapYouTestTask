package ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import ge.anthony_kharin.tapyoutesttask.R
import ge.anthony_kharin.tapyoutesttask.base.BaseFragment
import ge.anthony_kharin.tapyoutesttask.databinding.FragmentPointDetailsBinding
import ge.anthony_kharin.tapyoutesttask.di.component.ActivityComponent
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointsContainerEntity
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view.recyclerView.HorizontalDividerWithStartEndDecoration
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view.recyclerView.PointDetailsRecyclerViewAdapter
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.viewModel.PointDetailsViewModel
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.viewModel.PointDetailsViewModelFactory
import ge.anthony_kharin.tapyoutesttask.feature.utils.setThrottleClickListener
import ge.anthony_kharin.tapyoutesttask.feature.utils.toList
import javax.inject.Inject

class PointDetailsFragment : BaseFragment<FragmentPointDetailsBinding>() {

    @Inject
    lateinit var vmFactory: PointDetailsViewModelFactory
    private val viewModel: PointDetailsViewModel by viewModels { vmFactory }

    private val adapter by lazy { PointDetailsRecyclerViewAdapter() }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPointDetailsBinding
        get() = FragmentPointDetailsBinding::inflate

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                saveChartInternal()
            } else {
                // Тут можно показать диалог, почему нам действительно необходимо получить разрешение
                // Рассказать что можно дать разрешение в настройках и пр, но для тестового проекта
                // кажется, что это просто трата времени
                showToast(resources.getString(R.string.cant_save_image_error))
            }
        }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun subscribeStates() {
        viewModel.screenState.subscribeInViewLifecycle { state ->
            adapter.items = state.pointsTable
            updateChartData(state.pointsChart, state.useBezier)
        }
    }

    private fun updateChartData(entries: List<Entry>, useBezier: Boolean) {
        binding.lineChart.apply {
            data = LineData(LineDataSet(entries, CHART_LABEL).setupChartDataSet(useBezier).toList())
            invalidate()
        }
    }

    override fun setupViews() {
        binding.pointsRecyclerView.apply {
            adapter = this@PointDetailsFragment.adapter
            addItemDecoration(HorizontalDividerWithStartEndDecoration(requireContext()))
        }
        binding.useBezierSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onUseBezierSwitchChecked(isChecked)
        }
        setupLineChart()
        binding.saveChartImageButton.setThrottleClickListener {
            checkPermissionAndSave()
        }
    }

    private fun setupLineChart() {
        binding.lineChart.apply {
            description.isEnabled = false

            setPinchZoom(true)

            xAxis.position = XAxisPosition.BOTTOM
            xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.axisTextColor)

            axisRight.isEnabled = false
            axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.axisTextColor)

            legend.isEnabled = false
        }
    }

    private fun checkPermissionAndSave() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> saveChartInternal()
            checkPermission() -> saveChartInternal()
            else -> requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    // Метод написан для обычной чистоты кода, по-хорошему это должно быть метод с параметром
    // И не в дочернем классе
    // То есть этот метод для одно конктретного случая, но в ревльном проекте можно было бы расширить
    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun saveChartInternal() {
        viewModel.saveChart(binding.lineChart.chartBitmap)
    }

    private fun LineDataSet.setupChartDataSet(
        useBezier: Boolean
    ): LineDataSet = this.apply {
        setDrawCircles(true)
        circleRadius = 4f
        setCircleColor(ColorTemplate.getHoloBlue())

        lineWidth = 2f

        // Сглаживание графика
        if (useBezier) {
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        setDrawValues(false)
    }

    companion object {
        const val POINT_DETAILS_ARGUMENT_TAG = "POINT_DETAILS_ARGUMENT_TAG"
        private const val CHART_LABEL = "PointListChart"

        fun newInstance(pointsContainerEntity: PointsContainerEntity): PointDetailsFragment =
            PointDetailsFragment().apply {
                arguments = bundleOf(POINT_DETAILS_ARGUMENT_TAG to pointsContainerEntity)
            }
    }
}