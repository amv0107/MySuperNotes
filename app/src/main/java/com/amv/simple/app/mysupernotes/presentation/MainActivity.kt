package com.amv.simple.app.mysupernotes.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
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

        enableEdgeToEdge()

        setSupportActionBar(toolbar)

        setupDrawer()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navHostFragmentContainer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupDrawer() {
        val drawerLayout = binding.drawerLayout
        val navView = binding.navView
        val navController = findNavController(R.id.navHostFragmentContainer)

        appBarConfiguration = AppBarConfiguration(destinations, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbarTitle.text = navController.currentDestination?.label
            if (destination.id == R.id.editorFragment) {
                toolbar.setBackgroundColor(getColor(R.color.yellow))
                window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.yellow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                toolbar.setBackgroundColor(getColor(R.color.white))
                window.statusBarColor = Color.TRANSPARENT
            }
        }
    }
}