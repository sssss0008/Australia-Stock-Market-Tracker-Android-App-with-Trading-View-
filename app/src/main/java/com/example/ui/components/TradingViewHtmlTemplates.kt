package com.example.ui.components

object TradingViewHtmlTemplates {

  fun tickerTape(
    symbols: String = "ASX:XJO,ASX:BHP,ASX:CBA,ASX:CSL,ASX:NAB,ASX:WBC,ASX:ANZ,ASX:WES,ASX:MQG,ASX:RIO,ASX:FMG,ASX:WDS,ASX:TLS,FX_IDC:AUDUSD",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-ticker-tape.js"></script>
      <tv-ticker-tape
        symbols="$symbols"
        theme="$theme"
        show-hover
        style="width: 100%; display: block;">
      </tv-ticker-tape>
    """.trimIndent()
  }

  fun advancedChart(
    symbol: String = "ASX:XJO",
    interval: String = "D",
    range: String = "YTD",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    val bgColor = if (isDark) "#070D1E" else "#FFFFFF"
    val gridColor = if (isDark) "rgba(255, 255, 255, 0.06)" else "rgba(46, 46, 46, 0.1)"
    return """
      <div class="tradingview-widget-container" style="height:100%;width:100%">
        <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-advanced-chart.js" async>
        {
          "autosize": true,
          "symbol": "$symbol",
          "interval": "$interval",
          "timezone": "Australia/Sydney",
          "theme": "$theme",
          "style": "1",
          "locale": "en",
          "enable_publishing": false,
          "allow_symbol_change": true,
          "calendar": false,
          "details": true,
          "hide_side_toolbar": false,
          "hide_top_toolbar": false,
          "hide_legend": false,
          "hide_volume": false,
          "hotlist": true,
          "backgroundColor": "$bgColor",
          "gridColor": "$gridColor",
          "watchlist": [],
          "withdateranges": true,
          "range": "$range",
          "support_host": "https://www.tradingview.com"
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun marketOverview(
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="height:100%;width:100%">
        <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-market-overview.js" async>
        {
          "colorTheme": "$theme",
          "dateRange": "12M",
          "showChart": true,
          "locale": "en",
          "largeChartUrl": "",
          "isTransparent": false,
          "showSymbolLogo": true,
          "showFloatingTooltip": false,
          "width": "100%",
          "height": "600",
          "plotLineColorGrowing": "rgba(41, 98, 255, 1)",
          "plotLineColorFalling": "rgba(41, 98, 255, 1)",
          "gridLineColor": "rgba(240, 243, 246, 0.08)",
          "scaleFontColor": "rgba(120, 123, 134, 1)",
          "belowLineFillColorGrowing": "rgba(41, 98, 255, 0.12)",
          "belowLineFillColorFalling": "rgba(41, 98, 255, 0.12)",
          "belowLineFillColorGrowingBottom": "rgba(41, 98, 255, 0)",
          "belowLineFillColorFallingBottom": "rgba(41, 98, 255, 0)",
          "symbolActiveColor": "rgba(41, 98, 255, 0.12)",
          "tabs": [
            {
              "title": "Australian Indices",
              "symbols": [
                { "s": "ASX:XJO", "d": "S&P/ASX 200" },
                { "s": "ASX:XAO", "d": "All Ordinaries" },
                { "s": "ASX:XSO", "d": "ASX Small Ordinaries" },
                { "s": "ASX:XFJ", "d": "ASX 200 Financials" },
                { "s": "ASX:XMJ", "d": "ASX 200 Materials" },
                { "s": "ASX:XIJ", "d": "ASX 200 Info Tech" }
              ]
            },
            {
              "title": "ASX Blue Chips",
              "symbols": [
                { "s": "ASX:BHP", "d": "BHP Group" },
                { "s": "ASX:CBA", "d": "Commonwealth Bank" },
                { "s": "ASX:CSL", "d": "CSL Limited" },
                { "s": "ASX:NAB", "d": "National Australia Bank" },
                { "s": "ASX:WBC", "d": "Westpac Banking Corp" },
                { "s": "ASX:ANZ", "d": "ANZ Group" },
                { "s": "ASX:WES", "d": "Wesfarmers" },
                { "s": "ASX:MQG", "d": "Macquarie Group" },
                { "s": "ASX:RIO", "d": "Rio Tinto" },
                { "s": "ASX:FMG", "d": "Fortescue" }
              ]
            },
            {
              "title": "AUD & Commodities",
              "symbols": [
                { "s": "FX_IDC:AUDUSD", "d": "AUD / USD" },
                { "s": "FX_IDC:AUDNZD", "d": "AUD / NZD" },
                { "s": "TVC:GOLD", "d": "Gold Spot" },
                { "s": "TVC:SILVER", "d": "Silver Spot" },
                { "s": "NYMEX:CL1!", "d": "Crude Oil" }
              ]
            }
          ]
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun hotlists(
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="height:100%;width:100%">
        <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-hotlists.js" async>
        {
          "colorTheme": "$theme",
          "dateRange": "12M",
          "exchange": "ASX",
          "showChart": true,
          "locale": "en",
          "largeChartUrl": "",
          "isTransparent": false,
          "showSymbolLogo": true,
          "showFloatingTooltip": false,
          "width": "100%",
          "height": "580",
          "plotLineColorGrowing": "rgba(41, 98, 255, 1)",
          "plotLineColorFalling": "rgba(41, 98, 255, 1)",
          "gridLineColor": "rgba(240, 243, 246, 0.08)",
          "scaleFontColor": "rgba(120, 123, 134, 1)",
          "belowLineFillColorGrowing": "rgba(41, 98, 255, 0.12)",
          "belowLineFillColorFalling": "rgba(41, 98, 255, 0.12)",
          "belowLineFillColorGrowingBottom": "rgba(41, 98, 255, 0)",
          "belowLineFillColorFallingBottom": "rgba(41, 98, 255, 0)",
          "symbolActiveColor": "rgba(41, 98, 255, 0.12)"
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun marketQuotes(
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="height:100%;width:100%">
        <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-market-quotes.js" async>
        {
          "width": "100%",
          "height": 550,
          "symbolsGroups": [
            {
              "name": "ASX Leaders",
              "symbols": [
                { "name": "ASX:BHP", "displayName": "BHP Group" },
                { "name": "ASX:CBA", "displayName": "Commonwealth Bank" },
                { "name": "ASX:CSL", "displayName": "CSL Ltd" },
                { "name": "ASX:NAB", "displayName": "National Australia Bank" },
                { "name": "ASX:WBC", "displayName": "Westpac Banking" },
                { "name": "ASX:ANZ", "displayName": "ANZ Bank" },
                { "name": "ASX:WES", "displayName": "Wesfarmers" },
                { "name": "ASX:MQG", "displayName": "Macquarie Group" },
                { "name": "ASX:RIO", "displayName": "Rio Tinto" },
                { "name": "ASX:FMG", "displayName": "Fortescue Metals" }
              ]
            },
            {
              "name": "Mining & Resources",
              "symbols": [
                { "name": "ASX:BHP", "displayName": "BHP" },
                { "name": "ASX:RIO", "displayName": "Rio Tinto" },
                { "name": "ASX:FMG", "displayName": "Fortescue" },
                { "name": "ASX:WDS", "displayName": "Woodside Energy" },
                { "name": "ASX:STO", "displayName": "Santos" },
                { "name": "ASX:MIN", "displayName": "Mineral Resources" },
                { "name": "ASX:PLS", "displayName": "Pilbara Minerals" }
              ]
            },
            {
              "name": "Banking & Financials",
              "symbols": [
                { "name": "ASX:CBA", "displayName": "CBA" },
                { "name": "ASX:NAB", "displayName": "NAB" },
                { "name": "ASX:WBC", "displayName": "Westpac" },
                { "name": "ASX:ANZ", "displayName": "ANZ" },
                { "name": "ASX:MQG", "displayName": "Macquarie" }
              ]
            }
          ],
          "showSymbolLogo": true,
          "isTransparent": false,
          "colorTheme": "$theme",
          "locale": "en"
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun timeline(
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="height:100%;width:100%">
        <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-timeline.js" async>
        {
          "feedMode": "market",
          "market": "stock",
          "isTransparent": false,
          "displayMode": "regular",
          "width": "100%",
          "height": 550,
          "colorTheme": "$theme",
          "locale": "en"
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun miniSymbolOverview(
    symbol: String = "ASX:XJO",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="width:100%">
        <div class="tradingview-widget-container__widget"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-mini-symbol-overview.js" async>
        {
          "symbol": "$symbol",
          "width": "100%",
          "height": 220,
          "locale": "en",
          "dateRange": "1M",
          "colorTheme": "$theme",
          "isTransparent": false,
          "autosize": false,
          "largeChartUrl": ""
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun stockHeatmap(
    dataSource: String = "AllAustralia",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="height:100%;width:100%">
        <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-stock-heatmap.js" async>
        {
          "dataSource": "$dataSource",
          "blockSize": "market_cap_basic",
          "blockColor": "change",
          "grouping": "sector",
          "locale": "en",
          "colorTheme": "$theme",
          "exchanges": [],
          "hasTopBar": true,
          "isDataSetEnabled": true,
          "isZoomEnabled": true,
          "hasSymbolTooltip": true,
          "isMonoSize": false,
          "width": "100%",
          "height": "100%"
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun stockScreener(
    market: String = "australia",
    defaultScreen: String = "most_capitalized",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="height:100%;width:100%">
        <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-screener.js" async>
        {
          "market": "$market",
          "showToolbar": true,
          "defaultColumn": "overview",
          "defaultScreen": "$defaultScreen",
          "isTransparent": false,
          "locale": "en",
          "colorTheme": "$theme",
          "width": "100%",
          "height": "100%"
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun economicCalendar(
    countryFilter: String = "au",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="height:100%;width:100%">
        <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-events.js" async>
        {
          "colorTheme": "$theme",
          "isTransparent": false,
          "locale": "en",
          "countryFilter": "$countryFilter",
          "importanceFilter": "-1,0,1",
          "width": "100%",
          "height": "100%"
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun economicMap(
    region: String = "oceania",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-economic-map.js"></script>
      <tv-economic-map
        region="$region"
        theme="$theme"
        hide-legend
        style="width: 100%; height: 100%; display: block;">
      </tv-economic-map>
    """.trimIndent()
  }

  fun symbolInfo(
    symbol: String = "ASX:BHP",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="width:100%">
        <div class="tradingview-widget-container__widget"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-symbol-info.js" async>
        {
          "symbol": "$symbol",
          "colorTheme": "$theme",
          "isTransparent": false,
          "locale": "en",
          "width": "100%"
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun technicalAnalysis(
    symbol: String = "ASX:BHP",
    interval: String = "1M",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-technical-analysis.js"></script>
      <tv-technical-analysis
        symbol="$symbol"
        interval="$interval"
        ratings-mode="multiple"
        theme="$theme"
        style="width: 100%; display: block;">
      </tv-technical-analysis>
    """.trimIndent()
  }

  fun financials(
    symbol: String = "ASX:BHP",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <div class="tradingview-widget-container" style="height:100%;width:100%">
        <div class="tradingview-widget-container__widget" style="height:100%;width:100%"></div>
        <script type="text/javascript" src="https://s3.tradingview.com/external-embedding/embed-widget-financials.js" async>
        {
          "symbol": "$symbol",
          "colorTheme": "$theme",
          "displayMode": "regular",
          "isTransparent": false,
          "locale": "en",
          "width": "100%",
          "height": 550
        }
        </script>
      </div>
    """.trimIndent()
  }

  fun companyProfile(
    symbol: String = "ASX:BHP",
    isDark: Boolean = true
  ): String {
    val theme = if (isDark) "dark" else "light"
    return """
      <script type="module" src="https://widgets.tradingview-widget.com/w/en/tv-company-profile.js"></script>
      <tv-company-profile
        symbol="$symbol"
        theme="$theme"
        style="width: 100%; display: block;">
      </tv-company-profile>
    """.trimIndent()
  }
}
