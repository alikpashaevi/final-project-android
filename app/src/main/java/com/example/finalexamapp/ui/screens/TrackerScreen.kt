package com.example.finalexamapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalexamapp.ui.components.CustomProgressRing
import com.example.finalexamapp.viewmodel.FitnessViewModel

@Composable
fun TrackerScreen(viewModel: FitnessViewModel) {
    val isTrackingActive by viewModel.isTrackingActive.collectAsState()
    val activeStepCount by viewModel.activeStepCount.collectAsState()
    val liveDistanceKm by viewModel.liveDistanceKm.collectAsState()
    val liveCalories by viewModel.liveCalories.collectAsState()
    val dailyStepGoal by viewModel.dailyStepGoal.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomProgressRing(
            currentSteps = activeStepCount,
            currentDistance = liveDistanceKm,
            targetSteps = dailyStepGoal,
            modifier = Modifier.size(240.dp)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MetricChip("Steps", "$activeStepCount")
            MetricChip("Distance", "${String.format("%.2f", liveDistanceKm)} km")
            MetricChip("Burn", "${String.format("%.0f", liveCalories)} kcal")
        }
        
        Spacer(modifier = Modifier.height(64.dp))
        
        if (!isTrackingActive) {
            Button(
                onClick = { viewModel.startTracking() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332)),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text("START TRACKING", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = { viewModel.stopAndSaveWorkout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A)),
                shape = RoundedCornerShape(999.dp)
            ) {
                Text("STOP & SAVE", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun MetricChip(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color(0xFF1B4332))
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
    }
}
