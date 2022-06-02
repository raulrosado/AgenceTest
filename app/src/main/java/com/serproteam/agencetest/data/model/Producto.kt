package com.serproteam.agencetest.data.model

import java.io.Serializable

class Product(
    var id: Int,
    var name: String,
    var image:String,
    var description:String,
    var price:String
):Serializable