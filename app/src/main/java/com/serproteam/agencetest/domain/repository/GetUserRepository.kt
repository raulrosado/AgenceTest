package com.serproteam.agencetest.domain.repository

import android.content.Context
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.User

class GetUserRepository {
    fun getUser(context: Context): User {
        var tinyDB = TinyDB(context)
        var user = User(
            tinyDB.getString("userId")!!,
            tinyDB.getString("name")!!,
            tinyDB.getString("lastName")!!,
            tinyDB.getString("email")!!,
            tinyDB.getString("token")!!
        )
        return user
    }
}