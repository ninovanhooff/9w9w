package com.ninovanhooff.negenwnegenw.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ninovanhooff.negenwnegenw.R

class TodayFragment : Fragment() {

    private lateinit var todayViewModel: TodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todayViewModel = ViewModelProvider(this).get(TodayViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_today, container, false)
        val temperatureView: TextView = root.findViewById(R.id.text_temperature)
        todayViewModel.text.observe(viewLifecycleOwner, Observer {
            temperatureView.text = it
        })
        return root
    }
}
