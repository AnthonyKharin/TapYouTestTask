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
import kotlin.math.absoluteValue

private const val MIN_VALUE = 1
private const val MAX_VALUE = 1_000
private const val ZERO_STRING = "0"

class MainViewModel(
    private val interactor: PointsInteractor,
    private val resources: Resources,
    private val router: Router
) : ViewModel() {

    private val _screenState: MutableStateFlow<MainScreenState> =
        MutableStateFlow(MainScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    private var currentCount: Int? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _screenState.value = MainScreenState.Error(throwable.message.orEmpty())
    }

    fun onEnterCountEditTextChanged(inputText: String) {
        val inputNumber = inputText.toIntOrNull()
        currentCount = inputNumber

        if (inputText.isEmpty()) {
            _screenState.value = MainScreenState.Initial
            return
        }

        val errorMessage = when {
            inputText.startsWith(ZERO_STRING) -> resources.getString(R.string.cant_start_with_zero_error)
            inputText.length > MAX_VALUE.countDigits() -> getRangeMessage()
            inputNumber !in MIN_VALUE..MAX_VALUE -> getRangeMessage()
            inputNumber == null -> resources.getString(R.string.not_a_number_error)
            else -> null
        }

        _screenState.value = MainScreenState.Loaded(
            editTextError = errorMessage,
            currentInput = inputText
        )
    }

    fun onContinueButtonClicked() {
        val count = currentCount ?: run {
            _screenState.value = MainScreenState.Error(
                resources.getString(R.string.something_went_wrong_with_count_field_error)
            )
            return@onContinueButtonClicked
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            _screenState.value = MainScreenState.Loading
            val points = interactor.execute(count)
            _screenState.value = MainScreenState.Loaded()
            router.navigateTo(Screens.pointDetails(points))
        }
    }

    private fun getRangeMessage(): String = resources.getString(
        R.string.invalid_range_error,
        MIN_VALUE.toString(),
        MAX_VALUE.toString()
    )

    private fun Int.countDigits(): Int = absoluteValue.toString().length
}
