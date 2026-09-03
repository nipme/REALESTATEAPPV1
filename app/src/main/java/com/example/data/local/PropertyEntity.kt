package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val price: Long,
    val currency: String = "ر.س",
    val type: String, // فيلا, شقة, بنتهاوس, أرض, تاون هاوس
    val purpose: String, // للبيع, للإيجار
    val city: String, // الرياض, جدة, الخبر, الدمام, مكة المكرمة
    val neighborhood: String,
    val area: Int, // بالمتر المربع
    val bedrooms: Int,
    val bathrooms: Int,
    val parkingSpaces: Int = 1,
    val imageResName: String, // villa_luxury, apartment_luxury, penthouse_living
    val isFavorite: Boolean = false,
    val agentName: String = "عقارات المملكة",
    val agentPhone: String = "+966501234567",
    val agentLicense: String = "رخصة فال: 1200018492",
    val amenities: String = "تكييف مركزي,مطبخ مجهز,أمن وحراسة,موقف سيارات",
    val createdAt: Long = System.currentTimeMillis()
)
