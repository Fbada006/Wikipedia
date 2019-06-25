package com.disruption.wikipedia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.disruption.wikipedia.R
import com.disruption.wikipedia.holders.CardHolder
import com.disruption.wikipedia.holders.ListItemHolder
import com.disruption.wikipedia.models.WikiPage

class ArticleListItemRecyclerAdapter : RecyclerView.Adapter<ListItemHolder>() {
    val currentResults: ArrayList<WikiPage> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val cardItem = LayoutInflater.from(parent.context).
            inflate(R.layout.article_list_item, parent, false)

        return ListItemHolder(cardItem)
    }

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val page = currentResults[position]

        holder.updateWithPage(page)
    }
}