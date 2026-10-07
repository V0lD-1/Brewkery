package `in`.aditya.brewkery.ui.screens.order

import androidx.lifecycle.ViewModel
import `in`.aditya.brewkery.data.repository.CartRepository
import `in`.aditya.brewkery.domain.model.Order
import `in`.aditya.brewkery.domain.model.StoreMeta
import kotlinx.coroutines.flow.StateFlow

class OrderStatusViewModel(
    val cartRepository: CartRepository = CartRepository
) : ViewModel() {

    constructor() : this(CartRepository)

    val activeOrder: StateFlow<Order?> = cartRepository.activeOrder
    val storeMeta: StateFlow<StoreMeta> = cartRepository.storeMeta
}
