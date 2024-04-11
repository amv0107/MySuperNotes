package com.amv.simple.app.mysupernotes.presentation

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.ActivityMainBinding
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreTypeTheme
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainActivityViewModel by viewModels()
    private val toolbar: MaterialToolbar by lazy { binding.mainToolbar }
    private val drawerLayout: DrawerLayout by lazy { binding.drawerLayout }
    private val navView: NavigationView by lazy { binding.navView }
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        navHostFragment.navController
    }
    private val actionBarDrawerToggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
    }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var uiMode: DataStoreTypeTheme

    private val destinations = setOf(
        R.id.mainListFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        enableEdgeToEdge()

        setSupportActionBar(toolbar)
        setupDrawer()

        binding.mainToolbar.setNavigationOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.mainListFragment -> {
                    drawerLayout.open()
                }

                R.id.editorFragment -> {
                    if (onBackPressedDispatcher.hasEnabledCallbacks())
                        onBackPressedDispatcher.onBackPressed()
                    else
                        navController.navigateUp()
                }

                else -> navController.navigateUp()
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setUIMode(name: DataStoreTypeTheme) {
        when (name) {
            DataStoreTypeTheme.DARK -> {
                uiMode = DataStoreTypeTheme.DARK
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            DataStoreTypeTheme.LIGHT -> {
                uiMode = DataStoreTypeTheme.LIGHT
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            DataStoreTypeTheme.SYSTEM -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO:  FIX SN_B_5 мне кажется будут проблемы когда цвет будет браться из заметки
        if (navController.currentDestination?.id == R.id.editorFragment) {
            toolbar.setBackgroundColor(getColor(R.color.yellow))
            window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.yellow)
        }
    }

    private fun setupDrawer() {
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        appBarConfiguration = AppBarConfiguration(destinations, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbarTitle.text = navController.currentDestination?.label

            if (destination.id == R.id.editorFragment) {
                toolbar.setBackgroundColor(getColor(R.color.yellow))
                window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.yellow)
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                viewModel.themeAppFlow.observe(this) {
                    uiMode = it.typeTheme
                    setUIMode(uiMode)
                    when (applicationContext.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            if (uiMode == DataStoreTypeTheme.DARK)
                                toolbar.setBackgroundColor(getColor(R.color.black))
                            else
                                toolbar.setBackgroundColor(getColor(R.color.white))
                        }

                        Configuration.UI_MODE_NIGHT_YES -> {
                            if (uiMode == DataStoreTypeTheme.LIGHT)
                                toolbar.setBackgroundColor(getColor(R.color.white))
                            else
                                toolbar.setBackgroundColor(getColor(R.color.black))

                        }
                    }
                    window.statusBarColor = Color.TRANSPARENT
                }
            }
        }
    }
}