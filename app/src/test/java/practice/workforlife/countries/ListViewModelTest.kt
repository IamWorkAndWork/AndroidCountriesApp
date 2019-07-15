package practice.workforlife.countries

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import practice.workforlife.countries.Model.CountriesService
import practice.workforlife.countries.Model.Country
import practice.workforlife.countries.ViewModel.ListViewModel
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle: Single<List<Country>>? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesSuccess() {
        val country = Country("CountryName", "capital", "url")
        val countriesList = arrayListOf<Country>(country)

        testSingle = Single.just(countriesList)

        val res = countriesService.getCountries()
        `when`(res).thenReturn(testSingle)

        listViewModel.refresh()

        res?.subscribeOn(Schedulers.io())
            ?.subscribe { t1, t2 ->

                println("res = " + t1?.toString())
                println("res2 = " + t2?.toString())

            }

        println("data = " + listViewModel.countries.value)


        Assert.assertEquals(1, listViewModel.countries.value?.size)
        Assert.assertEquals(false, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getCountriesFail() {

        testSingle = Single.error(Throwable())

        `when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(true, listViewModel.countryLoadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)

    }

    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {


            override fun scheduleDirect(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }

        }

        RxJavaPlugins.setInitIoSchedulerHandler { Scheduler ->
            immediate
        }
        RxJavaPlugins.setInitComputationSchedulerHandler { it -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { it -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { it -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { it -> immediate }
    }

}