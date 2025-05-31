package ge.anthony_kharin.tapyoutesttask.feature.main.data.api

import ge.anthony_kharin.tapyoutesttask.feature.main.domain.models.PointsContainerEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Поскольку модель не меняется от слоя к слою, то и нет смысла создавать модель респонса
    // В случае реального проекта в таком же кейсе я бы оставил одну модель
    // Но в случае если ее необходимо будет мапить, то сделал бы соответсвующий маппер
    @GET("/api/test/points")
    suspend fun getPoints(@Query("count") count: Int): PointsContainerEntity
}
