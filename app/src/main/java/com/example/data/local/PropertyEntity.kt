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
    val type: String, // فيلا, شقة, أرض, عمارة, مكتب, شاليه
    val purpose: String, // للبيع, للإيجار
    val city: String, // الرياض, جدة, الخبر, الدمام, مكة المكرمة
    val neighborhood: String,
    val area: Int, // بالمتر المربع
    val bedrooms: Int = 0,
    val bathrooms: Int = 0,
    val parkingSpaces: Int = 1,
    val imageResName: String,
    val isFavorite: Boolean = false,
    val agentName: String = "وسيط معتمد",
    val agentPhone: String = "+966501234567",
    val agentLicense: String = "رخصة فال: 1200018492",
    val amenities: String = "تكييف مركزي,موقف سيارات",
    val publisherType: String = "broker", // "broker" (وسيط معتمد), "agency" (مكتب عقارات), "owner" (صاحب العقار)
    val publisherBadge: String = "وسيط معتمد فال", // "من المالك مباشرة", "مكتب عقاري مرخص", "وسيط معتمد فال"
    val typeSpecificDetails: String = "", // e.g. "عرض الشارع: 20م • واجهة شمالية" للأراضي, أو "عائد استثماري: 9%" للعمارة
    val createdAt: Long = System.currentTimeMillis()
)
