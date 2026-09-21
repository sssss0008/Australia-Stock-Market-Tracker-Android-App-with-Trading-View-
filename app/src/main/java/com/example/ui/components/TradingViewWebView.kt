package com.example.ui.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun TradingViewWebView(
  htmlContent: String,
  modifier: Modifier = Modifier,
  backgroundColor: Color = MaterialTheme.colorScheme.background,
  testTag: String = "tradingview_webview",
  hardwareAccelerated: Boolean = true
) {
  var isLoading by remember { mutableStateOf(true) }
  var hasError by remember { mutableStateOf(false) }
  var webViewKey by remember { mutableStateOf(0) }
  var webViewRef by remember { mutableStateOf<WebView?>(null) }

  // Ensure loading spinner automatically clears quickly so it never blocks touch
  LaunchedEffect(htmlContent, webViewKey) {
    isLoading = true
    delay(2000)
    isLoading = false
  }

  val bgHex = remember(backgroundColor) {
    String.format("#%06X", 0xFFFFFF and backgroundColor.value.toInt())
  }

  val finalHtml = remember(htmlContent, bgHex) {
    wrapInHtmlShell(htmlContent, bgHex)
  }

  Box(
    modifier = modifier
      .background(backgroundColor)
      .testTag(testTag)
  ) {
    androidx.compose.runtime.key(webViewKey) {
      AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
          WebView(context).apply {
            // Enable touch, click, and gesture interactions
            isClickable = true
            isFocusable = true
            isFocusableInTouchMode = true
            isNestedScrollingEnabled = true

            // Standard NONE layer type allows normal GPU rendering without offscreen FBO rendernode allocation
            setLayerType(View.LAYER_TYPE_NONE, null)

            settings.apply {
              javaScriptEnabled = true
              domStorageEnabled = true
              databaseEnabled = true
              useWideViewPort = true
              loadWithOverviewMode = true
              cacheMode = WebSettings.LOAD_DEFAULT
              allowContentAccess = true
              allowFileAccess = false
              mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
              setSupportZoom(true)
              builtInZoomControls = true
              displayZoomControls = false
              userAgentString = settings.userAgentString + " AustraliaMarketApp/1.0"
            }
            setBackgroundColor(android.graphics.Color.TRANSPARENT)

            webViewClient = object : WebViewClient() {
              override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                isLoading = true
                hasError = false
              }

              override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isLoading = false
              }

              override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
              ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true && error?.errorCode != -1) {
                  hasError = true
                  isLoading = false
                }
              }

              override fun onRenderProcessGone(
                view: WebView?,
                detail: android.webkit.RenderProcessGoneDetail?
              ): Boolean {
                // Safely destroy and detach crashed WebView without killing the host app
                try {
                  view?.stopLoading()
                  (view?.parent as? android.view.ViewGroup)?.removeView(view)
                  view?.destroy()
                } catch (_: Throwable) {}
                webViewRef = null
                hasError = true
                isLoading = false
                return true
              }
            }

            webChromeClient = WebChromeClient()
            loadDataWithBaseURL("https://www.tradingview.com", finalHtml, "text/html", "UTF-8", null)
            webViewRef = this
          }
        },
        update = { webView ->
          if (webView.tag != finalHtml) {
            webView.tag = finalHtml
            isLoading = true
            hasError = false
            webView.loadDataWithBaseURL(
              "https://www.tradingview.com",
              finalHtml,
              "text/html",
              "UTF-8",
              null
            )
          }
        }
      )
    }

    // Animated Loading Spinner (non-blocking)
    if (isLoading) {
      Box(
        modifier = Modifier
          .align(Alignment.Center)
          .size(48.dp)
          .background(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
            shape = MaterialTheme.shapes.medium
          ),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(26.dp),
          strokeWidth = 2.5.dp
        )
      }
    }

    // Error retry UI
    if (hasError) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .background(backgroundColor)
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
      ) {
        Text(
          text = "Unable to load market widget",
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Tap retry to refresh market feed",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        ElevatedButton(
          onClick = {
            hasError = false
            isLoading = true
            // Increment webViewKey to instantiate a clean, new WebView instance safely
            webViewKey++
          },
          colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          )
        ) {
          Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.size(6.dp))
          Text("Retry")
        }
      }
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      webViewRef?.let { wv ->
        try {
          wv.stopLoading()
          (wv.parent as? android.view.ViewGroup)?.removeView(wv)
          wv.destroy()
        } catch (_: Throwable) {}
      }
      webViewRef = null
    }
  }
}

private fun wrapInHtmlShell(content: String, bgHex: String): String {
  return """
    <!DOCTYPE html>
    <html style="width:100%;height:100%;margin:0;padding:0;background-color:$bgHex;">
    <head>
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=2.0, user-scalable=yes">
      <style>
        * { box-sizing: border-box; }
        html, body {
          margin: 0;
          padding: 0;
          width: 100%;
          height: 100%;
          background-color: $bgHex;
          color: #ffffff;
          font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
          overflow-x: hidden;
        }
        .tradingview-widget-container {
          width: 100% !important;
          height: 100% !important;
        }
        .tradingview-widget-copyright {
          display: none !important;
        }
      </style>
    </head>
    <body style="background-color:$bgHex;">
      $content
    </body>
    </html>
  """.trimIndent()
}
