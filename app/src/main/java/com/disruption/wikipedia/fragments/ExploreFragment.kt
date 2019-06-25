package com.disruption.wikipedia.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.disruption.wikipedia.R
import com.disruption.wikipedia.WikiApplication
import com.disruption.wikipedia.activities.SearchActivity
import com.disruption.wikipedia.adapters.ArticleCardRecyclerAdapter
import com.disruption.wikipedia.managers.WikiManager

/**
 * A simple [Fragment] subclass.
 *
 */
class ExploreFragment : Fragment() {

    private var wikiManager: WikiManager? = null

    private var searchCardView: CardView? = null
    var exploreRecycler: RecyclerView? = null
    var adapter = ArticleCardRecyclerAdapter()
    private var refresher: SwipeRefreshLayout? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        wikiManager = (activity?.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        searchCardView = view.findViewById(R.id.search_card_view)
        exploreRecycler = view.findViewById(R.id.explore_articles_recycler)
        refresher = view.findViewById(R.id.refresher)

        searchCardView!!.setOnClickListener {
            context?.startActivity(Intent(context, SearchActivity::class.java))
        }

        // Listener for when pull-to-refresh is invoked.
        refresher?.setOnRefreshListener {
            getRandomArticles()
        }

        exploreRecycler!!.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)

        exploreRecycler!!.adapter = adapter

        getRandomArticles()

        // Inflate the layout for this fragment
        return view
    }

    private fun getRandomArticles() {
        refresher?.isRefreshing = true
        try {
            wikiManager?.getRandom(15) {
                //Do something with the results
                adapter.currentResults.clear()
                adapter.currentResults.addAll(it.query!!.pages)
                activity?.runOnUiThread {
                    adapter.notifyDataSetChanged()
                    refresher?.isRefreshing = false
                }
            }
        } catch (e: Exception) {
            // Show Alert
            val builder = activity?.let { AlertDialog.Builder(it) }
            builder?.setMessage(e.message)?.setTitle("oops!")

            val dialog = builder?.create()
            dialog?.show()
        }
    }

}
