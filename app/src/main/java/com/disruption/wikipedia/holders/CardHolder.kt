package com.disruption.wikipedia.holders

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.disruption.wikipedia.R
import com.disruption.wikipedia.activities.ArticleDetailActivity
import com.disruption.wikipedia.models.WikiPage
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val articleImageView = itemView.findViewById<ImageView>(R.id.article_image)
    private val articleTitle = itemView.findViewById<TextView>(R.id.article_title)

    // Update ViewHolder with Data - Set WikiPage associated with it at the time of rendering.
    private var currentPage: WikiPage? = null

    // ClickHandler -> In order to pass data to the DetailArticleActivity
    init {
        itemView.setOnClickListener { view: View? ->
            // Build intent for ArticleDetailActivity & Gson string to the Extras for the Intent
            val detailPageIntent = Intent(itemView.context, ArticleDetailActivity::class.java)
            val pageJson = Gson().toJson(currentPage)
            detailPageIntent.putExtra("page", pageJson)
            itemView.context.startActivity(detailPageIntent)
        }
    }

    // Set the current page & update the data within the View.
    fun updateWithPage(page: WikiPage) {
        currentPage = page

        // Set titleTextView text property to title of the WikiPage Model
        articleTitle.text = page.title

        // Load image lazily with Picasso from a URL into an ImageView for the Thumbnail
        if (page.thumbnail != null)
            Picasso.get().load(page.thumbnail!!.source).into(articleImageView)
    }
}