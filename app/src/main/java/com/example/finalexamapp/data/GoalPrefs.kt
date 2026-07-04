package com.example.finalexamapp.data

import android.content.Context
import android.content.SharedPreferences

class GoalPrefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("goal_prefs", Context.MODE_PRIVATE)

    fun getDailyStepGoal(): Int {
        return prefs.getInt("daily_step_goal", 10000)
    }

    fun setDailyStepGoal(goal: Int) {
        prefs.edit().putInt("daily_step_goal", goal).apply()
    }
}
