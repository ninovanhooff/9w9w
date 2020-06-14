package com.ninovanhooff.negenwnegenw.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.ninovanhooff.negenwnegenw.databinding.FragmentTodayBinding
import com.ninovanhooff.negenwnegenw.ui.BaseWeatherFragment

/** Shows a single WeatherForecast */
class TodayFragment : BaseWeatherFragment() {

    private val todayViewModel: TodayViewModel by lazy {
        ViewModelProvider(this).get(TodayViewModel::class.java)
    }
    override fun provideBaseViewModel() = todayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding: FragmentTodayBinding = FragmentTodayBinding.inflate(inflater, container, false)

        todayViewModel.weatherModel.observe(viewLifecycleOwner, Observer {
            binding.today = it

            LottieCompositionFactory.fromRawRes(context, it.animationRawRes).addListener { composition ->
                when(binding.weatherFlipper.displayedChild) {
                    0 -> binding.weatherAnimation1.setComposition(composition)
                    1 -> binding.weatherAnimation0.setComposition(composition)
                    else -> throw NotImplementedError("Unexpected ViewFlipper child index $it")
                }
            }

        })

        bindLottieViews(binding)

        return binding.root
    }

    private fun bindLottieViews(binding: FragmentTodayBinding) {
        val listener: (composition: LottieComposition) -> Unit = {
            binding.weatherFlipper.showNext()
        }
        binding.weatherAnimation0.addLottieOnCompositionLoadedListener(listener)
        binding.weatherAnimation1.addLottieOnCompositionLoadedListener(listener)
    }
}
