package com.example.rma_carpoolin.model

data class User(
    var id: String = "",
    val email: String,
    val password:String,
    var picture: String = ""
) {

}