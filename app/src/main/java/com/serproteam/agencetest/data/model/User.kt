package com.serproteam.agencetest.data.model

import java.io.Serializable

class User(
    var userId:String,
    var name: String,
    var lastName:String,
    var email:String,
    var token:String
):Serializable