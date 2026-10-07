package `in`.aditya.brewkery.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.aditya.brewkery.data.repository.BrewkeryRepository
import `in`.aditya.brewkery.data.repository.BrewkeryRepositoryImpl
import `in`.aditya.brewkery.data.repository.CartRepository
import `in`.aditya.brewkery.domain.model.CartItem
import `in`.aditya.brewkery.domain.model.MenuItem
import `in`.aditya.brewkery.domain.model.MilkOption
import `in`.aditya.brewkery.domain.model.SizeOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Success(
        val menuItem: MenuItem,
        val selectedSize: SizeOption?,
        val selectedSugar: String?,
        val selectedMilk: MilkOption?,
        val quantity: Int = 1,
        val unitPrice: Double,
        val totalPrice: Double
    ) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: BrewkeryRepository = BrewkeryRepositoryImpl(),
    private val cartRepository: CartRepository = CartRepository
) : ViewModel() {

    constructor(savedStateHandle: SavedStateHandle) : this(
        savedStateHandle = savedStateHandle,
        repository = BrewkeryRepositoryImpl(),
        cartRepository = CartRepository
    )

    private val itemId: Int = savedStateHandle.get<Int>("itemId")
        ?: savedStateHandle.get<String>("itemId")?.toIntOrNull()
        ?: 1

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    val isFavorite: StateFlow<Boolean> = cartRepository.favoriteItemIds
        .map { it.contains(itemId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = cartRepository.favoriteItemIds.value.contains(itemId)
        )

    init {
        loadItemDetail()
    }

    fun loadItemDetail() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading
            repository.getItemDetail(itemId)
                .onSuccess { item ->
                    val defaultSize = item.customizations.sizes.firstOrNull()
                    val defaultSugar = item.customizations.sugarLevels.firstOrNull()
                    val defaultMilk = item.customizations.milkOptions.firstOrNull()
                    val unit = calculateUnitPrice(item, defaultSize, defaultMilk)

                    _uiState.value = DetailUiState.Success(
                        menuItem = item,
                        selectedSize = defaultSize,
                        selectedSugar = defaultSugar,
                        selectedMilk = defaultMilk,
                        quantity = 1,
                        unitPrice = unit,
                        totalPrice = unit
                    )
                }
                .onFailure {
                    // Fallback to fetch full menu if single item route fails or isn't formatted
                    repository.getFullMenu()
                        .onSuccess { fullMenu ->
                            val item = fullMenu.items.find { it.id == itemId }
                            if (item != null) {
                                val defaultSize = item.customizations.sizes.firstOrNull()
                                val defaultSugar = item.customizations.sugarLevels.firstOrNull()
                                val defaultMilk = item.customizations.milkOptions.firstOrNull()
                                val unit = calculateUnitPrice(item, defaultSize, defaultMilk)

                                _uiState.value = DetailUiState.Success(
                                    menuItem = item,
                                    selectedSize = defaultSize,
                                    selectedSugar = defaultSugar,
                                    selectedMilk = defaultMilk,
                                    quantity = 1,
                                    unitPrice = unit,
                                    totalPrice = unit
                                )
                            } else {
                                _uiState.value = DetailUiState.Error("Item not found")
                            }
                        }
                        .onFailure { err ->
                            _uiState.value = DetailUiState.Error(err.localizedMessage ?: "Failed to load item detail.")
                        }
                }
        }
    }

    fun toggleFavorite() {
        cartRepository.toggleFavorite(itemId)
    }

    private fun calculateUnitPrice(
        item: MenuItem,
        size: SizeOption?,
        milk: MilkOption?
    ): Double {
        var price = item.basePrice
        if (size != null) price += size.extraPrice
        if (milk != null) price += milk.extraPrice
        return price
    }

    fun selectSize(size: SizeOption) {
        _uiState.update { current ->
            if (current is DetailUiState.Success) {
                val unit = calculateUnitPrice(current.menuItem, size, current.selectedMilk)
                current.copy(
                    selectedSize = size,
                    unitPrice = unit,
                    totalPrice = unit * current.quantity
                )
            } else current
        }
    }

    fun selectSugar(sugar: String) {
        _uiState.update { current ->
            if (current is DetailUiState.Success) {
                current.copy(selectedSugar = sugar)
            } else current
        }
    }

    fun selectMilk(milk: MilkOption) {
        _uiState.update { current ->
            if (current is DetailUiState.Success) {
                val unit = calculateUnitPrice(current.menuItem, current.selectedSize, milk)
                current.copy(
                    selectedMilk = milk,
                    unitPrice = unit,
                    totalPrice = unit * current.quantity
                )
            } else current
        }
    }

    fun updateQuantity(delta: Int) {
        _uiState.update { current ->
            if (current is DetailUiState.Success) {
                val newQty = (current.quantity + delta).coerceAtLeast(1)
                current.copy(
                    quantity = newQty,
                    totalPrice = current.unitPrice * newQty
                )
            } else current
        }
    }

    fun addToCart(): Boolean {
        val state = _uiState.value
        if (state is DetailUiState.Success) {
            val cartItem = CartItem(
                menuItem = state.menuItem,
                selectedSize = state.selectedSize,
                selectedSugar = state.selectedSugar,
                selectedMilk = state.selectedMilk,
                quantity = state.quantity
            )
            cartRepository.addToCart(cartItem)
            return true
        }
        return false
    }
}
