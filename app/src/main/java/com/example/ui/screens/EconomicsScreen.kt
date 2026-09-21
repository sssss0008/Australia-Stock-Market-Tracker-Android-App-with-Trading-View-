package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EconomicsScreen(
  isDark: Boolean,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var countryFilter by remember { mutableStateOf("au") }
  var mapRegion by remember { mutableStateOf("oceania") }

  val tabs = listOf("Economic Calendar", "Economic Map")

  val countries = listOf(
    "au" to "Australia (RBA & ABS)",
    "us" to "United States (Fed)",
    "au,us,gb" to "Global Major"
  )

  val regions = listOf(
    "oceania" to "Oceania & Pacific",
    "asia" to "Asia-Pacific",
    "world" to "World Macro"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("economics_screen")
  ) {
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
        Text(
          text = "Macroeconomics & Reserve Bank of Australia",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "Key monetary policy releases, CPI inflation reports, and macroeconomic geographic indicators.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    PrimaryTabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surfaceVariant,
      contentColor = MaterialTheme.colorScheme.primary
    ) {
      tabs.forEachIndexed { index, title ->
        val icon = if (index == 0) Icons.Default.EventNote else Icons.Default.Public
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          icon = {
            Icon(
              imageVector = icon,
              contentDescription = title,
              modifier = Modifier.size(18.dp)
            )
          },
          text = { Text(title, fontSize = 12.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
        )
      }
    }

    when (selectedTab) {
      0 -> {
        // Economic Calendar Tab
        Column(modifier = Modifier.fillMaxSize()) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Country Filter: ",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              items(countries) { (cCode, label) ->
                FilterChip(
                  selected = countryFilter == cCode,
                  onClick = { countryFilter = cCode },
                  label = { Text(label, fontSize = 11.sp) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                  )
                )
              }
            }
          }

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
          ) {
            TradingViewWebView(
              htmlContent = TradingViewHtmlTemplates.economicCalendar(
                countryFilter = countryFilter,
                isDark = isDark
              ),
              modifier = Modifier.fillMaxSize(),
              backgroundColor = MaterialTheme.colorScheme.background,
              testTag = "economic_calendar_webview"
            )
          }
        }
      }

      1 -> {
        // Economic Map Tab
        Column(modifier = Modifier.fillMaxSize()) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Region: ",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              items(regions) { (rCode, label) ->
                FilterChip(
                  selected = mapRegion == rCode,
                  onClick = { mapRegion = rCode },
                  label = { Text(label, fontSize = 11.sp) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                  )
                )
              }
            }
          }

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
          ) {
            TradingViewWebView(
              htmlContent = TradingViewHtmlTemplates.economicMap(
                region = mapRegion,
                isDark = isDark
              ),
              modifier = Modifier.fillMaxSize(),
              backgroundColor = MaterialTheme.colorScheme.background,
              testTag = "economic_map_webview"
            )
          }
        }
      }
    }
  }
}
