package com.ninovanhooff.negenwnegenw.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer

abstract class BaseWeatherFragment: Fragment() {

    abstract fun provideBaseViewModel() : BaseWeatherViewModel

    /** Base implementation will return null, so must be overridden */
    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        provideBaseViewModel().errorMessages.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandledOrReturnNull()?.let { text ->
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        })

        return null
    }
}