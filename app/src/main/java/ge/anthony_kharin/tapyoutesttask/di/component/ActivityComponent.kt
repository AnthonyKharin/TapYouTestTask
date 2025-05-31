package ge.anthony_kharin.tapyoutesttask.di.component

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Subcomponent
import ge.anthony_kharin.tapyoutesttask.MainActivity
import ge.anthony_kharin.tapyoutesttask.di.module.ViewModelModule
import ge.anthony_kharin.tapyoutesttask.di.scope.ActivityScope
import ge.anthony_kharin.tapyoutesttask.feature.main.presentation.view.MainFragment
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view.PointDetailsFragment

@ActivityScope
@Subcomponent(
    modules = [
        ViewModelModule::class
    ]
)
interface ActivityComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity): ActivityComponent
    }

    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(mainFragment: PointDetailsFragment)
}