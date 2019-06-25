package com.disruption.wikipedia.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.disruption.wikipedia.R
import com.disruption.wikipedia.WikiApplication
import com.disruption.wikipedia.managers.WikiManager
import com.disruption.wikipedia.models.WikiPage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_article_detail.*
import org.jetbrains.anko.toast

class ArticleDetailActivity : AppCompatActivity() {
    private var currentPage: WikiPage? = null
    val TAG = "ArticleDetailActivity"

    private var wikiManager: WikiManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        wikiManager = (applicationContext as WikiApplication).wikiManager

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Get page from the extras
        val wikipageJson = intent.getStringExtra("page")

        currentPage = Gson().fromJson<WikiPage>(wikipageJson, WikiPage::class.java)
        Log.e(TAG, "Current page title========== " + currentPage)
        supportActionBar?.title = currentPage?.title

        // Useful handler to make sure we properly load the URL rather than calling over to the WebView
        article_detail_webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return true
            }
        }

        // Take auto-mapped article_detail_webview load the full url of the WikiPage passed in.
        article_detail_webView.loadUrl(currentPage?.fullUrl)

        wikiManager?.addHistory(currentPage!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.article_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_favorite) {
            try {
                if (wikiManager!!.getIsFavorite(currentPage!!.pageId!!)) {
                    wikiManager!!.removeFavorite(currentPage!!.pageId!!)
                    toast("Article has been removed from favorites!")
                } else {
                    wikiManager!!.addFavorite(currentPage!!)
                    toast("Article added to favorites!")
                }
            } catch (e: Exception) {
                toast("Unable to update this article!")
            }

        }
        return true
    }
}