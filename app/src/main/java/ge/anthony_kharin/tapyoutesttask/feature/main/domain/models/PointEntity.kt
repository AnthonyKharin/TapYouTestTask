package ge.anthony_kharin.tapyoutesttask.feature.main.domain.models

// В domain слой просочилась android.os зависимость
// Мое лично мнение, что в этом ничего страшного нет
// Чистая архитектура - это рекомендации к коду, а не правила
// В данном конкретно случае не вижу смысла мапить к такой же сущности presentation слоя
// Однако если на проекте так принято, то можно будет это сделать, не большая проблема
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class PointEntity(
    val x: Double,
    val y: Double
) : Parcelable

@Parcelize
@Serializable
class PointsContainerEntity(
    val points: List<PointEntity>
) : Parcelable
