package ge.anthony_kharin.tapyoutesttask.di.module

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import ge.anthony_kharin.tapyoutesttask.TapYouTestTaskApplication
import ge.anthony_kharin.tapyoutesttask.di.qualifiers.ApplicationCoroutineScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
class AndroidModule {
    @Provides
    @Singleton
    fun provideContext(app: TapYouTestTaskApplication): Context = app


    @Provides
    @Singleton
    fun provideResources(context: Context): Resources = context.resources

    @Provides
    @ApplicationCoroutineScope
    fun provideApplicationScope(app: TapYouTestTaskApplication): CoroutineScope =
        app.applicationScope
}