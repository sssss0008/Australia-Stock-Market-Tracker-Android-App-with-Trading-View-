package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AsxStock
import com.example.model.MarketIndex
import com.example.model.MarketRepository
import com.example.ui.components.TradingViewHtmlTemplates
import com.example.ui.components.TradingViewWebView
import com.example.ui.theme.AusGold
import com.example.ui.theme.AusGreen
import com.example.ui.theme.AusRed

enum class MarketTabMode(val label: String, val testTag: String) {
  ALL_STOCKS("Top ASX 20", "tab_all_stocks"),
  MOVERS("Movers & Gainers", "tab_movers"),
  WATCHLIST("My Watchlist", "tab_watchlist"),
  TRADINGVIEW("TradingView Feeds", "tab_tradingview")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketsScreen(
  isDark: Boolean,
  selectedStock: AsxStock,
  onSelectStock: (AsxStock) -> Unit,
  onNavigateToChart: (String) -> Unit,
  onNavigateToCompany: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var activeTabMode by remember { mutableStateOf(MarketTabMode.ALL_STOCKS) }
  var selectedSector by remember { mutableStateOf("All") }
  var searchQuery by remember { mutableStateOf("") }
  var watchlistSymbols by remember { mutableStateOf(setOf("ASX:BHP", "ASX:CBA", "ASX:CSL")) }
  var detailedStockForSheet by remember { mutableStateOf<AsxStock?>(null) }
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val scope = rememberCoroutineScope()

  val sectors = listOf(
    "All",
    "Materials & Mining",
    "Financials & Banking",
    "Healthcare",
    "Technology",
    "Consumer",
    "Energy"
  )

  // Filter stocks based on search query, sector, and tab
  val displayedStocks = remember(searchQuery, selectedSector, activeTabMode, watchlistSymbols) {
    var list = MarketRepository.topAsxStocks

    if (searchQuery.isNotBlank()) {
      val query = searchQuery.trim().lowercase()
      list = list.filter {
        it.ticker.lowercase().contains(query) ||
          it.name.lowercase().contains(query) ||
          it.sector.lowercase().contains(query)
      }
    }

    if (selectedSector != "All") {
      val prefix = selectedSector.split(" ").first()
      list = list.filter { it.sector.contains(prefix, ignoreCase = true) }
    }

    when (activeTabMode) {
      MarketTabMode.ALL_STOCKS -> list
      MarketTabMode.MOVERS -> list.sortedByDescending { kotlin.math.abs(it.changePercent) }
      MarketTabMode.WATCHLIST -> list.filter { watchlistSymbols.contains(it.symbol) }
      MarketTabMode.TRADINGVIEW -> list
    }
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("markets_screen"),
    contentPadding = PaddingValues(bottom = 80.dp)
  ) {
    // 1. Live Australian Benchmark Indices Barometer (Horizontal Scrollable & Clickable)
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
          .padding(vertical = 10.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AusGreen)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "AUSTRALIAN BENCHMARKS",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = AusGreen,
              letterSpacing = 1.sp
            )
          }
          Text(
            text = "Tap to Chart",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LazyRow(
          contentPadding = PaddingValues(horizontal = 12.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(MarketRepository.australianIndices) { index ->
            IndexSummaryCard(
              index = index,
              onClick = {
                onNavigateToChart(index.symbol)
              }
            )
          }
        }
      }
    }

