package ge.anthony_kharin.tapyoutesttask.feature.main.presentation.model

sealed interface MainScreenState {
    object Initial : MainScreenState
    object Loading : MainScreenState
    data class Loaded(
        val editTextError: String? = null,
        val currentInput: String = ""
    ) : MainScreenState

    data class Error(val message: String) : MainScreenState
}