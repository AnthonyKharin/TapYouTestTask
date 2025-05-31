package ge.anthony_kharin.tapyoutesttask.feature.main.presentation.model

sealed class MainScreenState {
    object Loading : MainScreenState()
    data class Loaded(val editTextError: String? = null) : MainScreenState()
    data class Error(val message: String) : MainScreenState()
}