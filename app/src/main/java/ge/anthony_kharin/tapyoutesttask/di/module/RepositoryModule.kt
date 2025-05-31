package ge.anthony_kharin.tapyoutesttask.di.module

import dagger.Module
import dagger.Provides
import ge.anthony_kharin.tapyoutesttask.feature.main.data.PointsRepositoryImpl
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.repository.PointsRepository
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun providePointsRepository(
        repositoryImpl: PointsRepositoryImpl
    ): PointsRepository = repositoryImpl
}