package com.tatyanashkolnik.studentassistantkotlin.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import com.tatyanashkolnik.studentassistantkotlin.Constants
import com.tatyanashkolnik.studentassistantkotlin.R

class DiaryFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var webView: WebView

    private lateinit var btnMos: Button
    private lateinit var btnSpb: Button
    private lateinit var btnDnevnik: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_diary, container, false)


        initViews(rootView)

        initListeners()

        return rootView
    }

    fun initViews(rootView: View){
        btnMos = rootView.findViewById(R.id.mos)
        btnSpb = rootView.findViewById(R.id.spb)
        btnDnevnik = rootView.findViewById(R.id.dnevnik)
        webView = rootView.findViewById(R.id.webview)
    }

    fun initListeners(){
        btnMos.setOnClickListener {
            initWebView(Constants.MOSRU)
        }
        btnSpb.setOnClickListener {
            initWebView(Constants.SPB)
        }
        btnDnevnik.setOnClickListener {
            initWebView(Constants.DNEVNIK)
        }
    }

    fun initWebView(site: String){
        changeVisibility()
        webView.settings.javaScriptEnabled = true
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        // webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.setSupportZoom(true)
        webSettings.defaultTextEncodingName = "utf-8"
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webView.loadUrl(site)
    }

    fun changeVisibility(){
        btnMos.visibility = View.INVISIBLE
        btnSpb.visibility = View.INVISIBLE
        btnDnevnik.visibility = View.INVISIBLE
        webView.visibility = View.VISIBLE
    }
}
