package com.example.quizwithjetpack.network

import com.example.quizwithjetpack.model.QuizList
import retrofit2.http.GET
import javax.inject.Singleton

/**
 * Created by kannanpvm007 on 09-07-2023.
 */
@Singleton
interface RetrofitApiService {
    @GET("kannanpvm007/Quiz_WIth_jetpack/master/app/src/main/java/com/example/quizwithjetpack/josn/Quiz.json")
    suspend fun getAllQuestions() : QuizList
}