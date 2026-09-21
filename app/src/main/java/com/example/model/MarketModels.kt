package com.example.model

data class AsxStock(
  val symbol: String, // e.g. "ASX:BHP"
  val ticker: String, // e.g. "BHP"
  val name: String,
  val sector: String,
  val priceAud: Double,
  val changePercent: Double,
  val marketCap: String,
  val description: String
)

data class MarketIndex(
  val symbol: String,
  val name: String,
  val value: String,
  val change: String,
  val isPositive: Boolean
)

object MarketRepository {
  val australianIndices = listOf(
    MarketIndex(
      symbol = "ASX:XJO",
      name = "S&P/ASX 200",
      value = "8,415.20",
      change = "+0.64%",
      isPositive = true
    ),
    MarketIndex(
      symbol = "ASX:XAO",
      name = "All Ordinaries",
      value = "8,658.80",
      change = "+0.58%",
      isPositive = true
    ),
    MarketIndex(
      symbol = "FX_IDC:AUDUSD",
      name = "AUD / USD",
      value = "0.6582",
      change = "+0.22%",
      isPositive = true
    ),
    MarketIndex(
      symbol = "TVC:GOLD",
      name = "Gold (AUD/oz)",
      value = "4,120.50",
      change = "+1.15%",
      isPositive = true
    ),
    MarketIndex(
      symbol = "BITSTAMP:BTCUSD",
      name = "Bitcoin (USD)",
      value = "64,250",
      change = "+2.40%",
      isPositive = true
    )
  )

