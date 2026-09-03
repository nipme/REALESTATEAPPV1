package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.PropertyEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPropertySheet(
    onDismiss: () -> Unit,
    onSaveProperty: (PropertyEntity) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("شقة") }
    var selectedPurpose by remember { mutableStateOf("للبيع") }
    var selectedCity by remember { mutableStateOf("الرياض") }
    var neighborhood by remember { mutableStateOf("") }
    var areaStr by remember { mutableStateOf("") }
    var bedroomsStr by remember { mutableStateOf("3") }
    var bathroomsStr by remember { mutableStateOf("2") }
    var description by remember { mutableStateOf("") }

    val types = listOf("فيلا", "شقة", "بنتهاوس", "تاون هاوس")
    val purposes = listOf("للبيع", "للإيجار")
    val cities = listOf("الرياض", "جدة", "الخبر", "الدمام", "مكة المكرمة")

    var showError by remember { mutableStateOf(false) }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("add_property_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.AddHome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "إضافة عقار جديد",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_add_property_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "إلغاء",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Purpose Selection (للبيع / للإيجار)
            Text(
                "الغرض من الإعلان",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                purposes.forEach { purpose ->
                    FilterChip(
                        selected = selectedPurpose == purpose,
                        onClick = { selectedPurpose = purpose },
                        label = { Text(purpose, fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.testTag("purpose_chip_$purpose")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Type Selection
            Text(
                "نوع العقار",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                types.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.testTag("type_chip_$type")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("عنوان الإعلان (مثال: فيلا فاخرة بحي الملقا)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("property_title_input"),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Price Field
            OutlinedTextField(
                value = priceStr,
                onValueChange = { priceStr = it },
                label = { Text("السعر الإجمالي (ر.س)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("property_price_input"),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(14.dp))

            // City Selection
            Text(
                "المدينة",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                cities.take(4).forEach { city ->
                    FilterChip(
                        selected = selectedCity == city,
                        onClick = { selectedCity = city },
                        label = { Text(city, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.testTag("city_chip_$city")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Neighborhood
            OutlinedTextField(
                value = neighborhood,
                onValueChange = { neighborhood = it },
                label = { Text("اسم الحي (مثال: حي الياسمين)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("property_neighborhood_input"),
                shape = RoundedCornerShape(12.dp),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Specifications: Area, Bedrooms, Bathrooms
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = areaStr,
                    onValueChange = { areaStr = it },
                    label = { Text("المساحة (م²)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("property_area_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = bedroomsStr,
                    onValueChange = { bedroomsStr = it },
                    label = { Text("الغرف") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("property_bedrooms_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = bathroomsStr,
                    onValueChange = { bathroomsStr = it },
                    label = { Text("الحمامات") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("property_bathrooms_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("وصف العقار والمميزات الإضافية") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .testTag("property_desc_input"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4,
                colors = textFieldColors
            )

            if (showError) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "يرجى تعبئة الحقول الأساسية (العنوان، السعر، الحي والمساحة)",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    val price = priceStr.toLongOrNull()
                    val area = areaStr.toIntOrNull() ?: 200
                    val beds = bedroomsStr.toIntOrNull() ?: 3
                    val baths = bathroomsStr.toIntOrNull() ?: 2

                    if (title.isBlank() || price == null || neighborhood.isBlank()) {
                        showError = true
                    } else {
                        val imageChosen = when (selectedType) {
                            "فيلا" -> "villa_luxury"
                            "شقة" -> "apartment_luxury"
                            else -> "penthouse_living"
                        }
                        val currency = if (selectedPurpose == "للإيجار") "ر.س/سنوي" else "ر.س"

                        val newProperty = PropertyEntity(
                            title = title,
                            description = if (description.isNotBlank()) description else "عقار استثنائي في موقع مميز يتوفر على كافة الخدمات الأساسية والمرافق الحيوية.",
                            price = price,
                            currency = currency,
                            type = selectedType,
                            purpose = selectedPurpose,
                            city = selectedCity,
                            neighborhood = neighborhood,
                            area = area,
                            bedrooms = beds,
                            bathrooms = baths,
                            imageResName = imageChosen,
                            isFavorite = false,
                            agentName = "المالك مباشرة",
                            agentPhone = "+966500000000",
                            agentLicense = "إعلان مباشر موثق",
                            amenities = "موقف خاص,تكييف,مدخل خاص"
                        )
                        onSaveProperty(newProperty)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_property_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "نشر الإعلان الآن",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
