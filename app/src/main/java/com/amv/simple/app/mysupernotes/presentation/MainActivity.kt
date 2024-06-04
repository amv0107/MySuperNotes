package com.amv.simple.app.mysupernotes.presentation

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.ActivityMainBinding
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreTypeTheme
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

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

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.FLEXIBLE

    private val destinations = setOf(
        R.id.mainListFragment,
        R.id.archiveListFragment,
        R.id.favoriteListFragment,
        R.id.trashListFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.registerListener(installStateUpdatedListener)
        }
        checkForAppUpdates()
        setContentView(binding.root)

        enableEdgeToEdge()

        setSupportActionBar(toolbar)
        setupDrawer()

        binding.mainToolbar.setNavigationOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.categoryListFragment -> navController.navigateUp()
                R.id.settingsFragment -> navController.navigateUp()
                R.id.listOfNotesByCategoryOrTag -> navController.navigateUp()
                R.id.editorFragment -> {
                    if (onBackPressedDispatcher.hasEnabledCallbacks())
                        onBackPressedDispatcher.onBackPressed()
                    else
                        navController.navigateUp()
                }
                else -> drawerLayout.open()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO:  FIX SN_B_5 мне кажется будут проблемы когда цвет будет браться из заметки
        if (navController.currentDestination?.id == R.id.editorFragment) {
            toolbar.setBackgroundColor(getColor(R.color.yellow))
            window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.yellow)
        }

        if (updateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        updateType,
                        this,
                        123 // TODO: What the magic number???
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) { // TODO: What the magic number???
            if (resultCode != RESULT_OK) {
                Log.d("TAG", "Something went wrong updating...")
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (updateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        }
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

    fun updateToolbarTitle(title: String) {
        binding.toolbarTitle.text = title
    }

    private fun setupDrawer() {
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        appBarConfiguration = AppBarConfiguration(destinations, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateToolbarTitle(navController.currentDestination?.label.toString())

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

    private val installStateUpdatedListener = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            Toast.makeText(applicationContext, "Download successful. Restarting app in 5 seconds.", Toast.LENGTH_SHORT)
                .show()
            lifecycleScope.launch {
                delay(5.seconds)
                appUpdateManager.completeUpdate()
            }
        }
    }

    /**
     * Проверяем наличие обновлений
     */
    private fun checkForAppUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = when (updateType) {
                AppUpdateType.FLEXIBLE -> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE -> info.isImmediateUpdateAllowed
                else -> false
            }

            if (isUpdateAvailable && isUpdateAllowed) {
                appUpdateManager.startUpdateFlowForResult(
                    info,
                    updateType,
                    this,
                    123 // TODO: What the magic number???
                )
            }
        }
    }
}