package `in`.aditya.brewkery.data.remote

import `in`.aditya.brewkery.data.model.MenuItemDto
import `in`.aditya.brewkery.data.model.MenuResponseDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface BrewkeryApiService {

    @GET("data.json")
    suspend fun getFullMenu(): MenuResponseDto

    @GET("api/items/{id}.json")
    suspend fun getItemDetail(@Path("id") id: Int): MenuItemDto
}

object RetrofitClient {
    private const val BASE_URL = "https://raw.githubusercontent.com/VivekShah138/Brewkery/main/"

    val apiService: BrewkeryApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BrewkeryApiService::class.java)
    }
}
