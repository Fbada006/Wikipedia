package com.disruption.wikipedia.providers

import android.util.Log
import com.disruption.wikipedia.models.Urls
import com.disruption.wikipedia.models.WikiResult
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import java.io.Reader

class ArticleDataProvider {
    val TAG = "ArticleDataProvider"

    //Setup the user agent header
    init {
        FuelManager.instance.baseHeaders = mapOf("User-Agent" to "The Real Monster 2034")
    }

    // Search Function
    fun search(term: String, skip: Int, take: Int, responseHandler: (result: WikiResult) -> Unit?) {
        Urls.getSearchUrl(term, skip, take).httpGet()
            .responseObject(WikipediaDataDeserializer()) { _, response, result ->
                Log.e(TAG, "Search Url ================= " + Urls.getSearchUrl(term, skip, take))
                // Error Handling
                if (response.statusCode != 200) {
                    throw Exception("Unable to GET articles")
                }

                // Calling our response handler
                val (data, _) = result // This is a Kotlin pair object containing data and error
                responseHandler.invoke(data as WikiResult)
            }
    }

    fun getRandom(take: Int, responseHandler: (result: WikiResult) -> Unit?) {
        Urls.getRandomUrl(take).httpGet()
            .responseObject(WikipediaDataDeserializer()) { _, response, result ->
                Log.e(TAG, "Random Url ================= " + Urls.getRandomUrl(15))

                // Error Handling
                if (response.statusCode != 200) {
                    throw Exception("Unable to GET articles")
                }

                val (data, _) = result
                responseHandler.invoke(data as WikiResult)
            }
    }

    /*
      Subclass for the Wikipedia Data Deserializer.
      (i) Allows Fuel to know what its deserializing the data with & what its using to do it.
     (ii) We create a deserializer that's going to use Gson to pass the String from
     the Reader down to the object that we need. <WikiResult>
   */
    class WikipediaDataDeserializer : ResponseDeserializable<WikiResult> {
        override fun deserialize(reader: Reader): WikiResult =
            Gson().fromJson(reader, WikiResult::class.java)
    }
}