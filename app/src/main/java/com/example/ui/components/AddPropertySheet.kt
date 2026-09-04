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
    var selectedType by remember { mutableStateOf("فيلا") }
    var selectedPurpose by remember { mutableStateOf("للبيع") }
    var selectedPublisherRole by remember { mutableStateOf("owner") } // owner, broker, agency
    var selectedCity by remember { mutableStateOf("الرياض") }
    var neighborhood by remember { mutableStateOf("") }
    var areaStr by remember { mutableStateOf("") }
    var bedroomsStr by remember { mutableStateOf("4") }
    var bathroomsStr by remember { mutableStateOf("4") }
    var typeDetails by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var publisherName by remember { mutableStateOf("صاحب العقار") }
    var phone by remember { mutableStateOf("+966501234567") }
    var showError by remember { mutableStateOf(false) }

    val propertyTypes = listOf("فيلا", "شقة", "أرض", "عمارة", "مكتب", "شاليه")
    val purposeList = listOf("للبيع", "للإيجار")
    val cities = listOf("الرياض", "جدة", "الخبر", "الدمام", "مكة المكرمة", "المدينة المنورة")
    val publisherRoles = listOf(
        Triple("owner", "صاحب العقار", "من المالك مباشرة"),
        Triple("broker", "وسيط عقاري", "وسيط معتمد فال"),
        Triple("agency", "مكتب عقارات", "مكتب عقاري معتمد")
    )

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor = MaterialTheme.colorScheme.primary
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("add_property_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
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

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "إلغاء",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. صفة المعلن (صاحب العقار، وسيط، مكتب)
            Text(
                text = "صفة ناشر الإعلان (المستخدم):",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                publisherRoles.forEach { (roleKey, roleLabel, _) ->
                    FilterChip(
                        selected = selectedPublisherRole == roleKey,
                        onClick = {
                            selectedPublisherRole = roleKey
                            publisherName = if (roleKey == "owner") "المالك مباشرة" else if (roleKey == "broker") "وسيط فال معتمد" else "مكتب عقارات مرخص"
                        },
                        label = { Text(roleLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. نوع العقار
            Text(
                text = "نوع العقار:",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                propertyTypes.forEach { type ->
                    FilterChip(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        label = { Text(type, fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. الغرض من العقار (بيع / إيجار)
            Text(
                text = "الغرض:",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                purposeList.forEach { purpose ->
                    FilterChip(
                        selected = selectedPurpose == purpose,
                        onClick = { selectedPurpose = purpose },
                        label = { Text(purpose, fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("عنوان الإعلان (مثال: فيلا مودرن للبيع)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("property_title_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Price & Area Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = priceStr,
                    onValueChange = { priceStr = it },
                    label = { Text("السعر (ر.س)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("property_price_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = areaStr,
                    onValueChange = { areaStr = it },
                    label = { Text("المساحة (م²)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("property_area_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = textFieldColors
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // City & Neighborhood Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = { selectedCity = it },
                    label = { Text("المدينة") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = neighborhood,
                    onValueChange = { neighborhood = it },
                    label = { Text("الحي") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("property_neighborhood_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = textFieldColors
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Conditional inputs based on property type
            if (selectedType != "أرض") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = bedroomsStr,
                        onValueChange = { bedroomsStr = it },
                        label = { Text(if (selectedType == "عمارة") "الوحدات" else "الغرف") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = textFieldColors
                    )

                    OutlinedTextField(
                        value = bathroomsStr,
                        onValueChange = { bathroomsStr = it },
                        label = { Text("دورات المياه") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = textFieldColors
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Type-specific details (عرض الشارع، الواجهة، العائد)
            OutlinedTextField(
                value = typeDetails,
                onValueChange = { typeDetails = it },
                label = {
                    Text(
                        when (selectedType) {
                            "أرض" -> "تفاصيل الأرض (عرض الشارع، الواجهة، طبيعة الأرض)"
                            "عمارة" -> "تفاصيل العمارة (العائد المتوقع، عدد المعارض)"
                            "مكتب" -> "تفاصيل المكتب (الدور، التكييف، عدد المواقف)"
                            "شاليه" -> "تفاصيل الشاليه (المسبح، المسطح الأخضر)"
                            else -> "تفاصيل إضافية (الدور، الإطلالة، المسبح، الخ)"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Publisher Name & Phone
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = publisherName,
                    onValueChange = { publisherName = it },
                    label = { Text("اسم المعلن") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم الجوال") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = textFieldColors
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("وصف العقار ومميزاته") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .testTag("property_desc_input"),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3,
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
                    val beds = bedroomsStr.toIntOrNull() ?: 0
                    val baths = bathroomsStr.toIntOrNull() ?: 0

                    if (title.isBlank() || price == null || neighborhood.isBlank()) {
                        showError = true
                    } else {
                        val imageChosen = when (selectedType) {
                            "فيلا" -> "villa_luxury"
                            "شقة" -> "apartment_luxury"
                            else -> "penthouse_living"
                        }
                        val currency = if (selectedPurpose == "للإيجار") "ر.س/سنوي" else "ر.س"

                        val badgeText = when (selectedPublisherRole) {
                            "owner" -> "من المالك مباشرة"
                            "agency" -> "مكتب عقاري معتمد"
                            else -> "وسيط معتمد فال"
                        }

                        val licenseText = when (selectedPublisherRole) {
                            "owner" -> "إعلان موثق من المالك مباشرة"
                            "agency" -> "سجل تجاري وترخيص منشأة معتمد"
                            else -> "وسيط مرخص برخصة فال العقارية"
                        }

                        val newProperty = PropertyEntity(
                            title = title,
                            description = if (description.isNotBlank()) description else "عقار مميز بموقع استراتيجي متكامل الخدمات والمرافق.",
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
                            agentName = publisherName,
                            agentPhone = phone,
                            agentLicense = licenseText,
                            amenities = "موقف خاص,تكييف,مدخل خاص",
                            publisherType = selectedPublisherRole,
                            publisherBadge = badgeText,
                            typeSpecificDetails = typeDetails
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
