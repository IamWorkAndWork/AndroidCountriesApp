package practice.workforlife.countries.View

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_country.view.*
import practice.workforlife.countries.Model.Country
import practice.workforlife.countries.R
import practice.workforlife.countries.utils.getProgressDrawable
import practice.workforlife.countries.utils.loadImage

class CountryListAdapter(var countries: ArrayList<Country?>) :
    RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {

    fun updateCountries(newCountries: ArrayList<Country?>) {
        countries?.clear()
        countries?.addAll(newCountries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false);
        return CountryViewHolder(v)
    }

    override fun getItemCount(): Int {
        return countries?.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        countries?.get(position)?.let { holder.bind(it) }
    }


    inner class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val countryName = view?.name
        private val capital = view?.capital
        private val imageView = view?.imageView
        private val progressDrawable = getProgressDrawable(view.context)

        fun bind(country: Country) {

            countryName?.text = country?.countryName
            capital?.text = country?.capital

            imageView.loadImage(country.flag, progressDrawable)

        }
    }


}