package com.serproteam.agencetest.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.serproteam.agencetest.R
import com.serproteam.agencetest.core.TinyDB
import com.serproteam.agencetest.data.model.Product
import com.serproteam.agencetest.databinding.ActivityHomeBinding
import com.serproteam.agencetest.ui.viewmodel.CartViewModel
import com.serproteam.agencetest.ui.viewmodel.UserViewModel

class HomeActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private val cartViewModel: CartViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    val logi = "Dev"
    var idOrden: Int = 0
    lateinit var producto: Product
    var cartList = ArrayList<Product>()
    lateinit var viewGlobal: View
    lateinit var drawerLayout: DrawerLayout
    lateinit var tinyDB:TinyDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tinyDB = TinyDB(applicationContext)

        setSupportActionBar(binding.appBarHome.toolbar)

        binding.appBarHome.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_miProducts, R.id.nav_setting
            ), drawerLayout
//            ,R.id.nav_Session
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        cartViewModel.Cart.observe(this, Observer {
            if (it!!.size > 0) {
                Log.v(logi, "tiene algo")
                binding.appBarHome.cantProductCart.visibility = View.VISIBLE
                binding.appBarHome.cantProductCart.text = it!!.size.toString()
                cartList = it
            } else {
                Log.v(logi, "no tiene nada")
                binding.appBarHome.cantProductCart.visibility = View.GONE
            }
        })

        viewGlobal = binding.root

        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            if (menuItem.isChecked) menuItem.isChecked = false else menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_miProducts -> {
                    navController.navigate(R.id.nav_miProducts)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_setting -> {
                    navController.navigate(R.id.nav_setting)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_Session -> {
                    Log.v(logi, "Session")
                    Toast.makeText(this, "se cerro la session", Toast.LENGTH_SHORT).show()
                    Log.v(logi, "se cerro la session")
                    LoginManager.getInstance().logOut()
                    FirebaseAuth.getInstance().signOut()

                    userViewModel.delUser(applicationContext)
                    startActivity(Intent(this, MainActivity::class.java))
                    return@OnNavigationItemSelectedListener true
                }
                else -> {
                    Log.v(logi, "else")
                    return@OnNavigationItemSelectedListener true
                }
            }
        })

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.nav_Session){
            Toast.makeText(this, "se cerro la session", Toast.LENGTH_SHORT).show()
            Log.v(logi, "se cerro la session")
            LoginManager.getInstance().logOut()
            FirebaseAuth.getInstance().signOut()
        }
        return true;
    }
}