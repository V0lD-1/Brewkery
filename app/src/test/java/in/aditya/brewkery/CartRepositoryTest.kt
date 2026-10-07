package `in`.aditya.brewkery

import `in`.aditya.brewkery.data.repository.CartRepository
import `in`.aditya.brewkery.domain.model.CartItem
import `in`.aditya.brewkery.domain.model.MenuItem
import `in`.aditya.brewkery.domain.model.SizeOption
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CartRepositoryTest {

    @Before
    fun setUp() {
        CartRepository.clearCart()
    }

    @Test
    fun addToCart_increasesQuantity_whenSameItemAdded() {
        val dummyItem = MenuItem(
            id = 1,
            categoryId = "coffee",
            name = "Espresso",
            tagline = "Strong and bold",
            description = "Rich espresso shot",
            basePrice = 3.50,
            rating = 4.8,
            reviewCount = 120,
            prepTime = "5 mins",
            calories = 10,
            imageUrl = "",
            badge = "Bestseller"
        )

        val size = SizeOption(id = "m", label = "Medium", extraPrice = 0.50)

        val cartItem = CartItem(
            menuItem = dummyItem,
            selectedSize = size,
            quantity = 1
        )

        CartRepository.addToCart(cartItem)
        CartRepository.addToCart(cartItem)

        val items = CartRepository.cartItems.value
        assertEquals(1, items.size)
        assertEquals(2, items[0].quantity)
        assertEquals(8.00, items[0].totalPrice, 0.001)
    }

    @Test
    fun removeFromCart_removesItemSuccessfully() {
        val dummyItem = MenuItem(
            id = 1,
            categoryId = "coffee",
            name = "Espresso",
            tagline = "Strong and bold",
            description = "Rich espresso shot",
            basePrice = 3.50,
            rating = 4.8,
            reviewCount = 120,
            prepTime = "5 mins",
            calories = 10,
            imageUrl = "",
            badge = "Bestseller"
        )

        val size = SizeOption(id = "m", label = "Medium", extraPrice = 0.50)
        val cartItem = CartItem(
            menuItem = dummyItem,
            selectedSize = size,
            quantity = 1
        )
    }
}
