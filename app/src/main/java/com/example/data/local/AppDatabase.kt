package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PropertyEntity::class], version = 1, exportSchema = false)
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
                    PropertyEntity(
                        title = "قصر مودرن فاخر مع مسبح خاص وحديقة",
                        description = "فيلا بتصميم معماري فائق الفخامة ومسبح خارجي متدرج، نظام سمارت هوم متكامل، تكييف دكت مركزي، مصعد بانورامي، تشطيبات فندقية عالمية وموقع استراتيجي هادئ.",
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
                        agentName = "سلطان العتيبي",
                        agentPhone = "+966504433221",
                        agentLicense = "رخصة فال: 1200029381",
                        amenities = "مسبح خاص,مصعد بانورامي,سمارت هوم,حديقة خاصة,غرفة خادمة,غرفة سائق,موقف مغطى"
                    ),
                    PropertyEntity(
                        title = "شقة سكنية بانورامية بإطلالة خلابة",
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
                        isFavorite = false,
                        agentName = "رغد المحمدي",
                        agentPhone = "+966551122334",
                        agentLicense = "رخصة فال: 1100094832",
                        amenities = "إطلالة بحرية,نادي صحي,مسبح مشترك,حراسة 24/7,موقف مخصص,مصعد سريع"
                    ),
                    PropertyEntity(
                        title = "بنتهاوس فاخر بتشطيبات راقية وتراس واسع",
                        description = "بنتهاوس استثنائي في أعلى أدوار البرج يتميز بأسقف مرتفعة وتراس مفتوح بإطلالة 360 درجة، صالة استقبال ملكية ومطبخين داخلي وخارجي.",
                        price = 95000,
                        currency = "ر.س/سنوي",
                        type = "بنتهاوس",
                        purpose = "للإيجار",
                        city = "الخبر",
                        neighborhood = "حي الكورنيش",
                        area = 380,
                        bedrooms = 4,
                        bathrooms = 5,
                        parkingSpaces = 2,
                        imageResName = "penthouse_living",
                        isFavorite = true,
                        agentName = "خالد الدوسري",
                        agentPhone = "+966533344556",
                        agentLicense = "رخصة فال: 1200038472",
                        amenities = "تراس بانورامي,جاكوزي خاص,مجلس ضيوف منفصل,تكييف مركزي,غرفة سينما منزلية"
                    ),
                    PropertyEntity(
                        title = "فيلا درج داخلي بتصميم نيو كلاسيك",
                        description = "فيلا عائلية راقية بتوزيع مساحات عملي ومثالي للعائلات الكبيرة، ملحق خارجي للضيوف، وموقع قريب من كافة المدارس والخدمات الحيوية.",
                        price = 3200000,
                        currency = "ر.س",
                        type = "فيلا",
                        purpose = "للبيع",
                        city = "الرياض",
                        neighborhood = "حي النرجس",
                        area = 450,
                        bedrooms = 5,
                        bathrooms = 6,
                        parkingSpaces = 2,
                        imageResName = "villa_luxury",
                        isFavorite = false,
                        agentName = "عبدالرحمن الشمري",
                        agentPhone = "+966509988776",
                        agentLicense = "رخصة فال: 1200054321",
                        amenities = "ملحق خارجي,حوش واسع,مستودع,غرفة غسيل,مدخل سيارة كهربائي"
                    ),
                    PropertyEntity(
                        title = "شقة مؤثثة بالكامل للإيجار السنوي",
                        description = "شقة جديدة مودرن بأثاث فندقي عصري وراقي، تكييف سبيلت انفيرتر، أجهزة كهربائية متكاملة وجاهزة للسكن الفوري.",
                        price = 68000,
                        currency = "ر.س/سنوي",
                        type = "شقة",
                        purpose = "للإيجار",
                        city = "الرياض",
                        neighborhood = "حي الياسمين",
                        area = 160,
                        bedrooms = 2,
                        bathrooms = 3,
                        parkingSpaces = 1,
                        imageResName = "apartment_luxury",
                        isFavorite = false,
                        agentName = "فيصل الدخيل",
                        agentPhone = "+966567788990",
                        agentLicense = "رخصة فال: 1100067451",
                        amenities = "مؤثثة بالكامل,إنترنت ألياف ضوئية,دخول ذكي,أجهزة كهربائية"
                    ),
                    PropertyEntity(
                        title = "تاون هاوس عصري بإطلالة حديقة خاصة",
                        description = "تاون هاوس بتصميم أنيق على مستويين، خصوصية عالية، روف علوي مجهز لجلسات الشواء وإطلالة مفتوحة على المساحات الخضراء.",
                        price = 1890000,
                        currency = "ر.س",
                        type = "تاون هاوس",
                        purpose = "للبيع",
                        city = "الدمام",
                        neighborhood = "حي الشاطئ الغربي",
                        area = 290,
                        bedrooms = 4,
                        bathrooms = 4,
                        parkingSpaces = 2,
                        imageResName = "penthouse_living",
                        isFavorite = false,
                        agentName = "هند الزهراني",
                        agentPhone = "+966544455667",
                        agentLicense = "رخصة فال: 1200087654",
                        amenities = "حديقة خاصة,روف علوي,مطبخ مفتوح,جلسة خارجية,مستودع"
                    )
                )
                dao.insertAll(initialListings)
            }
        }
    }
}
