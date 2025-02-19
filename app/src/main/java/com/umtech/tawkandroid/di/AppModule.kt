package com.umtech.tawkandroid.di

import com.umtech.tawkandroid.BuildConfig
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
            level = HttpLoggingInterceptor.Level.BODY // Logs request & response body
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // Attach OkHttpClient with logging enabled
            .build()
            .create(ApiService::class.java)
    }
    single { RemoteDataSource(get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single { FetchUserUseCase(get()) }
    viewModel { MainViewModel(get()) }
}