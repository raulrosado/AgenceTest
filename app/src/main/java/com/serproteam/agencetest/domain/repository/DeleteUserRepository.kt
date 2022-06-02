package com.serproteam.agencetest.domain.repository

import android.content.Context
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.User

class DeleteUserRepository {
    fun delUser(context: Context){
        var tinyDB = TinyDB(context)
        tinyDB.remove("userId")
        tinyDB.remove("name")
        tinyDB.remove("lastName")
        tinyDB.remove("email")
        tinyDB.remove("token")
    }
}