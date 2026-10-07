package `in`.aditya.brewkery.domain.model

import java.util.UUID

data class StoreMeta(
    val app: String = "Brewkery",
    val version: String = "1.0.0",
    val tagline: String = "Artisanal Coffee & Fresh Oven Bakes",
    val currency: String = "USD",
    val currencySymbol: String = "$",
    val deliveryFee: Double = 2.50,
    val taxRatePercent: Double = 8.0,
    val estimatedDeliveryTime: String = "20 - 30 mins"
)

data class Category(
    val id: String,
    val name: String,
    val icon: String,
    val itemCount: Int
)

data class SizeOption(
    val id: String,
    val label: String,
    val extraPrice: Double
)

data class MilkOption(
    val id: String,
    val name: String,
    val extraPrice: Double
)

data class Customizations(
    val sizes: List<SizeOption> = emptyList(),
    val sugarLevels: List<String> = emptyList(),
    val milkOptions: List<MilkOption> = emptyList()
)

data class MenuItem(
    val id: Int,
    val categoryId: String,
    val name: String,
    val tagline: String,
    val description: String,
    val basePrice: Double,
    val rating: Double,
    val reviewCount: Int,
    val prepTime: String,
    val calories: Int,
    val imageUrl: String,
    val badge: String,
    val ingredients: List<String> = emptyList(),
    val customizations: Customizations = Customizations()
)

data class CartItem(
    val id: String = UUID.randomUUID().toString(),
    val menuItem: MenuItem,
    val selectedSize: SizeOption? = null,
    val selectedSugar: String? = null,
    val selectedMilk: MilkOption? = null,
    val quantity: Int = 1
) {
    val unitPrice: Double
        get() {
            var price = menuItem.basePrice
            if (selectedSize != null) price += selectedSize.extraPrice
            if (selectedMilk != null) price += selectedMilk.extraPrice
            return price
        }

    val totalPrice: Double
        get() = unitPrice * quantity

    val optionsDescription: String
        get() {
            val parts = mutableListOf<String>()
            selectedSize?.let { parts.add(it.label) }
            selectedSugar?.let { parts.add(it) }
            selectedMilk?.let { parts.add(it.name) }
            return if (parts.isEmpty()) "Standard" else parts.joinToString(" • ")
        }
}

enum class OrderStatus {
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}

data class Order(
    val id: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val deliveryFee: Double,
    val tax: Double,
    val total: Double,
    val status: OrderStatus = OrderStatus.PREPARING,
    val timestamp: Long = System.currentTimeMillis()
)
