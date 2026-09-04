package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.PropertyEntity
import com.example.data.remote.FirestoreService
import com.example.data.repository.PropertyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.pow

data class PropertyFilter(
    val query: String = "",
    val type: String = "الكل",
    val purpose: String = "الكل",
    val city: String = "الكل",
    val publisher: String = "الكل", // الكل, من المالك مباشرة, مكاتب عقارية, وسطاء معتمدون
    val maxPrice: Long = Long.MAX_VALUE,
    val minBedrooms: Int = 0
)

data class MortgageCalculation(
    val propertyPrice: Double = 2500000.0,
    val downPaymentPercent: Double = 15.0,
    val loanYears: Int = 20,
    val annualRatePercent: Double = 4.5
) {
    val downPaymentAmount: Double
        get() = propertyPrice * (downPaymentPercent / 100.0)

    val loanAmount: Double
        get() = propertyPrice - downPaymentAmount

    val monthlyInstallment: Double
        get() {
            if (loanAmount <= 0) return 0.0
            val monthlyRate = (annualRatePercent / 100.0) / 12.0
            val totalMonths = loanYears * 12
            if (monthlyRate <= 0.0) return loanAmount / totalMonths
            val factor = (1 + monthlyRate).pow(totalMonths.toDouble())
            return (loanAmount * monthlyRate * factor) / (factor - 1)
        }

    val totalPayment: Double
        get() = (monthlyInstallment * loanYears * 12) + downPaymentAmount

    val totalProfitAmount: Double
        get() = (monthlyInstallment * loanYears * 12) - loanAmount
}

class RealEstateViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PropertyRepository

    // App Theme State: Dark Mode vs Light (Normal) Mode
    private val _isDarkTheme = MutableStateFlow(true)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // Active User Role State
    // "client" (عميل/مشتري), "broker" (وسيط عقاري فال), "agency" (مكتب عقارات مرخص), "owner" (صاحب العقار)
    private val _activeUserRole = MutableStateFlow("client")
    val activeUserRole: StateFlow<String> = _activeUserRole.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    val isCloudConfigured: Boolean
        get() = repository.firestoreService?.isConfigured == true

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        val firestoreService = FirestoreService(application)
        repository = PropertyRepository(database.propertyDao(), firestoreService)
        viewModelScope.launch {
            repository.checkAndSeedIfEmpty()
            syncWithCloud()
        }
    }

    private val _filter = MutableStateFlow(PropertyFilter())
    val filter: StateFlow<PropertyFilter> = _filter.asStateFlow()

    val allProperties: StateFlow<List<PropertyEntity>> = repository.allProperties
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteProperties: StateFlow<List<PropertyEntity>> = repository.favoriteProperties
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredProperties: StateFlow<List<PropertyEntity>> =
        combine(allProperties, _filter) { properties, filter ->
            properties.filter { property ->
                val matchesQuery = filter.query.isBlank() ||
                        property.title.contains(filter.query, ignoreCase = true) ||
                        property.neighborhood.contains(filter.query, ignoreCase = true) ||
                        property.city.contains(filter.query, ignoreCase = true) ||
                        property.type.contains(filter.query, ignoreCase = true) ||
                        property.agentName.contains(filter.query, ignoreCase = true) ||
                        property.publisherBadge.contains(filter.query, ignoreCase = true)

                val matchesType = filter.type == "الكل" || property.type == filter.type
                val matchesPurpose = filter.purpose == "الكل" || property.purpose == filter.purpose
                val matchesCity = filter.city == "الكل" || property.city == filter.city
                val matchesPrice = property.price <= filter.maxPrice
                val matchesBedrooms = property.bedrooms >= filter.minBedrooms

                val matchesPublisher = when (filter.publisher) {
                    "من المالك مباشرة" -> property.publisherType == "owner"
                    "مكاتب عقارية" -> property.publisherType == "agency"
                    "وسطاء معتمدون" -> property.publisherType == "broker"
                    else -> true
                }

                matchesQuery && matchesType && matchesPurpose && matchesCity && matchesPrice && matchesBedrooms && matchesPublisher
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Property for detail view
    private val _selectedProperty = MutableStateFlow<PropertyEntity?>(null)
    val selectedProperty: StateFlow<PropertyEntity?> = _selectedProperty.asStateFlow()

    // Mortgage calculator state
    private val _mortgageState = MutableStateFlow(MortgageCalculation())
    val mortgageState: StateFlow<MortgageCalculation> = _mortgageState.asStateFlow()

    fun setDarkTheme(enabled: Boolean) {
        _isDarkTheme.value = enabled
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun setActiveUserRole(role: String) {
        _activeUserRole.value = role
    }

    fun updateSearchQuery(query: String) {
        _filter.value = _filter.value.copy(query = query)
    }

    fun setFilterType(type: String) {
        _filter.value = _filter.value.copy(type = type)
    }

    fun setFilterPurpose(purpose: String) {
        _filter.value = _filter.value.copy(purpose = purpose)
    }

    fun setFilterCity(city: String) {
        _filter.value = _filter.value.copy(city = city)
    }

    fun setFilterPublisher(publisher: String) {
        _filter.value = _filter.value.copy(publisher = publisher)
    }

    fun resetFilters() {
        _filter.value = PropertyFilter()
    }

    fun selectProperty(property: PropertyEntity?) {
        _selectedProperty.value = property
    }

    fun toggleFavorite(property: PropertyEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(property.id, property.isFavorite)
            if (_selectedProperty.value?.id == property.id) {
                _selectedProperty.value = _selectedProperty.value?.copy(isFavorite = !property.isFavorite)
            }
        }
    }

    fun addProperty(property: PropertyEntity) {
        viewModelScope.launch {
            repository.insertProperty(property)
        }
    }

    fun deleteProperty(propertyId: Long) {
        viewModelScope.launch {
            repository.deleteProperty(propertyId)
            if (_selectedProperty.value?.id == propertyId) {
                _selectedProperty.value = null
            }
        }
    }

    fun updateMortgagePrice(price: Double) {
        _mortgageState.value = _mortgageState.value.copy(propertyPrice = price)
    }

    fun updateMortgageDownPayment(percent: Double) {
        _mortgageState.value = _mortgageState.value.copy(downPaymentPercent = percent)
    }

    fun updateMortgageYears(years: Int) {
        _mortgageState.value = _mortgageState.value.copy(loanYears = years)
    }

    fun updateMortgageRate(rate: Double) {
        _mortgageState.value = _mortgageState.value.copy(annualRatePercent = rate)
    }

    fun setMortgageForProperty(property: PropertyEntity) {
        _mortgageState.value = _mortgageState.value.copy(propertyPrice = property.price.toDouble())
    }

    fun syncWithCloud() {
        viewModelScope.launch {
            _isSyncing.value = true
            try {
                repository.syncWithCloud()
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun resetToDefaultProperties() {
        viewModelScope.launch {
            repository.resetToDefaultSeed()
        }
    }
}
