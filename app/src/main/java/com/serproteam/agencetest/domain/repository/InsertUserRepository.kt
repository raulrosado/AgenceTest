package com.serproteam.agencetest.domain.repository

import android.content.Context
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.User

class InsertUserRepository {
    fun insertUser(context: Context, user: User) {
        var tinyDB = TinyDB(context)
        tinyDB.putString("userId",user.userId)
        tinyDB.putString("name",user.name)
        tinyDB.putString("lastName",user.lastName)
        tinyDB.putString("email",user.email)
        tinyDB.putString("token",user.token)
    }
}