  val topAsxStocks = listOf(
    AsxStock(
      symbol = "ASX:BHP",
      ticker = "BHP",
      name = "BHP Group Limited",
      sector = "Materials & Mining",
      priceAud = 42.85,
      changePercent = 1.42,
      marketCap = "A$216B",
      description = "World's largest diversified resources company, leading in iron ore, copper, metallurgical coal, and nickel."
    ),
    AsxStock(
      symbol = "ASX:CBA",
      ticker = "CBA",
      name = "Commonwealth Bank of Australia",
      sector = "Financials & Banking",
      priceAud = 142.50,
      changePercent = 0.85,
      marketCap = "A$238B",
      description = "Australia's leading provider of integrated financial services, retail, business banking, and institutional banking."
    ),
    AsxStock(
      symbol = "ASX:CSL",
      ticker = "CSL",
      name = "CSL Limited",
      sector = "Healthcare & Biotech",
      priceAud = 295.10,
      changePercent = -0.32,
      marketCap = "A$142B",
      description = "Global biotechnology leader developing biotherapies and influenza vaccines, including CSL Behring and Seqirus."
    ),
    AsxStock(
      symbol = "ASX:NAB",
      ticker = "NAB",
      name = "National Australia Bank",
      sector = "Financials & Banking",
      priceAud = 38.60,
      changePercent = 0.94,
      marketCap = "A$119B",
      description = "One of Australia's big four banks, specializing in business and agribusiness banking and personal banking."
    ),
    AsxStock(
      symbol = "ASX:WBC",
      ticker = "WBC",
      name = "Westpac Banking Corporation",
      sector = "Financials & Banking",
      priceAud = 31.75,
      changePercent = 1.10,
      marketCap = "A$111B",
      description = "Australia's oldest bank and company, providing banking, wealth management, and corporate institutional finance."
    ),
    AsxStock(
      symbol = "ASX:ANZ",
      ticker = "ANZ",
      name = "ANZ Group Holdings Limited",
      sector = "Financials & Banking",
      priceAud = 30.80,
      changePercent = 0.45,
      marketCap = "A$92B",
      description = "Major financial institution with strong presence across Australia, New Zealand, and the Asia-Pacific region."
    ),
    AsxStock(
      symbol = "ASX:WES",
      ticker = "WES",
      name = "Wesfarmers Limited",
      sector = "Consumer Discretionary",
      priceAud = 71.20,
      changePercent = -0.15,
      marketCap = "A$80B",
      description = "Diverse conglomerate owning Bunnings Warehouse, Kmart Group, Officeworks, and industrial chemicals."
    ),
    AsxStock(
      symbol = "ASX:MQG",
      ticker = "MQG",
      name = "Macquarie Group Limited",
      sector = "Investment Banking",
      priceAud = 224.50,
      changePercent = 1.65,
      marketCap = "A$86B",
      description = "Global diversified financial group operating in asset management, commodities, banking, and capital markets."
    ),
    AsxStock(
      symbol = "ASX:RIO",
      ticker = "RIO",
      name = "Rio Tinto Limited",
      sector = "Materials & Mining",
      priceAud = 121.40,
      changePercent = 1.85,
      marketCap = "A$162B",
      description = "Global mining group producing aluminium, iron ore, copper, diamonds, and critical industrial minerals."
    ),
    AsxStock(
      symbol = "ASX:FMG",
      ticker = "FMG",
      name = "Fortescue Limited",
      sector = "Materials & Green Energy",
      priceAud = 19.85,
      changePercent = 2.10,
      marketCap = "A$61B",
      description = "Global green renewables and resources leader, one of the world's lowest-cost iron ore producers."
    ),
    AsxStock(
      symbol = "ASX:WDS",
      ticker = "WDS",
      name = "Woodside Energy Group",
      sector = "Energy & LNG",
      priceAud = 25.90,
      changePercent = -0.40,
      marketCap = "A$49B",
      description = "Australia's premier independent oil and gas exploration and production company, a major global LNG supplier."
    ),
    AsxStock(
      symbol = "ASX:TLS",
      ticker = "TLS",
      name = "Telstra Group Limited",
      sector = "Telecommunications",
      priceAud = 3.92,
      changePercent = 0.26,
      marketCap = "A$45B",
      description = "Australia's largest telecommunications and technology enterprise offering nationwide 5G and mobile services."
    ),
    AsxStock(
      symbol = "ASX:GMG",
      ticker = "GMG",
      name = "Goodman Group",
      sector = "Real Estate & Data Centers",
      priceAud = 36.40,
      changePercent = 1.12,
      marketCap = "A$69B",
      description = "Global commercial and industrial property group developing logistics facilities and AI data center campuses."
    ),
    AsxStock(
      symbol = "ASX:COL",
      ticker = "COL",
      name = "Coles Group Limited",
      sector = "Consumer Staples",
      priceAud = 17.65,
      changePercent = -0.20,
      marketCap = "A$23B",
      description = "Leading supermarket and retail chain providing food, liquor, and convenience products across Australia."
    ),
    AsxStock(
      symbol = "ASX:WOW",
      ticker = "WOW",
      name = "Woolworths Group Limited",
      sector = "Consumer Staples",
      priceAud = 30.15,
      changePercent = 0.35,
      marketCap = "A$36B",
      description = "Australia and New Zealand's largest retail grocer operating Woolworths supermarkets and Big W stores."
    ),
    AsxStock(
      symbol = "ASX:REA",
      ticker = "REA",
      name = "REA Group Limited",
      sector = "Technology & Real Estate",
      priceAud = 215.00,
      changePercent = 1.30,
      marketCap = "A$28B",
      description = "Digital advertising enterprise operating realestate.com.au, Australia's leading residential and commercial property portal."
    ),
    AsxStock(
      symbol = "ASX:WTC",
      ticker = "WTC",
      name = "WiseTech Global Limited",
      sector = "Technology & Logistics",
      priceAud = 128.50,
      changePercent = 2.45,
      marketCap = "A$42B",
      description = "Leading developer and provider of software solutions to the global logistics execution industry (CargoWise)."
    ),
    AsxStock(
      symbol = "ASX:XRO",
      ticker = "XRO",
      name = "Xero Limited",
      sector = "Technology & SaaS",
      priceAud = 152.80,
      changePercent = 1.70,
      marketCap = "A$23B",
      description = "Cloud-based accounting software platform connecting small businesses to their advisors and financial institutions."
    )
  )

  val defaultTickerTapeSymbols = "ASX:XJO,ASX:BHP,ASX:CBA,ASX:CSL,ASX:NAB,ASX:WBC,ASX:ANZ,ASX:WES,ASX:MQG,ASX:RIO,ASX:FMG,ASX:WDS,ASX:TLS,FX_IDC:AUDUSD,BITSTAMP:BTCUSD"
}
