package com.example.finalexamapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.finalexamapp.data.FitnessDatabase
import com.example.finalexamapp.data.GoalPrefs
import com.example.finalexamapp.ui.navigation.Screen
import com.example.finalexamapp.ui.screens.AchievementsScreen
import com.example.finalexamapp.ui.screens.DashboardScreen
import com.example.finalexamapp.ui.screens.TrackerScreen
import com.example.finalexamapp.ui.theme.EcoCommuteTheme
import com.example.finalexamapp.viewmodel.FitnessViewModel
import com.example.finalexamapp.viewmodel.FitnessViewModelFactory

class MainActivity : ComponentActivity(), SensorEventListener {

    private val viewModel: FitnessViewModel by viewModels {
        val database = FitnessDatabase.getDatabase(this)
        val prefs = GoalPrefs(this)
        FitnessViewModelFactory(database.stepLogDao(), prefs)
    }

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    
    private var previousStepCount: Float? = null
    private var hasPermission = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                hasPermission = true
                registerSensor()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        checkAndRequestPermissions()

        setContent {
            EcoCommuteTheme {
                MainAppScaffold(viewModel)
            }
        }
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    hasPermission = true
                    registerSensor()
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                }
            }
        } else {
            hasPermission = true
            registerSensor()
        }
    }

    private fun registerSensor() {
        if (hasPermission) {
            stepCounterSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerSensor()
    }

    override fun onPause() {
        super.onPause()
        if (hasPermission) {
            sensorManager.unregisterListener(this)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val currentSteps = event.values[0]
            if (previousStepCount == null) {
                previousStepCount = currentSteps
            } else {
                val delta = (currentSteps - previousStepCount!!).toInt()
                if (delta > 0) {
                    viewModel.addLiveSteps(delta)
                    previousStepCount = currentSteps
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

@Composable
fun MainAppScaffold(viewModel: FitnessViewModel) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Tracker,
        Screen.Dashboard,
        Screen.Achievements
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFF8F9FA),
                contentColor = Color(0xFF1B4332)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF1B4332),
                            selectedTextColor = Color(0xFF1B4332),
                            indicatorColor = Color(0xFFD8F3DC),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Tracker.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Tracker.route) { TrackerScreen(viewModel) }
            composable(Screen.Dashboard.route) { DashboardScreen(viewModel) }
            composable(Screen.Achievements.route) { AchievementsScreen(viewModel) }
        }
    }
}
