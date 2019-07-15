package practice.workforlife.countries.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import practice.workforlife.countries.Model.CountriesService
import practice.workforlife.countries.Model.Country
import practice.workforlife.countries.di.DaggerApiComponent
import javax.inject.Inject

class ListViewModel : ViewModel() {

    //        private val countriesService: CountriesService? = CountriesService()
    @Inject
    lateinit var countriesService: CountriesService


    private val disposable: CompositeDisposable? = CompositeDisposable()

    val countries = MutableLiveData<List<Country?>?>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun refresh() {
        fetchCountries()
    }

    private fun fetchCountries() {

        loading?.value = true

        disposable?.add(

            countriesService?.getCountries()
                ?.subscribeOn(Schedulers.newThread())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(value: List<Country>) {
                        countries?.value = value
                        countryLoadError?.value = false
                        loading?.value = false
                    }

                    override fun onError(e: Throwable) {
                        countryLoadError?.value = true
                        loading?.value = false
                    }

                })!!
        )


//        countryLoadError.value = false
//        loading.value = false
//        countries.value = mockData
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.clear()
    }

}