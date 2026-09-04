package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Elevator
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PropertyEntity
import com.example.ui.theme.FavoriteRed
import com.example.ui.theme.TagBrokerBg
import com.example.ui.theme.TagBrokerCyan
import com.example.ui.theme.TagGreen
import com.example.ui.theme.TagGreenBg
import com.example.ui.theme.TagOfficeBg
import com.example.ui.theme.TagOfficeIndigo
import com.example.ui.theme.TagOwnerBg
import com.example.ui.theme.TagOwnerBlue
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PropertyCard(
    property: PropertyEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getNumberInstance(Locale("ar", "SA"))
    val formattedPrice = formatter.format(property.price)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .testTag("property_card_${property.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Image Header with overlay badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = PropertyImageHelper.getDrawableForName(property.imageResName)),
                    contentDescription = property.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Dark gradient overlay for contrast
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                                startY = 80f
                            )
                        )
                )

                // Top Status Badges (Sale/Rent, Type, and Publisher Badge)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            val isRent = property.purpose == "للإيجار"
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isRent) TagGreenBg else MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
                            ) {
                                Text(
                                    text = property.purpose,
                                    color = if (isRent) TagGreen else MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.Black.copy(alpha = 0.65f)
                            ) {
                                Text(
                                    text = property.type,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Publisher Badge (صاحب العقار، وسيط، مكتب)
                        val (badgeBg, badgeColor) = when (property.publisherType) {
                            "owner" -> Pair(TagOwnerBg, TagOwnerBlue)
                            "agency" -> Pair(TagOfficeBg, TagOfficeIndigo)
                            else -> Pair(TagBrokerBg, TagBrokerCyan)
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = badgeBg.copy(alpha = 0.95f),
                            border = BorderStroke(0.8.dp, badgeColor.copy(alpha = 0.6f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Verified,
                                    contentDescription = null,
                                    tint = badgeColor,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = property.publisherBadge,
                                    color = badgeColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Favorite Button
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                        modifier = Modifier.size(36.dp)
                    ) {
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("favorite_button_${property.id}")
                        ) {
                            Icon(
                                imageVector = if (property.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = if (property.isFavorite) "إزالة من المفضلة" else "إضافة للمفضلة",
                                tint = if (property.isFavorite) FavoriteRed else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Price Tag overlay at bottom
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = formattedPrice,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = property.currency,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }

            // Card Body Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = property.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Location Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "الموقع",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${property.neighborhood}، ${property.city}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Property-Specific Harmonized Features (ينسجم مع نوع العقار)
                HarmonizedSpecificationsRow(property = property)
            }
        }
    }
}

/**
 * Renders specifications that harmoniously adapt to the property type:
 * - أرض: المساحة، سعر المتر التقديري، المواصفة/الشارع
 * - عمارة: المساحة، العائد الاستثماري/الوحدات، المصعد
 * - شقة: المساحة، غرف النوم، دورات المياه
 * - فيلا: المساحة، الغرف، المسبح/الحوش
 * - مكتب: المساحة، المواقف، التكييف/التشطيب
 * - شاليه: المساحة، المسبح، المسطح الأخضر
 */
@Composable
fun HarmonizedSpecificationsRow(property: PropertyEntity) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Universal: Area
        SpecItem(
            icon = Icons.Filled.SquareFoot,
            label = "${property.area} م²"
        )

        when (property.type) {
            "أرض" -> {
                val pricePerMeter = if (property.area > 0) property.price / property.area else 0
                SpecItem(
                    icon = Icons.Filled.Landscape,
                    label = if (pricePerMeter > 0) "$pricePerMeter م²" else "صك إلكتروني"
                )
                SpecItem(
                    icon = Icons.Filled.Verified,
                    label = if (property.purpose == "للبيع") "سكني/تجاري" else "موقع مميز"
                )
            }
            "عمارة" -> {
                SpecItem(
                    icon = Icons.Filled.TrendingUp,
                    label = "عائد استثماري"
                )
                SpecItem(
                    icon = Icons.Filled.Business,
                    label = "${property.bedrooms} وحدة"
                )
            }
            "شاليه" -> {
                SpecItem(
                    icon = Icons.Filled.Pool,
                    label = "مسبح خاص"
                )
                SpecItem(
                    icon = Icons.Filled.Bed,
                    label = "${property.bedrooms} غرف"
                )
            }
            "مكتب" -> {
                SpecItem(
                    icon = Icons.Filled.Elevator,
                    label = "${property.parkingSpaces} مواقف"
                )
                SpecItem(
                    icon = Icons.Filled.Business,
                    label = "برج أعمال"
                )
            }
            else -> {
                // شقة، فيلا، تاون هاوس، بنتهاوس
                if (property.bedrooms > 0) {
                    SpecItem(
                        icon = Icons.Filled.Bed,
                        label = "${property.bedrooms} غرف"
                    )
                }
                if (property.bathrooms > 0) {
                    SpecItem(
                        icon = Icons.Filled.Bathtub,
                        label = "${property.bathrooms} حمامات"
                    )
                }
            }
        }
    }
}

@Composable
fun SpecItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
