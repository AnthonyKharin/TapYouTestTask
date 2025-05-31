package ge.anthony_kharin.tapyoutesttask.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointsContainerEntity
import ge.anthony_kharin.tapyoutesttask.feature.main.presentation.view.MainFragment
import ge.anthony_kharin.tapyoutesttask.feature.pointDetails.presentation.view.PointDetailsFragment

object Screens {
    fun main() = FragmentScreen { MainFragment.newInstance() }
    fun pointDetails(pointsContainerEntity: PointsContainerEntity) = FragmentScreen {
        PointDetailsFragment.newInstance(
            pointsContainerEntity = pointsContainerEntity
        )
    }
}