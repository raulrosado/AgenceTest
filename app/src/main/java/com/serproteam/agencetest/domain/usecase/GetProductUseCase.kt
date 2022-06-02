package com.serproteam.agencetest.domain.usecase

import com.serproteam.agencetest.data.model.DataProvider
import com.serproteam.agencetest.data.model.Product
import java.util.ArrayList

class GetProductUseCase() {
    fun getProduct(): ArrayList<Product> {
        return DataProvider().getDataList()
    }
}