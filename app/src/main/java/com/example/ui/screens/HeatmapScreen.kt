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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.TradingViewHtmlTemplates
import com.example.ui.components.TradingViewWebView
import com.example.ui.theme.AusGreen
import com.example.ui.theme.AusRed

@Composable
fun HeatmapScreen(
  isDark: Boolean,
  modifier: Modifier = Modifier
) {
  var selectedDataSource by remember { mutableStateOf("AllAustralia") }

  val dataSources = listOf(
    "AllAustralia" to "Australia (ASX)",
    "AllUSA" to "US Markets",
    "SPX500" to "S&P 500"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .testTag("heatmap_screen")
  ) {
    // Header Bar with Legend
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
            text = "Stock Market Heatmap",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          // Legend
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(AusGreen)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Gain", style = MaterialTheme.typography.labelSmall, color = AusGreen)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(AusRed)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Loss", style = MaterialTheme.typography.labelSmall, color = AusRed)
          }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Tile size represents Market Cap, color indicates price change %.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Data source chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(bottom = 4.dp)
        ) {
          items(dataSources) { (sourceId, label) ->
            FilterChip(
              selected = selectedDataSource == sourceId,
              onClick = { selectedDataSource = sourceId },
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

    // Interactive Heatmap WebView
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f)
    ) {
      TradingViewWebView(
        htmlContent = TradingViewHtmlTemplates.stockHeatmap(
          dataSource = selectedDataSource,
          isDark = isDark
        ),
        modifier = Modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
        testTag = "stock_heatmap_webview"
      )
    }
  }
}
