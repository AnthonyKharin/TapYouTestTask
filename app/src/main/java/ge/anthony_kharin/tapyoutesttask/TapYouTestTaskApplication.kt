package ge.anthony_kharin.tapyoutesttask

import android.app.Application
import ge.anthony_kharin.tapyoutesttask.di.component.DaggerApplicationComponent
import ge.anthony_kharin.tapyoutesttask.di.component.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class TapYouTestTaskApplication : Application() {

    lateinit var appComponent: ApplicationComponent
        private set

    val applicationScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.factory().create(this)
    }
}