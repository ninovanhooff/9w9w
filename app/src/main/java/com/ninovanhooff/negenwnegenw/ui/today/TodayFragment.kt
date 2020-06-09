package com.ninovanhooff.negenwnegenw.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ninovanhooff.negenwnegenw.databinding.FragmentTodayBinding

class TodayFragment : Fragment() {

    private lateinit var todayViewModel: TodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        todayViewModel = ViewModelProvider(this).get(TodayViewModel::class.java)

        val binding: FragmentTodayBinding = FragmentTodayBinding.inflate(inflater, container, false)

        todayViewModel.todayModel.observe(viewLifecycleOwner, Observer {
            binding.today = it
        })

        return binding.root
    }
}
