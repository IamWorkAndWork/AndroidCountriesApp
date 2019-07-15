package practice.workforlife.countries.di

import dagger.Component
import practice.workforlife.countries.Model.CountriesService
import practice.workforlife.countries.ViewModel.ListViewModel


@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: CountriesService)

    fun inject(viewModel: ListViewModel)

}