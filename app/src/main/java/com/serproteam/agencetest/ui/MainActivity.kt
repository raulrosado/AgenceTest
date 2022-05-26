package com.serproteam.agencetest.ui

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.serproteam.agencetest.core.ReplaceFragment
import com.serproteam.agencetest.databinding.ActivityMainBinding
import com.serproteam.agencetest.ui.Fragment.HomeFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var replaceFragment: ReplaceFragment = ReplaceFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configInicio()
    }

    private fun configInicio() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        replaceFragment.replace(HomeFragment(), fragmentTransaction)
    }
}