package com.disruption.wikipedia.repositories

import com.disruption.wikipedia.models.WikiPage
import com.disruption.wikipedia.models.WikiThumbnail
import com.google.gson.Gson
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select

class HistoryRepository(val databaseOpenHelper: ArticleDatabaseOpenHelper) {
    private val TABLE_NAME = "History"

    fun addHistory(page: WikiPage) {
        databaseOpenHelper.use {
            insert(
                TABLE_NAME,
                "id" to page.pageId,
                "title" to page.title,
                "url" to page.fullUrl,
                "thumbnail" to Gson().toJson(page.thumbnail)
            )
        }
    }

    fun removePageById(pageId: Int) {
        databaseOpenHelper.use {
            delete(TABLE_NAME, "id = {pageId}", "pageId" to pageId)
        }
    }

     fun getAllHistory(): ArrayList<WikiPage> {
        val pages = ArrayList<WikiPage>()

        val articleRowParser = rowParser { id: Int, title: String, url: String, thumbnailJson: String ->
            val page = WikiPage()
            page.title = title
            page.pageId = id
            page.fullUrl = url
            page.thumbnail = Gson().fromJson(thumbnailJson, WikiThumbnail::class.java)

            pages.add(page)
        }

         databaseOpenHelper.use {
             select(TABLE_NAME).parseList(articleRowParser)
         }

        return pages;
    }
}