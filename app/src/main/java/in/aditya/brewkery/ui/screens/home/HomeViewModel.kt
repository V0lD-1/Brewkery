package `in`.aditya.brewkery.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.aditya.brewkery.data.repository.BrewkeryRepository
import `in`.aditya.brewkery.data.repository.BrewkeryRepositoryImpl
import `in`.aditya.brewkery.data.repository.CartRepository
import `in`.aditya.brewkery.domain.model.Category
import `in`.aditya.brewkery.domain.model.MenuItem
import `in`.aditya.brewkery.domain.model.Order
import `in`.aditya.brewkery.domain.model.StoreMeta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(
        val meta: StoreMeta,
        val categories: List<Category>,
        val items: List<MenuItem>,
        val selectedCategoryId: String = "ALL",
        val searchQuery: String = "",
        val favoriteIds: Set<Int> = emptySet()
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(
    private val repository: BrewkeryRepository = BrewkeryRepositoryImpl(),
    val cartRepository: CartRepository = CartRepository
) : ViewModel() {

    constructor() : this(BrewkeryRepositoryImpl(), CartRepository)

    private val _rawState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    private val _selectedCategoryId = MutableStateFlow("ALL")
    private val _searchQuery = MutableStateFlow("")

    val cartItems = cartRepository.cartItems
    val activeOrder: StateFlow<Order?> = cartRepository.activeOrder
    val favoriteIds = cartRepository.favoriteItemIds

    val uiState: StateFlow<HomeUiState> = combine(
        _rawState,
        _selectedCategoryId,
        _searchQuery,
        cartRepository.favoriteItemIds
    ) { state, selectedCat, query, favorites ->
        if (state is HomeUiState.Success) {
            val filteredItems = state.items.filter { item ->
                val matchesCategory = when (selectedCat) {
                    "ALL" -> true
                    "FAVORITES" -> favorites.contains(item.id)
                    else -> item.categoryId == selectedCat
                }
                val matchesQuery = query.isBlank() ||
                        item.name.contains(query, ignoreCase = true) ||
                        item.tagline.contains(query, ignoreCase = true) ||
                        item.description.contains(query, ignoreCase = true)
                matchesCategory && matchesQuery
            }
            state.copy(
                items = filteredItems,
                selectedCategoryId = selectedCat,
                searchQuery = query,
                favoriteIds = favorites
            )
        } else {
            state
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    private var allItems: List<MenuItem> = emptyList()

    init {
        loadMenu()
    }

    fun loadMenu() {
        viewModelScope.launch {
            _rawState.value = HomeUiState.Loading
            repository.getFullMenu()
                .onSuccess { fullMenu ->
                    allItems = fullMenu.items
                    cartRepository.updateStoreMeta(fullMenu.meta)
                    _rawState.value = HomeUiState.Success(
                        meta = fullMenu.meta,
                        categories = fullMenu.categories,
                        items = fullMenu.items,
                        selectedCategoryId = _selectedCategoryId.value,
                        searchQuery = _searchQuery.value,
                        favoriteIds = cartRepository.favoriteItemIds.value
                    )
                }
                .onFailure { error ->
                    _rawState.value = HomeUiState.Error(
                        error.localizedMessage ?: "Failed to load menu. Please check your network connection."
                    )
                }
        }
    }

    fun selectCategory(categoryId: String) {
        _selectedCategoryId.value = categoryId
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(itemId: Int) {
        cartRepository.toggleFavorite(itemId)
    }
}
