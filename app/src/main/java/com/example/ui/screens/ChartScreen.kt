package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TradingViewHtmlTemplates
import com.example.ui.components.TradingViewWebView
import com.example.ui.theme.AusGold

@Composable
fun ChartScreen(
  initialSymbol: String = "ASX:XJO",
  isDark: Boolean,
  modifier: Modifier = Modifier
) {
  var activeSymbol by remember(initialSymbol) { mutableStateOf(initialSymbol) }
  var activeInterval by remember { mutableStateOf("D") }
  var activeRange by remember { mutableStateOf("YTD") }
  var searchInput by remember { mutableStateOf("") }
  val focusManager = LocalFocusManager.current

  val quickSymbols = listOf(
    "ASX:XJO" to "ASX 200",
    "ASX:BHP" to "BHP",
    "ASX:CBA" to "CBA",
    "ASX:CSL" to "CSL",
    "ASX:FMG" to "Fortescue",
    "ASX:RIO" to "Rio Tinto",
    "ASX:WES" to "Wesfarmers",
    "ASX:MQG" to "Macquarie",
    "FX_IDC:AUDUSD" to "AUD/USD",
    "BITSTAMP:BTCUSD" to "BTC/USD"
  )

  val intervals = listOf(
    "1" to "1m",
    "5" to "5m",
    "15" to "15m",
    "60" to "1h",
    "D" to "Daily",
    "W" to "Weekly"
  )

  val ranges = listOf("1D", "1M", "3M", "1Y", "YTD", "5Y", "ALL")

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("chart_screen")
  ) {
    // 1. Symbol Search & Active Indicator Bar
    Card(
      shape = RoundedCornerShape(0.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = AusGold.copy(alpha = 0.2f)
            ) {
              Text(
                text = "ASX CHART",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = AusGold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = activeSymbol,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Text(
            text = "Timezone: Sydney",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search textfield
        OutlinedTextField(
          value = searchInput,
          onValueChange = { searchInput = it },
          placeholder = { Text("Search ticker (e.g. ASX:BHP, ASX:CBA, AUDUSD)...") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          },
          trailingIcon = {
            if (searchInput.isNotEmpty()) {
              IconButton(onClick = {
                searchInput = ""
              }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
              }
            }
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
          keyboardActions = KeyboardActions(onSearch = {
            if (searchInput.isNotBlank()) {
              val formatted = searchInput.trim().uppercase()
              activeSymbol = if (!formatted.contains(":")) {
                if (formatted.length == 3 || formatted.length == 4) "ASX:$formatted" else formatted
              } else {
                formatted
              }
              focusManager.clearFocus()
            }
          }),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("chart_symbol_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
          )
        )
      }
    }

    // 2. Quick Symbol Selector Chips
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)
        .padding(vertical = 4.dp),
      contentPadding = PaddingValues(horizontal = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      items(quickSymbols) { (sym, label) ->
        FilterChip(
          selected = activeSymbol == sym,
          onClick = { activeSymbol = sym },
          label = { Text(label, fontSize = 12.sp) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
          )
        )
      }
    }

    // 3. Time Interval & Range Selection Row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        .padding(horizontal = 12.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Interval:",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(end = 6.dp)
      )

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.weight(1f)
      ) {
        items(intervals) { (intVal, label) ->
          FilterChip(
            selected = activeInterval == intVal,
            onClick = { activeInterval = intVal },
            label = { Text(label, fontSize = 11.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MaterialTheme.colorScheme.primary,
              selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
          )
        }
      }
    }

    // 4. Interactive TradingView Advanced Chart
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
    ) {
      TradingViewWebView(
        htmlContent = TradingViewHtmlTemplates.advancedChart(
          symbol = activeSymbol,
          interval = activeInterval,
          range = activeRange,
          isDark = isDark
        ),
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
        testTag = "advanced_tradingview_chart"
      )
    }
  }
}