    // 2. Market Tab Switcher (Touch-friendly filter chips)
    item {
      Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
          LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(MarketTabMode.values()) { tab ->
              FilterChip(
                selected = activeTabMode == tab,
                onClick = { activeTabMode = tab },
                label = {
                  Text(
                    text = tab.label,
                    fontSize = 12.sp,
                    fontWeight = if (activeTabMode == tab) FontWeight.Bold else FontWeight.Medium
                  )
                },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primary,
                  selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.testTag(tab.testTag)
              )
            }
          }
        }
      }
    }

    // 3. Search and Sector Filter Row (Only when in stock lists)
    if (activeTabMode != MarketTabMode.TRADINGVIEW) {
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
          // Search Field
          OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search ASX stocks by name, ticker, or sector...") },
            leadingIcon = {
              Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { searchQuery = "" }) {
                  Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
              }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = MaterialTheme.colorScheme.surface,
              unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("markets_search_input")
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Sector Filter Chips
          LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            items(sectors) { sector ->
              FilterChip(
                selected = selectedSector == sector,
                onClick = { selectedSector = sector },
                label = { Text(sector, fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                  selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
              )
            }
          }
        }
      }
    }

    // 4. Content Area
    if (activeTabMode == MarketTabMode.TRADINGVIEW) {
      // TradingView Live Widgets
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
        ) {
          Text(
            text = "ASX & Global Market Overview",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Real-time streaming charts and quotes directly from TradingView.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(10.dp))

          TradingViewWebView(
            htmlContent = TradingViewHtmlTemplates.marketOverview(isDark = isDark),
            modifier = Modifier
              .fillMaxWidth()
              .height(550.dp)
              .clip(RoundedCornerShape(12.dp)),
            backgroundColor = MaterialTheme.colorScheme.surface,
            testTag = "tv_market_overview"
          )

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "Live Market Quotes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Streaming prices and bid/ask ranges for major Australian market sectors.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(10.dp))

          TradingViewWebView(
            htmlContent = TradingViewHtmlTemplates.marketQuotes(isDark = isDark),
            modifier = Modifier
              .fillMaxWidth()
              .height(520.dp)
              .clip(RoundedCornerShape(12.dp)),
            backgroundColor = MaterialTheme.colorScheme.surface,
            testTag = "tv_market_quotes"
          )

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "ASX Hotlists (Top Gainers & Volume)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(10.dp))

          TradingViewWebView(
            htmlContent = TradingViewHtmlTemplates.hotlists(isDark = isDark),
            modifier = Modifier
              .fillMaxWidth()
              .height(520.dp)
              .clip(RoundedCornerShape(12.dp)),
            backgroundColor = MaterialTheme.colorScheme.surface,
            testTag = "tv_hotlists"
          )
        }
      }
    } else {
      // Native, Touch-Friendly Stock Cards
      if (displayedStocks.isEmpty()) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(40.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = if (activeTabMode == MarketTabMode.WATCHLIST) "No stocks in your watchlist" else "No matching stocks found",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = if (activeTabMode == MarketTabMode.WATCHLIST) "Tap the star icon on any stock card to add it to your watchlist." else "Try searching with a different ticker or keyword.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      } else {
        items(displayedStocks, key = { it.symbol }) { stock ->
          val isStarred = watchlistSymbols.contains(stock.symbol)
          val isSelected = selectedStock.symbol == stock.symbol

          StockItemCard(
            stock = stock,
            isStarred = isStarred,
            isSelected = isSelected,
            onCardClick = {
              onSelectStock(stock)
              detailedStockForSheet = stock
            },
            onStarToggle = {
              watchlistSymbols = if (isStarred) {
                watchlistSymbols - stock.symbol
              } else {
                watchlistSymbols + stock.symbol
              }
            },
            onOpenChart = {
              onNavigateToChart(stock.symbol)
            },
            onOpenCompany = {
              onNavigateToCompany(stock.symbol)
            }
          )
        }
      }
    }
  }

  // 5. Interactive Stock Detail Bottom Sheet (Touch-friendly modal)
  detailedStockForSheet?.let { stock ->
    val isStarred = watchlistSymbols.contains(stock.symbol)

    ModalBottomSheet(
      onDismissRequest = { detailedStockForSheet = null },
      sheetState = sheetState,
      containerColor = MaterialTheme.colorScheme.surface,
      modifier = Modifier.testTag("stock_detail_bottom_sheet")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp)
      ) {
        // Sheet Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.primaryContainer
            ) {
              Text(
                text = stock.ticker,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = stock.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = getSectorVectorIcon(stock.sector),
                  contentDescription = null,
                  modifier = Modifier.size(13.dp),
                  tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = stock.sector,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }

          IconButton(
            onClick = {
              watchlistSymbols = if (isStarred) {
                watchlistSymbols - stock.symbol
              } else {
                watchlistSymbols + stock.symbol
              }
            }
          ) {
            Icon(
              imageVector = if (isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
              contentDescription = "Watchlist",
              tint = if (isStarred) AusGold else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Price & Metrics
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Live Price",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = String.format("A$%.2f", stock.priceAud),
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (stock.changePercent >= 0) AusGreen.copy(alpha = 0.15f) else AusRed.copy(alpha = 0.15f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (stock.changePercent >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                contentDescription = null,
                tint = if (stock.changePercent >= 0) AusGreen else AusRed,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = String.format("%+.2f%%", stock.changePercent),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (stock.changePercent >= 0) AusGreen else AusRed
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(12.dp))

        // Key stats grid
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          MetricItem(label = "Market Cap", value = stock.marketCap)
          MetricItem(label = "Exchange", value = "ASX Australia")
          MetricItem(label = "Currency", value = "AUD ($)")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
          text = "About ${stock.name}",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = stock.description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons (Touch targets at least 48dp)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              detailedStockForSheet = null
              onNavigateToChart(stock.symbol)
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("sheet_btn_chart")
          ) {
            Icon(Icons.Default.ShowChart, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Interactive Chart", fontWeight = FontWeight.SemiBold)
          }

          OutlinedButton(
            onClick = {
              detailedStockForSheet = null
              onNavigateToCompany(stock.symbol)
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("sheet_btn_company")
          ) {
            Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Technicals & Fin", fontWeight = FontWeight.SemiBold)
          }
        }

        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
fun IndexSummaryCard(
  index: MarketIndex,
  onClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .width(140.dp)
      .clickable { onClick() }
      .testTag("index_card_${index.symbol}")
  ) {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(
        text = index.name,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = index.value,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(2.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = if (index.isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
          contentDescription = null,
          tint = if (index.isPositive) AusGreen else AusRed,
          modifier = Modifier.size(13.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = index.change,
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = if (index.isPositive) AusGreen else AusRed
        )
      }
    }
  }
}

@Composable
fun StockItemCard(
  stock: AsxStock,
  isStarred: Boolean,
  isSelected: Boolean,
  onCardClick: () -> Unit,
  onStarToggle: () -> Unit,
  onOpenChart: () -> Unit,
  onOpenCompany: () -> Unit
) {
  ElevatedCard(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.elevatedCardColors(
      containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 14.dp, vertical = 5.dp)
      .clickable { onCardClick() }
      .testTag("stock_card_${stock.ticker}")
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // Left info
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        // Ticker Badge
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.size(44.dp)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(
              text = stock.ticker,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
            )
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = stock.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
          )
          Spacer(modifier = Modifier.height(2.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = getSectorVectorIcon(stock.sector),
              contentDescription = null,
              modifier = Modifier.size(11.dp),
              tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = stock.sector,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              maxLines = 1
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Right: Price and Change
      Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = String.format("A$%.2f", stock.priceAud),
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(2.dp))
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = if (stock.changePercent >= 0) AusGreen.copy(alpha = 0.15f) else AusRed.copy(alpha = 0.15f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = if (stock.changePercent >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
              contentDescription = null,
              tint = if (stock.changePercent >= 0) AusGreen else AusRed,
              modifier = Modifier.size(11.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
              text = String.format("%+.2f%%", stock.changePercent),
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = if (stock.changePercent >= 0) AusGreen else AusRed
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(6.dp))

      // Star Icon Button
      IconButton(
        onClick = onStarToggle,
        modifier = Modifier.size(36.dp)
      ) {
        Icon(
          imageVector = if (isStarred) Icons.Filled.Star else Icons.Outlined.StarBorder,
          contentDescription = "Star stock",
          tint = if (isStarred) AusGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}

fun getSectorVectorIcon(sector: String): ImageVector {
  return when {
    sector.contains("Material", ignoreCase = true) || sector.contains("Mining", ignoreCase = true) -> Icons.Default.Terrain
    sector.contains("Financial", ignoreCase = true) || sector.contains("Bank", ignoreCase = true) -> Icons.Default.AccountBalance
    sector.contains("Health", ignoreCase = true) -> Icons.Default.LocalHospital
    sector.contains("Tech", ignoreCase = true) -> Icons.Default.Memory
    sector.contains("Energy", ignoreCase = true) -> Icons.Default.Bolt
    sector.contains("Consumer", ignoreCase = true) || sector.contains("Retail", ignoreCase = true) -> Icons.Default.Storefront
    sector.contains("Real Estate", ignoreCase = true) -> Icons.Default.Apartment
    sector.contains("Industrial", ignoreCase = true) -> Icons.Default.PrecisionManufacturing
    sector.contains("Telecom", ignoreCase = true) || sector.contains("Communication", ignoreCase = true) -> Icons.Default.CellTower
    else -> Icons.Default.Category
  }
}

@Composable
fun MetricItem(label: String, value: String) {
  Column {
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = value,
      style = MaterialTheme.typography.bodyMedium,
      fontWeight = FontWeight.SemiBold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}
