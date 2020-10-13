package hr.ferit.sumigaborna.dementiahelper.patient.ui.view_holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import hr.ferit.sumigaborna.dementiahelper.R
import kotlinx.android.synthetic.main.cell_daily_weather.view.*
import org.w3c.dom.Text

@EpoxyModelClass(layout = R.layout.cell_daily_weather)
abstract class WeatherHolderModel : EpoxyModelWithHolder<WeatherHolder>(){
    @EpoxyAttribute lateinit var day : String
    @EpoxyAttribute lateinit var date : String
    @EpoxyAttribute lateinit var city : String
    @EpoxyAttribute lateinit var icon :String
    @EpoxyAttribute lateinit var temperature : String
    @EpoxyAttribute lateinit var weatherDescription : String
    override fun bind(holder: WeatherHolder) {
        holder.apply {
            tvDay.text = day
            tvDate.text = date
            tvCity.text = city
            Glide.with(ivWeatherIcon).load(icon).into(ivWeatherIcon)
            tvTemperature.text = temperature
            tvWeatherDescription.text = weatherDescription
        }
    }
}

class WeatherHolder : EpoxyHolder(){
    lateinit var tvDay : TextView
    lateinit var tvDate : TextView
    lateinit var tvCity : TextView
    lateinit var ivWeatherIcon : ImageView
    lateinit var tvTemperature : TextView
    lateinit var tvWeatherDescription : TextView
    override fun bindView(itemView: View) {
        tvDay = itemView.tvDay
        tvDate = itemView.tvDate
        tvCity = itemView.tvCity
        ivWeatherIcon = itemView.ivWeatherIcon
        tvTemperature = itemView.tvTemperature
        tvWeatherDescription = itemView.tvWeatherDescription
    }
}