package com.sgo.SmyrnaGlobalOutreach

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button

class Donation : AppCompatActivity() {

    private lateinit var btnDonate: Button
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation)

        btnDonate = findViewById(R.id.btnDonate)
        webView = findViewById(R.id.webView)

        webView.webViewClient = MyWebViewClient()

        // Enable JavaScript (if required)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Set an OnClickListener for the btnDonate button
        btnDonate.setOnClickListener {
            DonateButton()
        }
    }

    private fun DonateButton() {
        val url = "https://www.paypal.com/donate/?hosted_button_id=VYR4EWX8RY6CY"
        webView.loadUrl(url)

        // Make the WebView visible when the button is clicked
        webView.visibility = View.VISIBLE
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (url != null) {
                view?.loadUrl(url)
            }
            return true
        }
    }
}
