package com.serproteam.agencetest.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
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
import com.serproteam.agencetest.data.model.User
import com.serproteam.agencetest.databinding.ActivityHomeBinding
import com.serproteam.agencetest.ui.viewmodel.CartViewModel
import com.serproteam.agencetest.ui.viewmodel.UserViewModel
import java.security.acl.Owner


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private val cartViewModel: CartViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    val logi = "Dev"
    var idOrden: Int = 0
    lateinit var producto: Product
    var cartList = ArrayList<Product>()
    lateinit var drawerLayout: DrawerLayout
    lateinit var tinyDB: TinyDB
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tinyDB = TinyDB(applicationContext)
        setSupportActionBar(binding.appBarHome.toolbar)
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        user = userViewModel.getUser(applicationContext)

        binding.appBarHome.fab.setOnClickListener { view ->
            navController.navigate(R.id.nav_miProducts)
        }

        drawerLayout = binding.drawerLayout
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_miProducts, R.id.nav_setting,R.id.nav_profile
            ), drawerLayout
//            ,R.id.nav_Session
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            if (menuItem.isChecked) menuItem.isChecked = false else menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_profile -> {
                    navController.navigate(R.id.nav_profile)
                    return@OnNavigationItemSelectedListener true
                }
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

        val header: View = binding.navView.getHeaderView(0)
        header.findViewById<TextView>(R.id.nameUser).text = "${user.name} ${user.lastName}"
        header.findViewById<TextView>(R.id.emailUser).text = "${user.email}"

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_Session) {
            Toast.makeText(this, "se cerro la session", Toast.LENGTH_SHORT).show()
            Log.v(logi, "se cerro la session")
            LoginManager.getInstance().logOut()
            FirebaseAuth.getInstance().signOut()
        }
        return true;
    }
}