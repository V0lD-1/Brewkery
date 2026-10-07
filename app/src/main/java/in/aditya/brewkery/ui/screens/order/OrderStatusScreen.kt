package `in`.aditya.brewkery.ui.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import `in`.aditya.brewkery.domain.model.Order
import `in`.aditya.brewkery.ui.theme.BadgeBg
import `in`.aditya.brewkery.ui.theme.BadgeText
import `in`.aditya.brewkery.ui.theme.BorderLight
import `in`.aditya.brewkery.ui.theme.EspressoDark
import `in`.aditya.brewkery.ui.theme.StoreBannerBg
import `in`.aditya.brewkery.ui.theme.TerracottaPrimary
import `in`.aditya.brewkery.ui.theme.TextDark
import `in`.aditya.brewkery.ui.theme.TextMuted
import `in`.aditya.brewkery.ui.theme.WarmBackground
import java.util.Locale

@Composable
fun OrderStatusScreen(
    onHomeClick: () -> Unit,
    viewModel: OrderStatusViewModel = viewModel()
) {
    val activeOrder by viewModel.activeOrder.collectAsState()
    val storeMeta by viewModel.storeMeta.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmBackground)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Order Status",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            IconButton(
                onClick = onHomeClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = TextDark
                )
            }
        }

        if (activeOrder == null) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "No active order found", fontSize = 16.sp, color = TextMuted)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onHomeClick,
                        colors = ButtonDefaults.buttonColors(containerColor = TerracottaPrimary)
                    ) {
                        Text("Back to Menu")
                    }
                }
            }
        } else {
            val order = activeOrder!!

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Status Header Card
                item {
                    StatusHeroCard(
                        order = order,
                        estimatedDelivery = storeMeta.estimatedDeliveryTime
                    )
                }

                // Progress Tracker Steps Card
                item {
                    StatusTrackerCard()
                }

                // Ordered Items Summary
                item {
                    OrderItemsCard(order = order, currencySymbol = storeMeta.currencySymbol)
                }

                // Delivery Info Card
                item {
                    DeliveryInfoCard(
                        grandTotal = order.total,
                        currencySymbol = storeMeta.currencySymbol
                    )
                }
            }

            // Bottom Action Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Button(
                        onClick = onHomeClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(26.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EspressoDark)
                    ) {
                        Text(
                            text = "Back to Menu",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusHeroCard(
    order: Order,
    estimatedDelivery: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = StoreBannerBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(TerracottaPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "☕",
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(BadgeBg)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "PREPARING YOUR ORDER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BadgeText
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Order ${order.id}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Our barista is carefully crafting your order.",
                fontSize = 13.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = TerracottaPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Estimated Arrival: $estimatedDelivery",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark
                )
            }
        }
    }
}

@Composable
private fun StatusTrackerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Order Progress",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            TrackerStepItem(
                stepNumber = "1",
                title = "Order Confirmed",
                subtitle = "Your order was received and verified",
                isCompleted = true,
                isActive = false
            )

            TrackerStepItem(
                stepNumber = "2",
                title = "Preparing & Brewing",
                subtitle = "Fresh espresso extraction and pastry warming",
                isCompleted = false,
                isActive = true
            )

            TrackerStepItem(
                stepNumber = "3",
                title = "Out for Delivery",
                subtitle = "Rider will pick up soon",
                isCompleted = false,
                isActive = false
            )

            TrackerStepItem(
                stepNumber = "4",
                title = "Delivered",
                subtitle = "Enjoy your fresh Brewkery treats!",
                isCompleted = false,
                isActive = false,
                isLast = true
            )
        }
    }
}

@Composable
private fun TrackerStepItem(
    stepNumber: String,
    title: String,
    subtitle: String,
    isCompleted: Boolean,
    isActive: Boolean,
    isLast: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isCompleted -> TerracottaPrimary
                            isActive -> BadgeBg
                            else -> BorderLight
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = stepNumber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) BadgeText else TextMuted
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(if (isCompleted) TerracottaPrimary else BorderLight)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isActive || isCompleted) FontWeight.Bold else FontWeight.Medium,
                color = if (isActive || isCompleted) TextDark else TextMuted
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OrderItemsCard(order: Order, currencySymbol: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Items in Order",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = item.menuItem.imageUrl,
                        contentDescription = item.menuItem.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${item.quantity}x ${item.menuItem.name}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = item.optionsDescription,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }

                    Text(
                        text = "$currencySymbol${String.format(Locale.US, "%.2f", item.totalPrice)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TerracottaPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun DeliveryInfoCard(grandTotal: Double, currencySymbol: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(StoreBannerBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = TerracottaPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Delivery Location", fontSize = 12.sp, color = TextMuted)
                Text(
                    text = "742 Evergreen Terrace, Apt 4B",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Total Paid", fontSize = 11.sp, color = TextMuted)
                Text(
                    text = "$currencySymbol${String.format(Locale.US, "%.2f", grandTotal)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TerracottaPrimary
                )
            }
        }
    }
}
