package com.serproteam.agencetest.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serproteam.agencetest.data.model.Product
import com.serproteam.agencetest.data.model.User
import com.serproteam.agencetest.domain.usecase.DeleteUserUseCase
import com.serproteam.agencetest.domain.usecase.GetProductUseCase
import com.serproteam.agencetest.domain.usecase.GetUserUseCase
import com.serproteam.agencetest.domain.usecase.InsertUserUseCase

class UserViewModel : ViewModel() {

    fun getUser(context: Context):User{
        return GetUserUseCase().getUserRepository(context)
    }

    fun insertUser(context: Context,user: User){
        return InsertUserUseCase().insertUserRepository(context,user)
    }

    fun delUser(context: Context){
        return DeleteUserUseCase().deleteUserRepository(context)
    }


}