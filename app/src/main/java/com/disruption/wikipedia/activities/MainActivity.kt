package com.disruption.wikipedia.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.disruption.wikipedia.R
import com.disruption.wikipedia.fragments.ExploreFragment
import com.disruption.wikipedia.fragments.FavoriteFragment
import com.disruption.wikipedia.fragments.HistoryFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_article_detail.*

class MainActivity : AppCompatActivity() {

    private val exploreFragment = ExploreFragment()
    private val favoriteFragment = FavoriteFragment()
    private val historyFragment = HistoryFragment()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val container = R.id.fragment_container
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)

            when (item.itemId) {
                R.id.navigation_explore -> transaction.replace(container, exploreFragment)
                R.id.navigation_favorite -> transaction.replace(container, favoriteFragment)
                R.id.navigation_history -> transaction.replace(container, historyFragment)
            }

            transaction.commit()

            true
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, exploreFragment)
        transaction.commit()
    }
}
