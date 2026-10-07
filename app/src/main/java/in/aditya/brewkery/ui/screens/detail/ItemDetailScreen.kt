package `in`.aditya.brewkery.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import `in`.aditya.brewkery.domain.model.SizeOption
import `in`.aditya.brewkery.ui.theme.BadgeBg
import `in`.aditya.brewkery.ui.theme.BadgeText
import `in`.aditya.brewkery.ui.theme.BorderLight
import `in`.aditya.brewkery.ui.theme.EspressoDark
import `in`.aditya.brewkery.ui.theme.RatingStar
import `in`.aditya.brewkery.ui.theme.TerracottaPrimary
import `in`.aditya.brewkery.ui.theme.TextDark
import `in`.aditya.brewkery.ui.theme.TextMuted
import `in`.aditya.brewkery.ui.theme.WarmBackground
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ItemDetailScreen(
    onBackClick: () -> Unit,
    onItemAddedToCart: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
    ) {
        when (val state = uiState) {
            is DetailUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = TerracottaPrimary)
                }
            }

            is DetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error Loading Item",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = state.message, color = TextMuted, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.loadItemDetail() },
                            colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            is DetailUiState.Success -> {
                val item = state.menuItem

                Column(modifier = Modifier.fillMaxSize()) {
                    // Top Header Row with ITEM CUSTOMIZER title & Favorite button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Back Button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, BorderLight, CircleShape)
                                .clickable { onBackClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Title
                        Text(
                            text = "ITEM CUSTOMIZER",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        // Favorite Heart Button
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.dp, BorderLight, CircleShape)
                                .clickable { viewModel.toggleFavorite() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = Color(0xFFE0533C),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        // 1. Image Viewer Card
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(260.dp),
                                    shape = RoundedCornerShape(24.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        AsyncImage(
                                            model = item.imageUrl,
                                            contentDescription = item.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        // Dark Overlay Badge (e.g. BESTSELLER)
                                        if (item.badge.isNotEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(16.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(EspressoDark.copy(alpha = 0.85f))
                                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                                    .align(Alignment.TopStart)
                                            ) {
                                                Text(
                                                    text = item.badge,
                                                    color = RatingStar,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 2. Info Section (Title + Price + Description)
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 8.dp)
                            ) {
                                // Title & Price Row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Text(
                                        text = "$${String.format(Locale.US, "%.2f", item.basePrice)}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TerracottaPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Rating & Prep Info Row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = RatingStar,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = item.rating.toString(),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark
                                        )
                                        Text(
                                            text = " (${item.reviewCount} reviews)",
                                            fontSize = 12.sp,
                                            color = TextMuted
                                        )
                                    }

                                    if (item.prepTime.isNotEmpty()) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Schedule,
                                                contentDescription = null,
                                                tint = TextMuted,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = item.prepTime,
                                                fontSize = 12.sp,
                                                color = TextDark,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    if (item.calories > 0) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.LocalFireDepartment,
                                                contentDescription = null,
                                                tint = TerracottaPrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${item.calories} kcal",
                                                fontSize = 12.sp,
                                                color = TextDark,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Description Paragraph
                                Text(
                                    text = item.description,
                                    fontSize = 14.sp,
                                    color = TextMuted,
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Ingredients Section
                                if (item.ingredients.isNotEmpty()) {
                                    Text(
                                        text = "Ingredients",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    FlowRow(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        item.ingredients.forEach { ingredient ->
                                            Surface(
                                                shape = RoundedCornerShape(16.dp),
                                                color = Color.White,
                                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
                                            ) {
                                                Text(
                                                    text = ingredient,
                                                    fontSize = 12.sp,
                                                    color = TextDark,
                                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))
                                }

                                // 3. Customizations Section
                                val custom = item.customizations

                                // Sizes
                                if (custom.sizes.isNotEmpty()) {
                                    CustomizationSectionTitle("Select Size", "Required")
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        custom.sizes.forEach { sizeOption ->
                                            val isSelected = state.selectedSize?.id == sizeOption.id
                                            OptionCard(
                                                title = sizeOption.label,
                                                priceExtra = sizeOption.extraPrice,
                                                isSelected = isSelected,
                                                onClick = { viewModel.selectSize(sizeOption) }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))
                                }

                                // Sugar Levels
                                if (custom.sugarLevels.isNotEmpty()) {
                                    CustomizationSectionTitle("Sweetness Level", "Optional")
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        custom.sugarLevels.forEach { sugar ->
                                            val isSelected = state.selectedSugar == sugar
                                            OptionCard(
                                                title = sugar,
                                                priceExtra = 0.0,
                                                isSelected = isSelected,
                                                onClick = { viewModel.selectSugar(sugar) }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))
                                }

                                // Milk Options
                                if (custom.milkOptions.isNotEmpty()) {
                                    CustomizationSectionTitle("Milk / Base Choice", "Optional")
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        custom.milkOptions.forEach { milkOption ->
                                            val isSelected = state.selectedMilk?.id == milkOption.id
                                            OptionCard(
                                                title = milkOption.name,
                                                priceExtra = milkOption.extraPrice,
                                                isSelected = isSelected,
                                                onClick = { viewModel.selectMilk(milkOption) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 4. Bottom Action Bar
                    BottomActionBar(
                        quantity = state.quantity,
                        totalPrice = state.totalPrice,
                        onDecreaseQty = { viewModel.updateQuantity(-1) },
                        onIncreaseQty = { viewModel.updateQuantity(1) },
                        onAddToCart = {
                            if (viewModel.addToCart()) {
                                onItemAddedToCart()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomizationSectionTitle(title: String, badge: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(
            text = badge,
            fontSize = 12.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun OptionCard(
    title: String,
    priceExtra: Double,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) TerracottaPrimary else BorderLight
    val containerColor = if (isSelected) TerracottaPrimary.copy(alpha = 0.08f) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, borderColor, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(
                            width = if (isSelected) 6.dp else 2.dp,
                            color = if (isSelected) TerracottaPrimary else TextMuted.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = TextDark
                )
            }

            if (priceExtra > 0) {
                Text(
                    text = "+$${String.format(Locale.US, "%.2f", priceExtra)}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TerracottaPrimary
                )
            }
        }
    }
}

@Composable
private fun BottomActionBar(
    quantity: Int,
    totalPrice: Double,
    onDecreaseQty: () -> Unit,
    onIncreaseQty: () -> Unit,
    onAddToCart: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Quantity Control
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(WarmBackground)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onDecreaseQty() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = TextDark,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Text(
                    text = quantity.toString(),
                    modifier = Modifier.padding(horizontal = 14.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onIncreaseQty() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = TextDark,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Add to Cart Button
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EspressoDark)
            ) {
                Text(
                    text = "Add to Cart • $${String.format(Locale.US, "%.2f", totalPrice)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
