package `in`.aditya.brewkery.ui.screens.cart

import androidx.lifecycle.ViewModel
import `in`.aditya.brewkery.data.repository.CartRepository
import `in`.aditya.brewkery.domain.model.CartItem
import `in`.aditya.brewkery.domain.model.Order
import `in`.aditya.brewkery.domain.model.StoreMeta
import kotlinx.coroutines.flow.StateFlow

class CartViewModel(
    val cartRepository: CartRepository = CartRepository
) : ViewModel() {

    constructor() : this(CartRepository)

    val cartItems: StateFlow<List<CartItem>> = cartRepository.cartItems
    val storeMeta: StateFlow<StoreMeta> = cartRepository.storeMeta

    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        cartRepository.updateQuantity(cartItemId, newQuantity)
    }

    fun removeItem(cartItemId: String) {
        cartRepository.removeFromCart(cartItemId)
    }

    fun placeOrder(): Order? {
        return cartRepository.placeOrder()
    }
}
