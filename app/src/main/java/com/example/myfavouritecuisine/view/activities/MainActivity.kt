package com.example.myfavouritecuisine.view.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myfavouritecuisine.R
import com.example.myfavouritecuisine.databinding.ActivityMainBinding
import com.example.myfavouritecuisine.model.notification.NotifyWorker
import com.example.myfavouritecuisine.utils.Constants
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mNavController: NavController
    private val NOTIFICATION_PERMISSION_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mNavController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes, R.id.navigation_favourite_dishes, R.id.navigation_random_dish
            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        binding.navView.setupWithNavController(mNavController)
        if(intent.hasExtra(Constants.NOTIFICATION_ID)) {
            val notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, 0)
            binding.navView.selectedItemId = R.id.navigation_random_dish
        }

        requestNotificationPermission()
        startWork()

    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_CODE
                )
            }
        }
    }

    private fun createConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(true)
        .build()

    private fun createWorkRequest() = PeriodicWorkRequestBuilder<NotifyWorker>(15, TimeUnit.MINUTES)
        .setConstraints(createConstraints())
        .build()

    private fun startWork() {
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "Fav Dish Notify Work",
                ExistingPeriodicWorkPolicy.REPLACE,
                createWorkRequest()
            )
    }
    fun hideBottomNavigationView() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 300
    }

    fun showBottomNavigationView() {
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(0f).duration = 300
    }

    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp() || super.onSupportNavigateUp()
    }
}
