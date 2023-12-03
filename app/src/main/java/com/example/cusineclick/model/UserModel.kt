package com.example.cusineclick.model

data class UserModel(
    var userId:String? = null,
    var name: String? =null,
    val email: String? =null,
    val password: String? =null,
    var location:String?=null,
    var city:String?=null,
    val imgUri:String?=null
)
