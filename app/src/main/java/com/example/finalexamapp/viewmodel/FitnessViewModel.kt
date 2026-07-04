package com.example.finalexamapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.finalexamapp.data.GoalPrefs
import com.example.finalexamapp.data.StepLog
import com.example.finalexamapp.data.StepLogDao
import com.example.finalexamapp.data.TimeframeSummary
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class Timeframe {
    DAILY, WEEKLY, MONTHLY
}

class FitnessViewModel(private val dao: StepLogDao, private val prefs: GoalPrefs) : ViewModel() {

    val dailyStepGoal = MutableStateFlow(prefs.getDailyStepGoal())

    fun updateDailyGoal(newGoal: Int) {
        prefs.setDailyStepGoal(newGoal)
        dailyStepGoal.value = newGoal
    }

    val selectedTimeframe = MutableStateFlow(Timeframe.DAILY)

    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredProgressLogs: StateFlow<List<TimeframeSummary>> = selectedTimeframe
        .flatMapLatest { timeframe ->
            when (timeframe) {
                Timeframe.DAILY -> dao.getDailySummaries()
                Timeframe.WEEKLY -> dao.getWeeklySummaries()
                Timeframe.MONTHLY -> dao.getMonthlySummaries()
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val historyLogs: StateFlow<List<StepLog>> = dao.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val isGoalMetToday: StateFlow<Boolean> = combine(dao.getDailySummaries(), dailyStepGoal) { dailySummaries, goal ->
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val todaySummary = dailySummaries.find { it.period == todayStr }
        val stepsToday = todaySummary?.totalSteps ?: 0
        stepsToday >= goal
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    val weeklyGoalsMetCount: StateFlow<Int> = combine(dao.getDailySummaries(), dailyStepGoal) { dailySummaries, goal ->
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val last7Days = (0..6).map { i -> 
            sdf.format(Date(System.currentTimeMillis() - i * 86400000L))
        }
        dailySummaries.filter { it.period in last7Days && it.totalSteps >= goal }.size
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val monthlyGoalsMetCount: StateFlow<Int> = combine(dao.getDailySummaries(), dailyStepGoal) { dailySummaries, goal ->
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val last30Days = (0..29).map { i -> 
            sdf.format(Date(System.currentTimeMillis() - i * 86400000L))
        }
        dailySummaries.filter { it.period in last30Days && it.totalSteps >= goal }.size
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val isTrackingActive = MutableStateFlow(false)
    val activeStepCount = MutableStateFlow(0)
    val liveDistanceKm = MutableStateFlow(0.0)
    val liveCalories = MutableStateFlow(0.0)

    fun startTracking() {
        isTrackingActive.value = true
        activeStepCount.value = 0
        liveDistanceKm.value = 0.0
        liveCalories.value = 0.0
    }

    fun stopAndSaveWorkout() {
        isTrackingActive.value = false
        val steps = activeStepCount.value
        if (steps > 0) {
            saveWorkoutSession(steps)
        }
        activeStepCount.value = 0
        liveDistanceKm.value = 0.0
        liveCalories.value = 0.0
    }

    fun addLiveSteps(steps: Int) {
        if (isTrackingActive.value) {
            activeStepCount.value += steps
            liveDistanceKm.value = (activeStepCount.value * 0.76) / 1000.0
            liveCalories.value = liveDistanceKm.value * 60.0
        }
    }

    private fun saveWorkoutSession(steps: Int) {
        val distance = (steps * 0.76) / 1000.0
        val calories = distance * 60.0
        
        val log = StepLog(
            stepCount = steps,
            distanceKm = distance,
            caloriesBurned = calories,
            timestamp = System.currentTimeMillis()
        )
        viewModelScope.launch {
            dao.insertLog(log)
        }
    }
}

class FitnessViewModelFactory(private val dao: StepLogDao, private val prefs: GoalPrefs) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FitnessViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FitnessViewModel(dao, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
