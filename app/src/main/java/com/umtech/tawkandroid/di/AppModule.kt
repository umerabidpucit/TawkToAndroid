package com.umtech.tawkandroid.di

import com.umtech.tawkandroid.BuildConfig
import com.umtech.tawkandroid.data.AppDatabase
import com.umtech.tawkandroid.data.api.ApiService
import com.umtech.tawkandroid.data.remote.RemoteDataSource
import com.umtech.tawkandroid.data.repository.UserRepository
import com.umtech.tawkandroid.data.repository.UserRepositoryImpl
import com.umtech.tawkandroid.domain.usecase.FetchUserUseCase
import com.umtech.tawkandroid.presentation.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // Ensure `BASE_URL` is correctly set
            .addConverterFactory(GsonConverterFactory.create())
            .client(get()) // Use the provided OkHttpClient
            .build()
    }
    single { get<Retrofit>().create(ApiService::class.java) }
    single { AppDatabase.getDatabase(get()).userDao() } // Provide UserDao
    single { RemoteDataSource(get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single { FetchUserUseCase(get()) } // Provide UseCase
    viewModel { MainViewModel(get()) } // Provide ViewModel
}