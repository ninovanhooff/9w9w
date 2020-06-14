package com.ninovanhooff.negenwnegenw.ui.forecast

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ninovanhooff.negenwnegenw.R
import com.ninovanhooff.negenwnegenw.ui.BaseWeatherFragment
import com.ninovanhooff.negenwnegenw.ui.BaseWeatherViewModel

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ForecastFragment.OnListFragmentInteractionListener] interface.
 */
class ForecastFragment : BaseWeatherFragment() {

    private val forecastItems: MutableList<ForecastDoubleItem> = mutableListOf()

    private var listener: OnListFragmentInteractionListener? = null

    private val forecastViewModel: ForecastViewModel by lazy {
        ViewModelProvider(this).get(ForecastViewModel::class.java)
    }
    override fun provideBaseViewModel(): BaseWeatherViewModel = forecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val recycler: RecyclerView = inflater.inflate(R.layout.fragment_forecast, container, false) as RecyclerView
        // Configures the snap helper and attaches itself to the recycler view -- now items will snap to the center
        LinearSnapHelper().attachToRecyclerView(recycler)


        // Set the adapter
        val adapter = ForecastRecyclerViewAdapter(forecastItems, listener)
        recycler.adapter = adapter

        forecastViewModel.forecastPages.observe(viewLifecycleOwner, Observer {
            forecastItems.clear()
            forecastItems.addAll(it)
            adapter.notifyDataSetChanged()
            recycler.smoothScrollToPosition(0)
        })

        return recycler
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: ForecastDoubleItem?)
    }

}
