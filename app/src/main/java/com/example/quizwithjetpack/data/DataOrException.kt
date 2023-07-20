package com.example.quizwithjetpack.data

/**
 * Created by kannanpvm007 on 09-07-2023.
 */
data class DataOrException<T, Boolean, E : Exception>(
    var data: T? = null,
    var loading: Boolean? = null,
    var e: E? = null
)