package com.ninovanhooff.negenwnegenw.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ninovanhooff.negenwnegenw.R


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var searchView: FloatingSearchView

    private val citySearchListener = object : FloatingSearchView.OnSearchListener {
        override fun onSearchAction(currentQuery: String?) {
            viewModel.onCityInputSubmit(currentQuery!!)
            searchView.clearSearchFocus()
        }

        override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {
            viewModel.onCitySuggestionSelected(searchSuggestion as CityModel)
            searchView.clearSearchFocus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        searchView = findViewById(R.id.floating_search_view)

        searchView.setOnQueryChangeListener { _, newQuery ->
            //get suggestions based on newQuery
            viewModel.onCityInputChange(newQuery)
        }

        searchView.setOnSearchListener(citySearchListener)

        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener {
            viewModel.onFabClicked()
        }

        viewModel.cityModel.observe(this, Observer {
            searchView.setSearchText(it.body)
        })

        viewModel.citySuggestions.observe(this, Observer {
            searchView.swapSuggestions(it)
        })
    }

}
