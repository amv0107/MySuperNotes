package com.amv.simple.app.mysupernotes.presentation

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val toolbar: MaterialToolbar by lazy {
        binding.mainToolbar
    }

    private val destinations = setOf(
        R.id.mainListFragment
    )

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(toolbar)

        setupDrawer()
    }

    fun updateTitleToolbar(@StringRes title: Int){
        binding.toolbarTitle.setText(title)
    }

    fun updateColorToolbar(@ColorRes backgroundColor: Int){
        toolbar.setBackgroundColor(getColor(backgroundColor))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragmentContainer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupDrawer() {
        val drawerLayout =binding.drawerLayout
        val navView = binding.navView
        val navController = findNavController(R.id.navHostFragmentContainer)

        appBarConfiguration = AppBarConfiguration(destinations, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}