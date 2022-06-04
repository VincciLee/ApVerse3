package com.example.apverse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.apverse.databinding.ActivityMainBinding
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.Users
import com.example.apverse.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Get info of login user
        lifecycleScope.launch {
            val data = Firestore().getUserInfo()
            var user: Users = Users()

            for (i in data?.documents!!){
                user = i?.toObject(Users::class.java)!!
            }

            DisplayHeaderInfo(user)
        }

        // Setup drawer menu
        if(Constants.USERTYPE == "librarian"){
            // This is setup for librarian
            navController.setGraph(R.navigation.librarian_navigation)
            navView.getMenu().clear()
            navView.inflateMenu(R.menu.librarian_main_drawer)

            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_l_home, R.id.nav_l_room, R.id.nav_l_book, R.id.nav_l_reservation, R.id.nav_logout
                ), drawerLayout
            )
        }else if(Constants.USERTYPE == "student"){
            // This is setup for student
            navController.setGraph(R.navigation.student_navigation)
            navView.getMenu().clear()
            navView.inflateMenu(R.menu.student_main_drawer)

            appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.nav_s_home, R.id.nav_s_room, R.id.nav_s_book, R.id.nav_s_computer, R.id.nav_logout
                ), drawerLayout
            )
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val handled = onNavDestinationSelected(item, navController)

        if(!handled){
            when (item.itemId){
                R.id.nav_logout -> {
                    Constants.USERTYPE = "librarian"
                    Log.i("ApVerse", "User has logged out. ")

                    FirebaseApp.initializeApp(this)
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return handled
    }

    fun DisplayHeaderInfo(user: Users){
        Log.i("ApVerse", "Displaying User Info.")

        val headerView = binding.navView.getHeaderView(0)
        val userEmail = headerView.findViewById<TextView>(R.id.user_email)
        val userName = headerView.findViewById<TextView>(R.id.user_name)
        val userProfile = headerView.findViewById<ImageView>(R.id.profile_image)

        userEmail.text = user.user_email
        userName.text = user.user_name

        Glide.with(this)
            .load(user.user_profile)
            .apply(RequestOptions.placeholderOf(R.drawable.profile_user)
            .override(75,75))
            .into(userProfile)
    }

}