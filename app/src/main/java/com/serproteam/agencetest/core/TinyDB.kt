package com.serproteam.agencetest.core

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class TinyDB(appContext: Context) {
    var context = appContext

    private var preferences: SharedPreferences? =
        context.getSharedPreferences("agence", Context.MODE_PRIVATE)

    fun getInt(key: String?): Int {
        return preferences!!.getInt(key, 0)
    }

    fun getInt(key: String?, inicial: Int): Int {
        return preferences!!.getInt(key, inicial)
    }

    fun getLong(key: String?, defaultValue: Long): Long {
        return preferences!!.getLong(key, defaultValue)
    }

    fun getFloat(key: String?): Float {
        return preferences!!.getFloat(key, 0f)
    }

    fun getString(key: String?): String? {
        return preferences!!.getString(key, "")
    }

    fun getString(key: String?, inicial: String): String? {
        return preferences!!.getString(key, inicial)
    }

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    open fun getListString(key: String?): ArrayList<String?>? {
        return TextUtils.split(
                    preferences!!.getString(key, ""),
                    "‚‗‚"
                ) as ArrayList<String?>?
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of Integers
     */
    fun getListInt(key: String?): ArrayList<Int>? {
        val myList = preferences!!.getString(key, "").toString().split("‚‗‚")
        var arrayToList: ArrayList<String> = ArrayList()
        arrayToList.addAll(myList)
        val newList = ArrayList<Int>()
        Log.v("DEV",arrayToList.size.toString());
        if(myList.get(0) != "") {
            for (item in arrayToList) newList.add(item.toInt())
        }
        return newList
    }

    /**
     * Get double value from SharedPreferences at 'key'. If exception thrown, return 'defaultValue'
     * @param key SharedPreferences key
     * @param defaultValue double value returned if exception is thrown
     * @return double value at 'key' or 'defaultValue' if exception is thrown
     */
    fun getDouble(key: String?, defaultValue: Double): Double {
        val number = getString(key)
        return try {
            number!!.toDouble()
        } catch (e: NumberFormatException) {
            defaultValue
        }
    }

    /**
     * Get parsed ArrayList of Double from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of Double
     */
    fun getListDouble(key: String?): ArrayList<Double>? {
        val myList = TextUtils.split(preferences!!.getString(key, ""), "‚‗‚")
        val arrayToList: ArrayList<String> = myList as ArrayList<String>
        val newList = ArrayList<Double>()
        for (item in arrayToList) newList.add(item.toDouble())
        return newList
    }

    /**
     * Get parsed ArrayList of Integers from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of Longs
     */
    fun getListLong(key: String?): ArrayList<Long>? {
        val myList = TextUtils.split(preferences!!.getString(key, ""), "‚‗‚")
        val arrayToList: ArrayList<String> = myList as ArrayList<String>
        val newList = ArrayList<Long>()
        for (item in arrayToList) newList.add(item.toLong())
        return newList
    }

    /**
     * Get boolean value from SharedPreferences at 'key'. If key not found, return 'defaultValue'
     * @param key SharedPreferences key
     * @return boolean value at 'key' or 'defaultValue' if key not found
     */
    fun getBoolean(key: String?): Boolean {
        return preferences!!.getBoolean(key, false)
    }

    /**
     * Get parsed ArrayList of Boolean from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of Boolean
     */
    fun getListBoolean(key: String?): ArrayList<Boolean>? {
        val myList = getListString(key)
        val newList = ArrayList<Boolean>()
        for (item in myList!!) {
            if (item == "true") {
                newList.add(true)
            } else {
                newList.add(false)
            }
        }
        return newList
    }

    fun getListObject(key: String?, mClass: Class<*>?): ArrayList<Any>? {
        val gson = Gson()
        val objStrings = getListString(key)
        val objects = ArrayList<Any>()
        for (jObjString in objStrings!!) {
            val value = gson.fromJson(jObjString, mClass)
            objects.add(value)
        }
        return objects
    }

    fun <T> getObject(key: String?, classOfT: Class<T>?): T {
        val json = getString(key)
        val value: T = Gson().fromJson(json, classOfT) ?: throw NullPointerException()
        return value
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     * @param key pref key
     */
    fun checkForNullKey(key: String?) {
        if (key == null) {
            throw java.lang.NullPointerException()
        }
    }

    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     * @param value pref key
     */
    fun checkForNullValue(value: String?) {
        if (value == null) {
            throw java.lang.NullPointerException()
        }
    }

    //Put Metod
    fun putInt(key: String?, value: Int) {
        checkForNullKey(key)
        preferences!!.edit().putInt(key, value).apply()
    }

    /**
     * Put ArrayList of Integer into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param intList ArrayList of Integer to be added
     */
    fun putListInt(key: String?, intList: ArrayList<Int>) {
        checkForNullKey(key)
        val myIntList: Array<Int> = intList.toArray(arrayOfNulls(intList.size))
        preferences!!.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply()
    }

    /**
     * Put long value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value long value to be added
     */
    fun putLong(key: String?, value: Long) {
        checkForNullKey(key)
        preferences!!.edit().putLong(key, value).apply()
    }

    /**
     * Put ArrayList of Long into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param longList ArrayList of Long to be added
     */
    fun putListLong(key: String?, longList: ArrayList<Long?>) {
        checkForNullKey(key)
        val myLongList: Array<Long> = longList.toArray(arrayOfNulls(longList.size))
        preferences!!.edit().putString(key, TextUtils.join("‚‗‚", myLongList)).apply()
    }

    /**
     * Put float value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value float value to be added
     */
    fun putFloat(key: String?, value: Float) {
        checkForNullKey(key)
        preferences!!.edit().putFloat(key, value).apply()
    }

    /**
     * Put double value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value double value to be added
     */
    fun putDouble(key: String?, value: Double) {
        checkForNullKey(key)
        putString(key, value.toString())
    }

    /**
     * Put ArrayList of Double into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param doubleList ArrayList of Double to be added
     */
    fun putListDouble(key: String?, doubleList: ArrayList<Double?>) {
        checkForNullKey(key)
        val myDoubleList: Array<Double> = doubleList.toArray(arrayOfNulls(doubleList.size))
        preferences!!.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply()
    }

    /**
     * Put String value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value String value to be added
     */
    fun putString(key: String?, value: String?) {
        checkForNullKey(key)
        checkForNullValue(value)
        preferences!!.edit().putString(key, value).apply()
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    fun putListString(key: String?, stringList: ArrayList<String?>) {
        checkForNullKey(key)
        val myStringList: Array<String> = stringList.toArray(arrayOfNulls(stringList.size))
        preferences!!.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply()
    }

    /**
     * Put boolean value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value boolean value to be added
     */
    fun putBoolean(key: String?, value: Boolean) {
        checkForNullKey(key)
        preferences!!.edit().putBoolean(key, value).apply()
    }

    /**
     * Put ArrayList of Boolean into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param boolList ArrayList of Boolean to be added
     */
    fun putListBoolean(key: String?, boolList: ArrayList<Boolean>) {
        checkForNullKey(key)
        val newList = ArrayList<String?>()
        for (item in boolList) {
            if (item) {
                newList.add("true")
            } else {
                newList.add("false")
            }
        }
        putListString(key, newList)
    }

    /**
     * Put ObJect any type into SharedPrefrences with 'key' and save
     * @param key SharedPreferences key
     * @param obj is the Object you want to put
     */
    fun putObject(key: String?, obj: Any?) {
        checkForNullKey(key)
        val gson = Gson()
        putString(key, gson.toJson(obj))
    }

    fun putListObject(key: String?, objArray: ArrayList<Any?>) {
        checkForNullKey(key)
        val gson = Gson()
        val objStrings = ArrayList<String?>()
        for (obj in objArray) {
            objStrings.add(gson.toJson(obj))
        }
        putListString(key, objStrings)
    }

    /**
     * Remove SharedPreferences item with 'key'
     * @param key SharedPreferences key
     */
    fun remove(key: String?) {
        preferences!!.edit().remove(key).apply()
    }

    /**
     * Clear SharedPreferences (remove everything)
     */
    fun clear() {
        preferences!!.edit().clear().apply()
    }

    /**
     * Check if external storage is writable or not
     * @return true if writable, false otherwise
     */
    fun isExternalStorageWritable(): Boolean {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
    }

    /**
     * Check if external storage is readable or not
     * @return true if readable, false otherwise
     */
    fun isExternalStorageReadable(): Boolean {
        val state: String = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)
    }

}