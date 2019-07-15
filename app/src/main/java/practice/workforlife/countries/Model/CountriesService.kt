package practice.workforlife.countries.Model

import io.reactivex.Single
import practice.workforlife.countries.di.DaggerApiComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject

class CountriesService {

    //    https://raw.githubusercontent.com/DevTides/countries/master/countriesV2.json
//    private val BASE_URL = "https://raw.githubusercontent.com/"

    @Inject
    lateinit var api: CountriesApi

    init {

        DaggerApiComponent.create().inject(this)

    }


    fun getCountries(): Single<List<Country>> {
        return api.getCountries()
    }

}