package com.example.rma_carpoolin

import android.app.Application
import com.example.rma_carpoolin.di.repositoryModule
import com.example.rma_carpoolin.di.viewModelModule
import org.koin.android.ext.koin.androidLogger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CarPoolin: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(
                if(BuildConfig.DEBUG)
                    Level.ERROR
                else
                    Level.NONE
            )
            androidContext(this@CarPoolin)
            modules(
                repositoryModule,
                viewModelModule
            )
        }
    }
}