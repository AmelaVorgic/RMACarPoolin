package com.example.rma_carpoolin.di

import com.example.rma_carpoolin.repository.RideRepository
import com.example.rma_carpoolin.repository.UserRepository
import com.example.rma_carpoolin.viewmodel.RideViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single {
        UserRepository()
    }
    single {
        RideRepository()
    }
}

val  viewModelModule = module {
    viewModel { RideViewModel(get(), get()) }
}