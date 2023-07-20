package com.example.quizwithjetpack.di

import com.example.quizwithjetpack.model.QuizList
import com.example.quizwithjetpack.network.RetrofitApiService
import com.example.quizwithjetpack.repo.QuizRepo
import com.example.quizwithjetpack.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

/**
 * Created by kannanpvm007 on 09-07-2023.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQustionRepository(api: RetrofitApiService)=QuizRepo(api)

    @Singleton
    @Provides
    fun providesQuizeApi(): RetrofitApiService {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitApiService::class.java)
    }
}