package com.serproteam.agencetest.data.model

import com.serproteam.agencetest.data.model.Product
import java.util.ArrayList

class DataProvider() {
    val dataList = ArrayList<Product>()
    @JvmName("getDataList1")
    fun getDataList():ArrayList<Product> {
        dataList.add(Product(
            1,
            "Hamburguesa",
            "descargacomida",
            "Una breve descripcion de los productos que se ofrecen en esta tienda",
            "100.00"
        ))
        dataList.add(Product(
            2,
            "Fresas con crema",
            "fresitas",
            "Una breve descripcion de los productos que se ofrecen en esta tienda",
            "150.00"
        ))
        dataList.add(Product(
            3,
            "Helados",
            "helados",
            "Una breve descripcion de los productos que se ofrecen en esta tienda",
            "50.00"
        ))
        dataList.add(Product(
            4,
            "Espaguetis",
            "comidas",
            "Una breve descripcion de los productos que se ofrecen en esta tienda",
            "100.00"
        ))
        dataList.add(Product(
            5,
            "Hamburguesa",
            "descargacomida",
            "Una breve descripcion de los productos que se ofrecen en esta tienda",
            "100.00"
        ))
        dataList.add(Product(
            6,
            "Fresas con crema",
            "fresitas",
            "Una breve descripcion de los productos que se ofrecen en esta tienda",
            "150.00"
        ))
        return dataList
    }
}