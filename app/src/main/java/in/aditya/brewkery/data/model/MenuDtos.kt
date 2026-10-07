package `in`.aditya.brewkery.data.model

import com.google.gson.annotations.SerializedName
import `in`.aditya.brewkery.domain.model.Category
import `in`.aditya.brewkery.domain.model.Customizations
import `in`.aditya.brewkery.domain.model.MenuItem
import `in`.aditya.brewkery.domain.model.MilkOption
import `in`.aditya.brewkery.domain.model.SizeOption
import `in`.aditya.brewkery.domain.model.StoreMeta

data class MenuResponseDto(
    @SerializedName("meta") val meta: MetaDto?,
    @SerializedName("categories") val categories: List<CategoryDto>?,
    @SerializedName("items") val items: List<MenuItemDto>?
)

data class MetaDto(
    @SerializedName("app") val app: String?,
    @SerializedName("version") val version: String?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("currency_symbol") val currencySymbol: String?,
    @SerializedName("delivery_fee") val deliveryFee: Double?,
    @SerializedName("tax_rate_percent") val taxRatePercent: Double?,
    @SerializedName("estimated_delivery_time") val estimatedDeliveryTime: String?
) {
    fun toDomain(): StoreMeta = StoreMeta(
        app = app ?: "Brewkery",
        version = version ?: "1.0.0",
        tagline = tagline ?: "Artisanal Coffee & Fresh Oven Bakes",
        currency = currency ?: "USD",
        currencySymbol = currencySymbol ?: "$",
        deliveryFee = deliveryFee ?: 2.50,
        taxRatePercent = taxRatePercent ?: 8.0,
        estimatedDeliveryTime = estimatedDeliveryTime ?: "20 - 30 mins"
    )
}

data class CategoryDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("item_count") val itemCount: Int?
) {
    fun toDomain(): Category = Category(
        id = id.orEmpty(),
        name = name.orEmpty(),
        icon = icon.orEmpty(),
        itemCount = itemCount ?: 0
    )
}

data class MenuItemDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("category_id") val categoryId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("tagline") val tagline: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("base_price") val basePrice: Double?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("review_count") val reviewCount: Int?,
    @SerializedName("prep_time") val prepTime: String?,
    @SerializedName("calories") val calories: Int?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("badge") val badge: String?,
    @SerializedName("ingredients") val ingredients: List<String>?,
    @SerializedName("customizations") val customizations: CustomizationsDto?
) {
    fun toDomain(): MenuItem = MenuItem(
        id = id ?: 0,
        categoryId = categoryId.orEmpty(),
        name = name.orEmpty(),
        tagline = tagline.orEmpty(),
        description = description.orEmpty(),
        basePrice = basePrice ?: 0.0,
        rating = rating ?: 0.0,
        reviewCount = reviewCount ?: 0,
        prepTime = prepTime ?: "",
        calories = calories ?: 0,
        imageUrl = imageUrl.orEmpty(),
        badge = badge.orEmpty(),
        ingredients = ingredients ?: emptyList(),
        customizations = customizations?.toDomain() ?: Customizations()
    )
}

data class CustomizationsDto(
    @SerializedName("sizes") val sizes: List<SizeDto>?,
    @SerializedName("sugar_levels") val sugarLevels: List<String>?,
    @SerializedName("milk_options") val milkOptions: List<MilkOptionDto>?
) {
    fun toDomain(): Customizations = Customizations(
        sizes = sizes?.map { it.toDomain() } ?: emptyList(),
        sugarLevels = sugarLevels ?: emptyList(),
        milkOptions = milkOptions?.map { it.toDomain() } ?: emptyList()
    )
}

data class SizeDto(
    @SerializedName("id") val id: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("extra_price") val extraPrice: Double?
) {
    fun toDomain(): SizeOption = SizeOption(
        id = id.orEmpty(),
        label = label.orEmpty(),
        extraPrice = extraPrice ?: 0.0
    )
}

data class MilkOptionDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("extra_price") val extraPrice: Double?
) {
    fun toDomain(): MilkOption = MilkOption(
        id = id.orEmpty(),
        name = name.orEmpty(),
        extraPrice = extraPrice ?: 0.0
    )
}
