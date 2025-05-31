package ge.anthony_kharin.tapyoutesttask.di.component

import dagger.BindsInstance
import dagger.Component
import ge.anthony_kharin.tapyoutesttask.TapYouTestTaskApplication
import ge.anthony_kharin.tapyoutesttask.di.module.AndroidModule
import ge.anthony_kharin.tapyoutesttask.di.module.NavigationModule
import ge.anthony_kharin.tapyoutesttask.di.module.NetworkModule
import ge.anthony_kharin.tapyoutesttask.di.module.RepositoryModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        NetworkModule::class,
        NavigationModule::class,
        RepositoryModule::class
    ]
)
interface ApplicationComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: TapYouTestTaskApplication): ApplicationComponent
    }

    fun activityComponent(): ActivityComponent.Factory
}