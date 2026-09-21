package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TradingViewHtmlTemplates
import com.example.ui.components.TradingViewWebView

@Composable
fun ScreenerScreen(
  isDark: Boolean,
  modifier: Modifier = Modifier
) {
  var activeScreen by remember { mutableStateOf("most_capitalized") }
  var activeMarket by remember { mutableStateOf("australia") }

  val screenOptions = listOf(
    "most_capitalized" to "Top Market Cap",
    "top_gainers" to "Top Gainers",
    "top_losers" to "Top Losers",
    "high_dividend" to "High Dividend",
    "most_volatile" to "Volatile"
  )

  val marketOptions = listOf(
    "australia" to "Australia (ASX)",
    "america" to "US Equities"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("screener_screen")
  ) {
    // Top Screener Configuration Bar
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "ASX Equities Screener",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          // Market Selector
          LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(marketOptions) { (marketId, label) ->
              FilterChip(
                selected = activeMarket == marketId,
                onClick = { activeMarket = marketId },
                label = { Text(label, fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                  selectedContainerColor = MaterialTheme.colorScheme.primary,
                  selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Real-time fundamental and technical screening across Australian stocks.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Preset screens
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(bottom = 4.dp)
        ) {
          items(screenOptions) { (screenId, label) ->
            val icon = when (screenId) {
              "most_capitalized" -> Icons.Default.CorporateFare
              "top_gainers" -> Icons.Default.TrendingUp
              "top_losers" -> Icons.Default.TrendingDown
              "high_dividend" -> Icons.Default.Payments
              else -> Icons.Default.ElectricBolt
            }
            FilterChip(
              selected = activeScreen == screenId,
              onClick = { activeScreen = screenId },
              leadingIcon = {
                Icon(
                  imageVector = icon,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
              },
              label = { Text(label, fontSize = 12.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }
      }
    }

    // Interactive Screener Table WebView
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
    ) {
      TradingViewWebView(
        htmlContent = TradingViewHtmlTemplates.stockScreener(
          market = activeMarket,
          defaultScreen = activeScreen,
          isDark = isDark
        ),
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
        testTag = "stock_screener_webview"
      )
    }
  }
}
