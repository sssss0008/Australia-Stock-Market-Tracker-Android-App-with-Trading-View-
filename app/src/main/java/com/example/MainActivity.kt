package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AsxStock
import com.example.model.MarketRepository
import com.example.ui.screens.ChartScreen
import com.example.ui.screens.CompanyScreen
import com.example.ui.screens.EconomicsScreen
import com.example.ui.screens.HeatmapScreen
import com.example.ui.screens.MarketsScreen
import com.example.ui.screens.ScreenerScreen
import com.example.ui.theme.AusGold
import com.example.ui.theme.AusGreen
import com.example.ui.theme.AusNavyDark
import com.example.ui.theme.AustraliaMarketTheme

enum class AppTab(
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
  val testTag: String
) {
  MARKETS("Markets", Icons.Filled.Storefront, Icons.Outlined.Storefront, "tab_markets"),
  CHART("Chart", Icons.Filled.ShowChart, Icons.Outlined.ShowChart, "tab_chart"),
  HEATMAP("Heatmap", Icons.Filled.GridOn, Icons.Outlined.GridOn, "tab_heatmap"),
  SCREENER("Screener", Icons.Filled.FilterAlt, Icons.Outlined.FilterAlt, "tab_screener"),
  COMPANY("Company", Icons.Filled.Analytics, Icons.Outlined.Analytics, "tab_company")
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      AustraliaMarketApp()
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AustraliaMarketApp() {
  var isDarkTheme by remember { mutableStateOf(true) }
  var selectedTab by remember { mutableStateOf(AppTab.MARKETS) }
  var showEconomicsModal by remember { mutableStateOf(false) }
  var showSearchModal by remember { mutableStateOf(false) }
  var currentStock by remember { mutableStateOf(MarketRepository.topAsxStocks.first()) }
  var chartSymbol by remember { mutableStateOf("ASX:XJO") }
  var companySymbol by remember { mutableStateOf("ASX:BHP") }

  AustraliaMarketTheme(darkTheme = isDarkTheme) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        CenterAlignedTopAppBar(
          title = {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                painter = painterResource(id = R.drawable.ic_australia_market),
                contentDescription = "Australia Market Tracker Logo",
                modifier = Modifier.size(30.dp),
                tint = Color.Unspecified
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                  text = "Australia Market Tracker",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "ASX • TradingView Live",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          },
          actions = {
            // Quick search
            IconButton(
              onClick = { showSearchModal = true },
              modifier = Modifier.testTag("btn_top_search")
            ) {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Stock",
                tint = MaterialTheme.colorScheme.onSurface
              )
            }

            // Macro / RBA Calendar toggle
            IconButton(
              onClick = { showEconomicsModal = !showEconomicsModal },
              modifier = Modifier.testTag("btn_top_economics")
            ) {
              Icon(
                imageVector = Icons.Default.EventNote,
                contentDescription = "Economic Calendar",
                tint = if (showEconomicsModal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
              )
            }

            // Dark / Light Theme Toggle
            IconButton(
              onClick = { isDarkTheme = !isDarkTheme },
              modifier = Modifier.testTag("btn_theme_toggle")
            ) {
              Icon(
                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = "Toggle Theme",
                tint = AusGold
              )
            }
          },
          colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
          )
        )
      },
      bottomBar = {
        NavigationBar(
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
          tonalElevation = 6.dp,
          modifier = Modifier
            .navigationBarsPadding()
            .testTag("bottom_navigation_bar")
        ) {
          AppTab.values().forEach { tab ->
            val isSelected = selectedTab == tab && !showEconomicsModal
            NavigationBarItem(
              selected = isSelected,
              onClick = {
                showEconomicsModal = false
                selectedTab = tab
              },
              icon = {
                Icon(
                  imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                  contentDescription = tab.title
                )
              },
              label = {
                Text(
                  text = tab.title,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
              ),
              modifier = Modifier.testTag(tab.testTag)
            )
          }
        }
      }
    ) { innerPadding ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
      ) {
        if (showEconomicsModal) {
          EconomicsScreen(
            isDark = isDarkTheme,
            modifier = Modifier.fillMaxSize()
          )
        } else {
          Crossfade(
            targetState = selectedTab,
            label = "tab_crossfade"
          ) { tab ->
            when (tab) {
              AppTab.MARKETS -> {
                MarketsScreen(
                  isDark = isDarkTheme,
                  selectedStock = currentStock,
                  onSelectStock = { stock ->
                    currentStock = stock
                    companySymbol = stock.symbol
                  },
                  onNavigateToChart = { symbol ->
                    chartSymbol = symbol
                    selectedTab = AppTab.CHART
                  },
                  onNavigateToCompany = { symbol ->
                    companySymbol = symbol
                    selectedTab = AppTab.COMPANY
                  }
                )
              }

              AppTab.CHART -> {
                ChartScreen(
                  initialSymbol = chartSymbol,
                  isDark = isDarkTheme
                )
              }

              AppTab.HEATMAP -> {
                HeatmapScreen(
                  isDark = isDarkTheme
                )
              }

              AppTab.SCREENER -> {
                ScreenerScreen(
                  isDark = isDarkTheme
                )
              }

              AppTab.COMPANY -> {
                CompanyScreen(
                  initialSymbol = companySymbol,
                  isDark = isDarkTheme,
                  onNavigateToChart = { symbol ->
                    chartSymbol = symbol
                    selectedTab = AppTab.CHART
                  }
                )
              }
            }
          }
        }
      }
    }

    // Quick Search Modal Dialog
    if (showSearchModal) {
      QuickStockSearchDialog(
        onDismiss = { showSearchModal = false },
        onSelectSymbol = { symbol ->
          chartSymbol = symbol
          companySymbol = symbol
          val matched = MarketRepository.topAsxStocks.find { it.symbol == symbol }
          if (matched != null) currentStock = matched
          showSearchModal = false
          selectedTab = AppTab.CHART
        }
      )
    }
  }
}

@Composable
fun QuickStockSearchDialog(
  onDismiss: () -> Unit,
  onSelectSymbol: (String) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  val filtered = remember(searchQuery) {
    if (searchQuery.isBlank()) {
      MarketRepository.topAsxStocks
    } else {
      MarketRepository.topAsxStocks.filter {
        it.ticker.contains(searchQuery, ignoreCase = true) ||
          it.name.contains(searchQuery, ignoreCase = true) ||
          it.sector.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Search Australian Stocks",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("e.g. BHP, CBA, CSL, Banking, Mining") },
          singleLine = true,
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
              }
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("dialog_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Results & Blue Chips:",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
        ) {
          items(filtered) { stock ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectSymbol(stock.symbol) }
                .padding(vertical = 8.dp, horizontal = 4.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(
                  text = "${stock.ticker} - ${stock.name}",
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = stock.sector,
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Text(
                text = String.format("A$%.2f", stock.priceAud),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }

          if (filtered.isEmpty() && searchQuery.isNotBlank()) {
            item {
              val customSym = "ASX:${searchQuery.trim().uppercase()}"
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { onSelectSymbol(customSym) }
                  .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Search custom symbol: $customSym",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.primary,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Close")
      }
    }
  )
}

// Backward compatibility for screenshot / unit tests
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}
