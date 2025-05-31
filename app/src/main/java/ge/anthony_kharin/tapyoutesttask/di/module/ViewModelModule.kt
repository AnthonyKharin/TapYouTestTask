package ge.anthony_kharin.tapyoutesttask.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ge.anthony_kharin.tapyoutesttask.feature.main.presentation.viewModel.MainViewModelFactory
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.viewModel.PointDetailsViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindFirstFactory(factory: MainViewModelFactory): ViewModelProvider.Factory

    @Binds
    abstract fun bindSecondFactory(factory: PointDetailsViewModelFactory): ViewModelProvider.Factory
}
