package `in`.aditya.brewkery.data.repository

import `in`.aditya.brewkery.data.remote.BrewkeryApiService
import `in`.aditya.brewkery.data.remote.RetrofitClient
import `in`.aditya.brewkery.domain.model.Category
import `in`.aditya.brewkery.domain.model.MenuItem
import `in`.aditya.brewkery.domain.model.StoreMeta

data class FullMenuData(
    val meta: StoreMeta,
    val categories: List<Category>,
    val items: List<MenuItem>
)

interface BrewkeryRepository {
    suspend fun getFullMenu(): Result<FullMenuData>
    suspend fun getItemDetail(id: Int): Result<MenuItem>
}

class BrewkeryRepositoryImpl(
    private val apiService: BrewkeryApiService = RetrofitClient.apiService
) : BrewkeryRepository {

    override suspend fun getFullMenu(): Result<FullMenuData> {
        return runCatching {
            val response = apiService.getFullMenu()
            val meta = response.meta?.toDomain() ?: StoreMeta()
            val categories = response.categories?.map { it.toDomain() } ?: emptyList()
            val items = response.items?.map { it.toDomain() } ?: emptyList()
            FullMenuData(meta, categories, items)
        }
    }

    override suspend fun getItemDetail(id: Int): Result<MenuItem> {
        return runCatching {
            apiService.getItemDetail(id).toDomain()
        }
    }
}
