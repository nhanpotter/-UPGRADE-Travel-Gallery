package tech.ducletran.travelgalleryupgrade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NAV_ID_NONE = -1
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigation: NavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfig: AppBarConfiguration

    private var currentNavId = NAV_ID_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        navigation = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }

        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHostFragment).navigateUp(appBarConfig)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentNavId = navigation.checkedItem?.itemId ?: NAV_ID_NONE
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    private fun setupNavigation() {
        navController = findNavController(this, R.id.navHostFragment)

        navigation.setupWithNavController(navController)

        navigation.setNavigationItemSelectedListener {
            it.isChecked = true
            drawerLayout.closeDrawers()
            navigateTo(it.itemId)
            true
        }

        appBarConfig = AppBarConfiguration(setOf(
            R.id.photosFragment,
            R.id.albumsFragment,
            R.id.mapFragment,
            R.id.travelGoalsFragment,
            R.id.accountFragment,
            R.id.settingsFragment
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun navigateTo(navId: Int) {
        if (currentNavId == navId) return
        currentNavId = navId

        val fragmentId = when (navId) {
            R.id.navPhotos -> R.id.photosFragment
            R.id.navAlbums -> R.id.albumsFragment
            R.id.navAccount -> R.id.accountFragment
            R.id.navMap -> R.id.mapFragment
            R.id.navSettings -> R.id.settingsFragment
            R.id.navTravelGoals -> R.id.travelGoalsFragment
            else -> return
        }
        navController.navigate(fragmentId)
    }
}
