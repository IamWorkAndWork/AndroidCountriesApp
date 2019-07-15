package practice.workforlife.countries.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import practice.workforlife.countries.R
import practice.workforlife.countries.ViewModel.ListViewModel

class MainActivity : AppCompatActivity() {

    var viewModel: ListViewModel? = null
    val countriesAdapter = CountryListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel?.refresh()


        val lm = LinearLayoutManager(this)
        countriesListview.apply {
            layoutManager = lm
            adapter = countriesAdapter
        }

        observeViewModel()

        initListener()

    }

    private fun initListener() {

        swipteRefreshLayout?.setOnRefreshListener {
            viewModel?.refresh()
        }

    }

    private fun observeViewModel() {

        viewModel?.countries?.observe(this, Observer {
            it?.let {
                countriesListview?.visibility = View.VISIBLE
                countriesAdapter?.updateCountries(it?.toCollection(arrayListOf()))
                swipteRefreshLayout?.isRefreshing = false
//                Log.e("print", "countries = " + it?.toString())
            }
        })

        viewModel?.countryLoadError?.observe(this, Observer { isError ->
            isError?.let {
                list_error?.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel?.loading?.observe(this, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    list_error.visibility = View.GONE
                    countriesListview.visibility = View.GONE
                }
            }
        })

    }
}
