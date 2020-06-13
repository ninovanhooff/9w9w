package com.ninovanhooff.negenwnegenw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arlib.floatingsearchview.FloatingSearchView
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        val searchView : FloatingSearchView = findViewById(R.id.floating_search_view)

        searchView.setOnQueryChangeListener { _, newQuery -> //get suggestions based on newQuery
            viewModel.onCityInputChange(newQuery)
        }

        viewModel.citySuggestions.observe(this, Observer {
            searchView.swapSuggestions(it)
        })
    }


}
