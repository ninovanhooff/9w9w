package com.ninovanhooff.negenwnegenw.ui.forecast


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.ninovanhooff.negenwnegenw.R
import com.ninovanhooff.negenwnegenw.ui.forecast.ForecastFragment.OnListFragmentInteractionListener
import kotlinx.android.synthetic.main.forecast_double_item.view.*
import kotlinx.android.synthetic.main.forecast_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [ForecastDoubleItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class ForecastRecyclerViewAdapter(
    private val values: List<ForecastDoubleItem>,
    private val listener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<ForecastRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as ForecastDoubleItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            listener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_double_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.leftTemperature.text = item.left.temp
        holder.leftTemperatureUnit.text = item.left.tempUnit
        holder.leftTemperatureMinMax.text = item.left.tempMinMax
        holder.leftWeatherDescription.text = item.left.weatherDescription

        LottieCompositionFactory.fromRawRes(holder.itemView.context, item.left.animationRawRes)
            .addListener { composition ->
                holder.leftAnimation.setComposition(composition)
                holder.leftAnimation.playAnimation()
            }


        holder.rightTemperature.text = item.right.temp
        holder.rightTemperatureUnit.text = item.right.tempUnit
        holder.rightTemperatureMinMax.text = item.right.tempMinMax
        holder.rightWeatherDescription.text = item.right.weatherDescription

        LottieCompositionFactory.fromRawRes(holder.itemView.context, item.right.animationRawRes)
            .addListener { composition ->
                holder.rightAnimation.setComposition(composition)
                holder.rightAnimation.playAnimation()
            }
        

        with(holder.mView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private val leftItemView: View = mView.item_left
        val leftTemperature: TextView = leftItemView.text_temperature
        val leftTemperatureUnit: TextView = leftItemView.text_temperature_unit
        val leftTemperatureMinMax: TextView = leftItemView.text_temp_min_max
        val leftWeatherDescription: TextView = leftItemView.text_weather_description
        val leftAnimation: LottieAnimationView = leftItemView.weather_animation
        private val rightItemView: View = mView.item_right
        val rightTemperature: TextView = rightItemView.text_temperature
        val rightTemperatureUnit: TextView = rightItemView.text_temperature_unit
        val rightTemperatureMinMax: TextView = rightItemView.text_temp_min_max
        val rightWeatherDescription: TextView = rightItemView.text_weather_description
        val rightAnimation: LottieAnimationView = rightItemView.weather_animation

        override fun toString(): String {
            return super.toString() + " '" + leftTemperature.text + "'"
        }
    }
}
