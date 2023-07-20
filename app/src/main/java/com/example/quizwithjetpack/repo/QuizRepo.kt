package com.example.quizwithjetpack.repo

import android.util.Log
import com.example.quizwithjetpack.data.DataOrException
import com.example.quizwithjetpack.model.QuizList
import com.example.quizwithjetpack.network.RetrofitApiService
import javax.inject.Inject

/**
 * Created by kannanpvm007 on 09-07-2023.
 */
class QuizRepo @Inject constructor(private  val api:RetrofitApiService) {

    private val dataOrException= DataOrException<QuizList,Boolean,Exception>()

    suspend fun getAllQustions(): DataOrException<QuizList, Boolean, Exception> {

        try {
            dataOrException.loading = true
            dataOrException.data=api.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading =false
        }catch (e:Exception){
            dataOrException.e=e
            Log.d("TAG", "getAllQustions: ${dataOrException.e!!.localizedMessage}")

        }
        return dataOrException
    }
}