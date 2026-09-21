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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarketRepository
import com.example.ui.components.TradingViewHtmlTemplates
import com.example.ui.components.TradingViewWebView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyScreen(
  initialSymbol: String = "ASX:BHP",
  isDark: Boolean,
  onNavigateToChart: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var activeSymbol by remember(initialSymbol) { mutableStateOf(initialSymbol) }
  var selectedTab by remember { mutableIntStateOf(0) }
  var techInterval by remember { mutableStateOf("1M") }
  var customInput by remember { mutableStateOf("") }
  val focusManager = LocalFocusManager.current

  val tabs = listOf("Overview & Profile", "Technical Gauges", "Financials")
  val intervals = listOf("1m", "5m", "15m", "1h", "1D", "1W", "1M")

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("company_screen")
  ) {
    // 1. Symbol Picker Bar
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
          Column {
            Text(
              text = "Company Fundamentals & Technicals",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Viewing: $activeSymbol",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.primary
            )
          }

          ElevatedButton(
            onClick = { onNavigateToChart(activeSymbol) },
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            modifier = Modifier.height(34.dp)
          ) {
            Icon(Icons.Default.ShowChart, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Chart", fontSize = 12.sp)
          }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Search textfield
        OutlinedTextField(
          value = customInput,
          onValueChange = { customInput = it },
          placeholder = { Text("Look up company ticker (e.g. BHP, CBA, CSL)...") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
          },
          trailingIcon = {
            if (customInput.isNotEmpty()) {
              IconButton(onClick = { customInput = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
              }
            }
          },
          singleLine = true,
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
          keyboardActions = KeyboardActions(onSearch = {
            if (customInput.isNotBlank()) {
              val formatted = customInput.trim().uppercase()
              activeSymbol = if (!formatted.contains(":")) {
                if (formatted.length in 3..4) "ASX:$formatted" else formatted
              } else {
                formatted
              }
              focusManager.clearFocus()
            }
          }),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("company_search_input"),
          shape = RoundedCornerShape(10.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
          )
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Quick Blue Chips Chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items(MarketRepository.topAsxStocks) { stock ->
            FilterChip(
              selected = activeSymbol == stock.symbol,
              onClick = { activeSymbol = stock.symbol },
              label = { Text("${stock.ticker} (${stock.name.take(8)})", fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
              )
            )
          }
        }
      }
    }

    // 2. Sub-Tabs Header
    PrimaryTabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surfaceVariant,
      contentColor = MaterialTheme.colorScheme.primary
    ) {
      tabs.forEachIndexed { index, title ->
        val icon = when (index) {
          0 -> Icons.Default.Info
          1 -> Icons.Default.Speed
          else -> Icons.Default.AccountBalance
        }
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          icon = {
            Icon(
              imageVector = icon,
              contentDescription = title,
              modifier = Modifier.size(16.dp)
            )
          },
          text = { Text(title, fontSize = 11.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
        )
      }
    }

    // 3. Tab Content
    when (selectedTab) {
      0 -> {
        // Overview & Profile: Symbol Info + Company Profile
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
        ) {
          Text(
            text = "Real-time Market Valuation & Performance",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(6.dp))
          TradingViewWebView(
            htmlContent = TradingViewHtmlTemplates.symbolInfo(activeSymbol, isDark = isDark),
            modifier = Modifier
              .fillMaxWidth()
              .height(200.dp),
            backgroundColor = MaterialTheme.colorScheme.surface,
            testTag = "company_symbol_info"
          )

          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Business Profile & Corporate Overview",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(6.dp))
          TradingViewWebView(
            htmlContent = TradingViewHtmlTemplates.companyProfile(activeSymbol, isDark = isDark),
            modifier = Modifier
              .fillMaxWidth()
              .height(340.dp),
            backgroundColor = MaterialTheme.colorScheme.surface,
            testTag = "company_profile_widget"
          )
        }
      }

      1 -> {
        // Technical Gauges
        Column(
          modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(14.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Technical Summary Meter",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )

            // Interval Selector
            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              items(intervals) { intVal ->
                FilterChip(
                  selected = techInterval == intVal,
                  onClick = { techInterval = intVal },
                  label = { Text(intVal, fontSize = 11.sp) },
                  colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Oscillators, moving averages, and technical indicators aggregate score for $activeSymbol ($techInterval timeframe).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(12.dp))

          TradingViewWebView(
            htmlContent = TradingViewHtmlTemplates.technicalAnalysis(
              symbol = activeSymbol,
              interval = techInterval,
              isDark = isDark
            ),
            modifier = Modifier
              .fillMaxWidth()
              .height(420.dp),
            backgroundColor = MaterialTheme.colorScheme.surface,
            testTag = "company_technical_analysis"
          )
        }
      }

      2 -> {
        // Financial Statements Overview
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
          Text(
            text = "Financial Fundamentals & Statements",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Annual and quarterly income statements, balance sheets, and cash flows for $activeSymbol.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(8.dp))

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .weight(1f)
          ) {
            TradingViewWebView(
              htmlContent = TradingViewHtmlTemplates.financials(activeSymbol, isDark = isDark),
              modifier = Modifier.fillMaxSize(),
              backgroundColor = MaterialTheme.colorScheme.surface,
              testTag = "company_financials_widget"
            )
          }
        }
      }
    }
  }
}
