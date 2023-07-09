package com.example.quizwithjetpack.model

import com.google.gson.annotations.SerializedName

/**
 * Created by kannanpvm007 on 09-07-2023.
 */
class  QuizList : ArrayList<QuizMbo>()

data class QuizMbo (
    @SerializedName("question" ) var question : String?           = null,
    @SerializedName("category" ) var category : String?           = null,
    @SerializedName("answer"   ) var answer   : String?           = null,
    @SerializedName("choices"  ) var choices  : ArrayList<String> = arrayListOf()
        )