package `in`.aditya.brewkery.data.repository

import `in`.aditya.brewkery.domain.model.CartItem
import `in`.aditya.brewkery.domain.model.Order
import `in`.aditya.brewkery.domain.model.OrderStatus
import `in`.aditya.brewkery.domain.model.StoreMeta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

object CartRepository {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _activeOrder = MutableStateFlow<Order?>(null)
    val activeOrder: StateFlow<Order?> = _activeOrder.asStateFlow()

    private val _storeMeta = MutableStateFlow(StoreMeta())
    val storeMeta: StateFlow<StoreMeta> = _storeMeta.asStateFlow()

    private val _favoriteItemIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteItemIds: StateFlow<Set<Int>> = _favoriteItemIds.asStateFlow()

    fun updateStoreMeta(meta: StoreMeta) {
        _storeMeta.value = meta
    }

    fun toggleFavorite(itemId: Int) {
        _favoriteItemIds.update { current ->
            if (current.contains(itemId)) {
                current - itemId
            } else {
                current + itemId
            }
        }
    }

    fun addToCart(item: CartItem) {
        _cartItems.update { current ->
            val existingIndex = current.indexOfFirst {
                it.menuItem.id == item.menuItem.id &&
                        it.selectedSize == item.selectedSize &&
                        it.selectedSugar == item.selectedSugar &&
                        it.selectedMilk == item.selectedMilk
            }
            if (existingIndex >= 0) {
                val existing = current[existingIndex]
                val updated = existing.copy(quantity = existing.quantity + item.quantity)
                current.toMutableList().apply { set(existingIndex, updated) }
            } else {
                current + item
            }
        }
    }

    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(cartItemId)
            return
        }
        _cartItems.update { current ->
            current.map {
                if (it.id == cartItemId) it.copy(quantity = newQuantity) else it
            }
        }
    }

    fun removeFromCart(cartItemId: String) {
        _cartItems.update { current ->
            current.filterNot { it.id == cartItemId }
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    fun placeOrder(): Order? {
        val currentItems = _cartItems.value
        if (currentItems.isEmpty()) return null

        val subtotal = currentItems.sumOf { it.totalPrice }
        val meta = _storeMeta.value
        val deliveryFee = meta.deliveryFee
        val tax = subtotal * (meta.taxRatePercent / 100.0)
        val total = subtotal + deliveryFee + tax

        val ticketNumber = Random.nextInt(1000, 9999)
        val order = Order(
            id = "#BK-$ticketNumber",
            items = currentItems,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            tax = tax,
            total = total,
            status = OrderStatus.PREPARING
        )

        _activeOrder.value = order
        clearCart()
        return order
    }
}
