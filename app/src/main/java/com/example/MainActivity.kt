package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddPropertySheet
import com.example.ui.components.FilterSheet
import com.example.ui.components.PropertyDetailSheet
import com.example.ui.screens.CalculatorScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.DeepViolet
import com.example.ui.theme.ElegantDarkNav
import com.example.ui.theme.ElegantDarkPrimary
import com.example.ui.theme.ElegantDarkSurfaceVariant
import com.example.ui.theme.ElegantDarkTextSecondary
import com.example.ui.theme.FavoriteRed
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Navy900
import com.example.ui.theme.Slate600
import com.example.ui.viewmodel.RealEstateViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: RealEstateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    RealEstateApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun RealEstateApp(viewModel: RealEstateViewModel) {
    val filteredProperties by viewModel.filteredProperties.collectAsStateWithLifecycle()
    val favoriteProperties by viewModel.favoriteProperties.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()
    val selectedProperty by viewModel.selectedProperty.collectAsStateWithLifecycle()
    val mortgageState by viewModel.mortgageState.collectAsStateWithLifecycle()

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var isFilterSheetOpen by remember { mutableStateOf(false) }
    var isAddPropertyOpen by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = ElegantDarkNav,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "الرئيسية"
                        )
                    },
                    label = {
                        Text(
                            text = "استكشاف",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElegantDarkPrimary,
                        selectedTextColor = ElegantDarkPrimary,
                        indicatorColor = ElegantDarkSurfaceVariant,
                        unselectedIconColor = ElegantDarkTextSecondary,
                        unselectedTextColor = ElegantDarkTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_explore")
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (favoriteProperties.isNotEmpty()) {
                                    Badge(
                                        containerColor = FavoriteRed,
                                        contentColor = DeepViolet
                                    ) {
                                        Text("${favoriteProperties.size}")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (selectedTab == 1) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "المفضلة"
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "المفضلة",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElegantDarkPrimary,
                        selectedTextColor = ElegantDarkPrimary,
                        indicatorColor = ElegantDarkSurfaceVariant,
                        unselectedIconColor = ElegantDarkTextSecondary,
                        unselectedTextColor = ElegantDarkTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_favorites")
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.Calculate else Icons.Outlined.Calculate,
                            contentDescription = "حاسبة التمويل"
                        )
                    },
                    label = {
                        Text(
                            text = "حاسبة التمويل",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ElegantDarkPrimary,
                        selectedTextColor = ElegantDarkPrimary,
                        indicatorColor = ElegantDarkSurfaceVariant,
                        unselectedIconColor = ElegantDarkTextSecondary,
                        unselectedTextColor = ElegantDarkTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_calculator")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                label = "ScreenTransition"
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> HomeScreen(
                        properties = filteredProperties,
                        filter = filter,
                        onSearchChange = { viewModel.updateSearchQuery(it) },
                        onSelectType = { viewModel.setFilterType(it) },
                        onSelectPurpose = { viewModel.setFilterPurpose(it) },
                        onOpenFilter = { isFilterSheetOpen = true },
                        onOpenAddProperty = { isAddPropertyOpen = true },
                        onPropertyClick = { viewModel.selectProperty(it) },
                        onToggleFavorite = { property ->
                            viewModel.toggleFavorite(property)
                            scope.launch {
                                val msg = if (!property.isFavorite) "تمت إضافة العقار للمفضلة" else "تمت إزالة العقار من المفضلة"
                                snackbarHostState.showSnackbar(msg)
                            }
                        },
                        onResetFilters = { viewModel.resetFilters() }
                    )

                    1 -> FavoritesScreen(
                        favorites = favoriteProperties,
                        onPropertyClick = { viewModel.selectProperty(it) },
                        onToggleFavorite = { property ->
                            viewModel.toggleFavorite(property)
                            scope.launch {
                                snackbarHostState.showSnackbar("تمت إزالة العقار من المفضلة")
                            }
                        },
                        onNavigateToExplore = { selectedTab = 0 }
                    )

                    2 -> CalculatorScreen(
                        mortgage = mortgageState,
                        onPriceChange = { viewModel.updateMortgagePrice(it) },
                        onDownPaymentChange = { viewModel.updateMortgageDownPayment(it) },
                        onYearsChange = { viewModel.updateMortgageYears(it) },
                        onRateChange = { viewModel.updateMortgageRate(it) }
                    )
                }
            }

            // Property Detail Modal Sheet
            selectedProperty?.let { property ->
                PropertyDetailSheet(
                    property = property,
                    onDismiss = { viewModel.selectProperty(null) },
                    onToggleFavorite = {
                        viewModel.toggleFavorite(property)
                        scope.launch {
                            val msg = if (!property.isFavorite) "تمت إضافة العقار للمفضلة" else "تمت إزالة العقار من المفضلة"
                            snackbarHostState.showSnackbar(msg)
                        }
                    },
                    onNavigateToCalculator = {
                        viewModel.setMortgageForProperty(property)
                        selectedTab = 2
                    }
                )
            }

            // Filter Bottom Sheet
            if (isFilterSheetOpen) {
                FilterSheet(
                    currentFilter = filter,
                    onDismiss = { isFilterSheetOpen = false },
                    onApplyType = { viewModel.setFilterType(it) },
                    onApplyPurpose = { viewModel.setFilterPurpose(it) },
                    onApplyCity = { viewModel.setFilterCity(it) },
                    onReset = {
                        viewModel.resetFilters()
                        isFilterSheetOpen = false
                    }
                )
            }

            // Add Property Sheet
            if (isAddPropertyOpen) {
                AddPropertySheet(
                    onDismiss = { isAddPropertyOpen = false },
                    onSaveProperty = { newProperty ->
                        viewModel.addProperty(newProperty)
                        isAddPropertyOpen = false
                        scope.launch {
                            snackbarHostState.showSnackbar("تم نشر إعلانك العقاري بنجاح!")
                        }
                    }
                )
            }
        }
    }
}

// Keep Greeting composable for tests
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
