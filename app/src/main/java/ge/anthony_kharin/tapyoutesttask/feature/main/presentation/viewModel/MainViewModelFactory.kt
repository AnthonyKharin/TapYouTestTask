package ge.anthony_kharin.tapyoutesttask.feature.main.presentation.viewModel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.Router
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.interactor.PointsInteractor
import javax.inject.Inject
import kotlin.jvm.java

class MainViewModelFactory @Inject constructor(
    private val interactor: PointsInteractor,
    private val resources: Resources,
    private val router: Router
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(interactor, resources, router) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}