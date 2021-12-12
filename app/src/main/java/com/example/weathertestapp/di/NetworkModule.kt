package com.example.weathertestapp.di

import android.content.Context
import com.example.weathertestapp.BuildConfig
import com.example.weathertestapp.data.network.ApiDataSource
import com.example.weathertestapp.data.network.ApiService
import com.example.weathertestapp.data.repository.WeatherRepository
import com.example.weathertestapp.data.repository.WeatherRepositoryImpl
import com.example.weathertestapp.utils.HereMapsUrls
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        return logging
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext appContext: Context,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(HereMapsUrls.BASE_WEATHER_FORECASTS_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiDataSource(apiService: ApiService) : ApiDataSource {
        return ApiDataSource(apiService)
    }

    @Singleton
    @Provides
    fun provideWeatherRepository(
        apiDataSource: ApiDataSource,
    ): WeatherRepository = WeatherRepositoryImpl(apiDataSource)
}