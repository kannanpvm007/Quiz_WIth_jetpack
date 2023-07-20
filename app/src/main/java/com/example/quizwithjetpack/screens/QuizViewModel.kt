package com.example.quizwithjetpack.screens

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizwithjetpack.data.DataOrException
import com.example.quizwithjetpack.model.QuizList
import com.example.quizwithjetpack.repo.QuizRepo
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kannanpvm007 on 10-07-2023.
 */
@HiltViewModel
class QuizViewModel @Inject constructor(private val repo: QuizRepo):ViewModel() {

      val data : MutableState<DataOrException<QuizList, Boolean, Exception>> =
        mutableStateOf(DataOrException(null, true, Exception("")))

    init {
        getAllQuestions()
    }
    private fun getAllQuestions(){
        viewModelScope.launch {
            data.value.loading = true
            data.value = repo.getAllQustions()
            Log.d("TAG", "getAllQuestions: data.value---------------> "+ data.value)
            if (data.value.data.toString().isNotEmpty()){
                data.value.loading= false
            }
        }
    }
}