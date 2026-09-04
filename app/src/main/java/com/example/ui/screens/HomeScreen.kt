package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.HolidayVillage
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.RealEstateAgent
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PropertyEntity
import com.example.ui.components.PropertyCard
import com.example.ui.theme.TagBrokerCyan
import com.example.ui.theme.TagOfficeIndigo
import com.example.ui.theme.TagOwnerBlue
import com.example.ui.viewmodel.PropertyFilter

@Composable
fun HomeScreen(
    properties: List<PropertyEntity>,
    filter: PropertyFilter,
    activeUserRole: String = "client",
    onSearchChange: (String) -> Unit,
    onSelectType: (String) -> Unit,
    onSelectPurpose: (String) -> Unit,
    onSelectPublisher: (String) -> Unit,
    onOpenFilter: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenAddProperty: () -> Unit,
    onPropertyClick: (PropertyEntity) -> Unit,
    onToggleFavorite: (PropertyEntity) -> Unit,
    onResetFilters: () -> Unit,
    isSyncing: Boolean = false,
    onSyncCloud: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Header Top Bar
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "عقاراتي",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }

                            // Role Indicator Chip
                            val (roleTitle, roleIcon) = when (activeUserRole) {
                                "owner" -> Pair("صاحب العقار (المالك)", Icons.Filled.Badge)
                                "broker" -> Pair("وسيط معتمد (فال)", Icons.Filled.RealEstateAgent)
                                "agency" -> Pair("مكتب عقارات مرخص", Icons.Filled.Apartment)
                                else -> Pair("عميل / باحث عن عقار", Icons.Filled.Person)
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .clickable(onClick = onOpenSettings)
                            ) {
                                Icon(
                                    imageVector = roleIcon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = roleTitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Settings Button (مظهر داكن/عادي، فئات المستخدمين، وقاعدة البيانات)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clickable(onClick = onOpenSettings)
                                    .testTag("open_settings_button")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Settings,
                                        contentDescription = "الإعدادات والمظهر",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Cloud Sync Button
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable(onClick = onSyncCloud)
                                    .testTag("cloud_sync_header_btn")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isSyncing) Icons.Filled.CloudSync else Icons.Filled.CloudDone,
                                        contentDescription = "مزامنة سحابية",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isSyncing) "مزامنة..." else "سحابي",
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Add Property Fast Action
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable(onClick = onOpenAddProperty)
                                    .testTag("open_add_property_header")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "أضف عقار",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "أضف إعلانك",
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search and Filter Bar Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = filter.query,
                            onValueChange = onSearchChange,
                            placeholder = { Text("ابحث بالحي، المدينة، نوع العقار، أو المعلن...", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "بحث",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = {
                                if (filter.query.isNotEmpty()) {
                                    IconButton(
                                        onClick = { onSearchChange("") },
                                        modifier = Modifier.testTag("clear_search_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "مسح",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("search_text_input")
                        )

                        // Filter Sheet Button
                        val hasActiveFilter = filter.city != "الكل" || filter.purpose != "الكل" || filter.type != "الكل" || filter.publisher != "الكل"
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (hasActiveFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .size(50.dp)
                                .clickable(onClick = onOpenFilter)
                                .testTag("filter_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.Tune,
                                    contentDescription = "تصفية",
                                    tint = if (hasActiveFilter) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Purpose Segment Tabs (الكل / للبيع / للإيجار)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("الكل", "للبيع", "للإيجار").forEach { purpose ->
                            val isSelected = filter.purpose == purpose
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onSelectPurpose(purpose) }
                                    .testTag("purpose_tab_$purpose")
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 7.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = purpose,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Publisher Filter Pills (الوسطاء، المكاتب، صاحب العقار)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val publishers = listOf(
                            Pair("الكل", "جميع المعلنين"),
                            Pair("من المالك مباشرة", "من المالك مباشرة"),
                            Pair("مكاتب عقارية", "مكاتب معتمدة"),
                            Pair("وسطاء معتمدون", "وسطاء فال")
                        )
                        publishers.forEach { (key, label) ->
                            val isSelected = filter.publisher == key
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .clickable { onSelectPublisher(key) }
                                    .testTag("publisher_pill_$key")
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Property Type Horizontal Pills Carousel (ينسجم مع نوع العقار)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val categories = listOf(
                            CategoryItem("الكل", Icons.Filled.Home),
                            CategoryItem("فيلا", Icons.Filled.HolidayVillage),
                            CategoryItem("شقة", Icons.Filled.Apartment),
                            CategoryItem("أرض", Icons.Filled.Landscape),
                            CategoryItem("عمارة", Icons.Filled.Business),
                            CategoryItem("مكتب", Icons.Filled.Business),
                            CategoryItem("شاليه", Icons.Filled.Pool)
                        )

                        categories.forEach { item ->
                            val isSelected = filter.type == item.name
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable { onSelectType(item.name) }
                                    .testTag("type_pill_${item.name}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = item.name,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Results Counter & Active Filters indicator
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "العقارات المتاحة (${properties.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    if (filter.city != "الكل" || filter.type != "الكل" || filter.purpose != "الكل" || filter.publisher != "الكل" || filter.query.isNotBlank()) {
                        Text(
                            text = "مسح الفلاتر",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable(onClick = onResetFilters)
                                .testTag("reset_filters_label")
                        )
                    }
                }
            }

            // Empty State
            if (properties.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.SearchOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "لم يتم العثور على عقارات مطابقة",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "جرب تغيير كلمات البحث أو إعادة تعيين الفلاتر",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onResetFilters,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("reset_search_empty_button")
                        ) {
                            Text("إعادة ضبط البحث")
                        }
                    }
                }
            } else {
                // Property Items List
                items(properties, key = { it.id }) { property ->
                    PropertyCard(
                        property = property,
                        onClick = { onPropertyClick(property) },
                        onToggleFavorite = { onToggleFavorite(property) },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp)
                    )
                }
            }
        }

        // Floating Action Button to Add Property
        FloatingActionButton(
            onClick = onOpenAddProperty,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("add_property_fab")
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "أضف عقار")
        }
    }
}

data class CategoryItem(
    val name: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
