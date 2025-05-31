package ge.anthony_kharin.tapyoutesttask.feature.main.presentation.viewModel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import ge.anthony_kharin.tapyoutesttask.R
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.interactor.PointsInteractor
import ge.anthony_kharin.tapyoutesttask.feature.main.presentation.model.MainScreenState
import ge.anthony_kharin.tapyoutesttask.navigation.Screens
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val MIN_VALUE = 1
private const val MAX_VALUE = 1_000

class MainViewModel(
    private val interactor: PointsInteractor,
    private val resources: Resources,
    private val router: Router
) : ViewModel() {

    private val _screenState: MutableStateFlow<MainScreenState> =
        MutableStateFlow(MainScreenState.Loaded())
    val screenState = _screenState.asStateFlow()

    private var currentCount: Int? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _screenState.value = MainScreenState.Error(throwable.message.orEmpty())
    }

    fun onTextChanged(inputText: String) {
        val inputNumber = inputText.toIntOrNull()
        currentCount = inputNumber

        val errorMessage = when {
            inputText.isEmpty() -> null
            inputNumber == null -> resources.getString(R.string.not_a_number_error)
            inputNumber !in MIN_VALUE..MAX_VALUE -> resources.getString(
                R.string.invalid_range_error,
                MIN_VALUE.toString(),
                MAX_VALUE.toString()
            )
            else -> null
        }
        _screenState.value = MainScreenState.Loaded(editTextError = errorMessage)
    }

    fun onGoButtonClicked() {
        val count = currentCount ?: return

        viewModelScope.launch(coroutineExceptionHandler) {
            _screenState.value = MainScreenState.Loading
            val points = interactor.execute(count)
            _screenState.value = MainScreenState.Loaded()
            router.navigateTo(Screens.pointDetails(points))
        }
    }
}