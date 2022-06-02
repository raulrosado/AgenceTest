package com.serproteam.agencetest.domain.usecase

import android.content.Context
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.User
import com.serproteam.agencetest.domain.repository.GetUserRepository
import com.serproteam.agencetest.domain.repository.InsertUserRepository

class InsertUserUseCase() {
    fun insertUserRepository(context: Context,user: User){
        return InsertUserRepository().insertUser(context,user)
    }
}