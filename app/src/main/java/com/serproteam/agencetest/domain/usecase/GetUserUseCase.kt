package com.serproteam.agencetest.domain.usecase

import android.content.Context
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.User
import com.serproteam.agencetest.domain.repository.GetUserRepository

class GetUserUseCase() {
    fun getUserRepository(context: Context): User {
        return GetUserRepository().getUser(context)
    }
}