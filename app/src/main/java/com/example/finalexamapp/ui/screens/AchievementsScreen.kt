package com.example.finalexamapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalexamapp.viewmodel.FitnessViewModel

@Composable
fun AchievementsScreen(viewModel: FitnessViewModel) {
    val historyLogs by viewModel.historyLogs.collectAsState()
    val isGoalMetToday by viewModel.isGoalMetToday.collectAsState()
    val weeklyGoalsMetCount by viewModel.weeklyGoalsMetCount.collectAsState()
    val monthlyGoalsMetCount by viewModel.monthlyGoalsMetCount.collectAsState()

    val totalSteps = historyLogs.sumOf { it.stepCount }
    val hasMarathoner = totalSteps > 30000
    val hasTorchbearer = historyLogs.any { it.caloriesBurned > 400.0 }
    val hasConsistent = historyLogs.size > 5
    val hasFirstStep = historyLogs.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Today Status",
                        tint = if (isGoalMetToday) Color(0xFF0E6C4A) else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isGoalMetToday) "Goal Met Today!" else "Goal Not Met Yet",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1B4332)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$weeklyGoalsMetCount/7", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = Color(0xFF0E6C4A))
                        Text("Weekly Streak", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$monthlyGoalsMetCount", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = Color(0xFF0E6C4A))
                        Text("Monthly Streak", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Medals & Badges",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF1B4332),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                BadgeItem("Marathoner", "> 30k Steps Total", hasMarathoner)
                BadgeItem("Consistent Mover", "> 5 Workouts", hasConsistent)
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                BadgeItem("Torchbearer", "> 400 kcal in 1 session", hasTorchbearer)
                BadgeItem("First Step", "First Log", hasFirstStep)
            }
        }
    }
}

@Composable
fun BadgeItem(title: String, desc: String, unlocked: Boolean) {
    val bg = if (unlocked) Color(0xFFA0F4C8) else Color(0xFFF3F4F5)
    val fg = if (unlocked) Color(0xFF19724F) else Color(0xFF717973)
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bg),
        modifier = Modifier.fillMaxWidth().height(100.dp) // Force height so they form a nice grid block
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(title, color = fg, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            Text(desc, color = fg, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}
