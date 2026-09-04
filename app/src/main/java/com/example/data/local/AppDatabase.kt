package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PropertyEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aqaraty_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.propertyDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: PropertyDao) {
                val initialListings = listOf(
                    // 1. فيلا فاخرة - وسيط معتمد
                    PropertyEntity(
                        title = "قصر مودرن فاخر مع مسبح خاص وحديقة",
                        description = "فيلا بتصميم معماري فائق الفخامة ومسبح خارجي متدرج، نظام سمارت هوم متكامل، تكييف دكت مركزي، مصعد بانورامي، تشطيبات فندقية وموقع استراتيجي هادئ.",
                        price = 4850000,
                        currency = "ر.س",
                        type = "فيلا",
                        purpose = "للبيع",
                        city = "الرياض",
                        neighborhood = "حي الملقا",
                        area = 620,
                        bedrooms = 6,
                        bathrooms = 7,
                        parkingSpaces = 3,
                        imageResName = "villa_luxury",
                        isFavorite = true,
                        agentName = "سلطان العتيبي (وسيط عقاري)",
                        agentPhone = "+966504433221",
                        agentLicense = "رخصة فال: 1200029381",
                        amenities = "مسبح خاص,مصعد بانورامي,سمارت هوم,حديقة خاصة,غرفة خادمة,غرفة سائق",
                        publisherType = "broker",
                        publisherBadge = "وسيط معتمد فال",
                        typeSpecificDetails = "أدوار: 3 • مسبح خاص • مجلسين ضيافة • غرفة سائق وخادمة"
                    ),
                    // 2. أرض سكنية استثمارية - من المالك مباشرة (صاحب العقار)
                    PropertyEntity(
                        title = "أرض سكنية زاوية على شارعين بصك إلكتروني",
                        description = "أرض مميزة للبيع مباشرة من صاحب العقار دون أي سعي أو عمولة، موقع مرتفع ومستوي وجاهز للبناء الفوري بالقرب من طريق الملك سلمان.",
                        price = 2150000,
                        currency = "ر.س",
                        type = "أرض",
                        purpose = "للبيع",
                        city = "الرياض",
                        neighborhood = "حي النرجس",
                        area = 500,
                        bedrooms = 0,
                        bathrooms = 0,
                        parkingSpaces = 0,
                        imageResName = "villa_luxury",
                        isFavorite = false,
                        agentName = "عبدالرحمن المالك (صاحب العقار)",
                        agentPhone = "+966501199884",
                        agentLicense = "صك إلكتروني موثق من المالك مباشرة",
                        amenities = "صك إلكتروني,شارع 20م,واجهة شرقية,مكتملة الخدمات,مستوية وجاهزة",
                        publisherType = "owner",
                        publisherBadge = "من المالك مباشرة",
                        typeSpecificDetails = "عرض الشارع: 20م • واجهة شمالية شرقية • سعر المتر: 4,300 ر.س"
                    ),
                    // 3. شقة فندقية فاخرة - مكتب عقارات
                    PropertyEntity(
                        title = "شقة سكنية بانورامية بإطلالة خلابة على البحر",
                        description = "شقة فسيحة في برج سكني راقٍ بواجهات زجاجية واسعة، تشطيبات رخامية إيطالية، مطبخ ألماني مجهز، وخدمات أمنية وإدارة مرافق على مدار الساعة.",
                        price = 1420000,
                        currency = "ر.س",
                        type = "شقة",
                        purpose = "للبيع",
                        city = "جدة",
                        neighborhood = "حي الشاطئ",
                        area = 240,
                        bedrooms = 3,
                        bathrooms = 4,
                        parkingSpaces = 2,
                        imageResName = "apartment_luxury",
                        isFavorite = true,
                        agentName = "شركة آفاق العقارية (مكتب معتمد)",
                        agentPhone = "+966551122334",
                        agentLicense = "سجل تجاري: 1010892341 | فال منشآت: 2100094832",
                        amenities = "إطلالة بحرية,نادي صحي,مسبح مشترك,حراسة 24/7,موقف مخصص,مصعد سريع",
                        publisherType = "agency",
                        publisherBadge = "مكتب عقاري معتمد",
                        typeSpecificDetails = "الدور: 14 • مصعد بانورامي • موقفين قبو • إطلالة بحرية كاملة"
                    ),
                    // 4. عمارة تجارية استثمارية - مكتب عقارات
                    PropertyEntity(
                        title = "عمارة تجارية استثمارية مؤجرة بعائد سنوي ممتاز",
                        description = "عمارة تجارية سكنية حديثة البناء تتكون من 4 محلات تجارية و12 شقة فاخرة، جميعها مؤجرة بعقود إلكترونية موثقة عبر منصة إيجار بعائد 9.2%.",
                        price = 6500000,
                        currency = "ر.س",
                        type = "عمارة",
                        purpose = "للبيع",
                        city = "الخبر",
                        neighborhood = "حي العليا",
                        area = 850,
                        bedrooms = 12,
                        bathrooms = 14,
                        parkingSpaces = 8,
                        imageResName = "penthouse_living",
                        isFavorite = false,
                        agentName = "مكتب الصرح العقاري المعتمد",
                        agentPhone = "+966533344556",
                        agentLicense = "سجل منشأة: 2050123984 | رخصة فال: 2200038472",
                        amenities = "عقود إيجار موثقة,مصعد ميتسوبيشي,دفاع مدني معتمد,عدادات مستقلة",
                        publisherType = "agency",
                        publisherBadge = "مكتب عقاري معتمد",
                        typeSpecificDetails = "عائد سنوي: 9.2% • 4 معارض تجارية • 12 شقة • دخل سنوي: 600,000 ر.س"
                    ),
                    // 5. شاليه ومصيف عائلي - صاحب العقار
                    PropertyEntity(
                        title = "شاليه ريفي فاخر مع مسبح وجلسات خارجية",
                        description = "شاليه عائلي للإيجار مجهز بأحدث الديكورات الريفية، مسطحات خضراء طبيعية ومسبح مع ألعاب مائية ومجلس ضيافة كبير مجهز بالكامل.",
                        price = 1200,
                        currency = "ر.س/يومي",
                        type = "شاليه",
                        purpose = "للإيجار",
                        city = "الرياض",
                        neighborhood = "حي الرمال",
                        area = 450,
                        bedrooms = 2,
                        bathrooms = 3,
                        parkingSpaces = 2,
                        imageResName = "villa_luxury",
                        isFavorite = false,
                        agentName = "أبو سعود (صاحب الشاليه)",
                        agentPhone = "+966567788990",
                        agentLicense = "ترخيص سياحي وصك ملكية معتمد",
                        amenities = "مسبح أطفال وكبار,مسطح أخضر,شواية باربكيو,ألعاب أطفال,مجلس خارجي",
                        publisherType = "owner",
                        publisherBadge = "من المالك مباشرة",
                        typeSpecificDetails = "مسبح مع سخان • مسطح أخضر 200م² • قسمين عائلي وضيافة"
                    ),
                    // 6. مكتب تجاري إداري - وسيط معتمد
                    PropertyEntity(
                        title = "مكتب إداري فاخر في برج أعمال تجاري مرموق",
                        description = "مكتب عمل بتشطيبات راقية ونظام تحكم مركزي، جاهز للتشغيل المباشر للشركات والكيانات الاستشارية بالقرب من مركز الملك عبدالله المالي (KAFD).",
                        price = 110000,
                        currency = "ر.س/سنوي",
                        type = "مكتب",
                        purpose = "للإيجار",
                        city = "الرياض",
                        neighborhood = "طريق الملك فهد",
                        area = 180,
                        bedrooms = 0,
                        bathrooms = 2,
                        parkingSpaces = 3,
                        imageResName = "penthouse_living",
                        isFavorite = false,
                        agentName = "نايف الغامدي (وسيط عقاري)",
                        agentPhone = "+966544455667",
                        agentLicense = "رخصة فال: 1200087654",
                        amenities = "ألياف ضوئية,مواقف قبو مخصصة,استقبال وأمن 24/7,قاعة اجتماعات مشتركة",
                        publisherType = "broker",
                        publisherBadge = "وسيط معتمد فال",
                        typeSpecificDetails = "قريب من KAFD • تشطيب كامل • قاعة اجتماعات • 3 مواقف قبو"
                    )
                )
                dao.insertAll(initialListings)
            }
        }
    }
}
