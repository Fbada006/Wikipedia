package com.disruption.wikipedia.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.disruption.wikipedia.R
import com.disruption.wikipedia.WikiApplication
import com.disruption.wikipedia.adapters.ArticleListItemRecyclerAdapter
import com.disruption.wikipedia.managers.WikiManager
import com.disruption.wikipedia.models.WikiPage
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.noButton
import org.jetbrains.anko.yesButton

/**
 * A simple [Fragment] subclass.
 *
 */
class HistoryFragment : Fragment() {
    var historyRecycler: RecyclerView? = null
    private var wikiManager: WikiManager? = null
    private val adapter: ArticleListItemRecyclerAdapter = ArticleListItemRecyclerAdapter()

    init {
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        wikiManager = (activity?.applicationContext as WikiApplication).wikiManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        historyRecycler = view.findViewById(R.id.history_article_recycler)

        historyRecycler!!.layoutManager = LinearLayoutManager(context)
        historyRecycler!!.adapter = adapter

        // Inflate the layout for this fragment
        return view
    }

    override fun onResume() {
        super.onResume()

        doAsync {
            val historyArticles = wikiManager!!.getHistory()
            adapter.currentResults.clear()
            adapter.currentResults.addAll(historyArticles as ArrayList<WikiPage>)
            activity?.runOnUiThread { adapter.notifyDataSetChanged() }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_clear_history) {
            activity?.alert("Are you sure you want to clear history?", "Confirm") {
                yesButton {
                    adapter.currentResults.clear()
                    doAsync {
                        wikiManager?.clearHistory()
                    }
                    activity?.runOnUiThread { adapter.notifyDataSetChanged() }
                }
                noButton { }
            }?.show()
        }
        return true
    }
}
