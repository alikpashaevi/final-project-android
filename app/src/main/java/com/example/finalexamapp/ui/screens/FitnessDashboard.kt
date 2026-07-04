package com.example.finalexamapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalexamapp.ui.components.HeroMetricCard
import com.example.finalexamapp.ui.components.TimeframeSelector
import com.example.finalexamapp.viewmodel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessDashboard(
    viewModel: FitnessViewModel
) {
    val filteredLogs by viewModel.filteredProgressLogs.collectAsState()
    val isTrackingActive by viewModel.isTrackingActive.collectAsState()
    val activeStepCount by viewModel.activeStepCount.collectAsState()
    val liveDistanceKm by viewModel.liveDistanceKm.collectAsState()
    val selectedTimeframe by viewModel.selectedTimeframe.collectAsState()

    val totalSteps = filteredLogs.sumOf { it.totalSteps } + activeStepCount
    val totalDistance = filteredLogs.sumOf { it.totalDistance } + liveDistanceKm
    val totalCalories = filteredLogs.sumOf { it.totalCalories } + (liveDistanceKm * 60.0)

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Stride & Fuel", 
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1B4332)
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF8F9FA)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                HeroMetricCard(
                    totalSteps = totalSteps,
                    totalDistance = totalDistance,
                    totalCalories = totalCalories
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            item {
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
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD8F3DC)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Live Workout",
                                color = Color(0xFF19724F),
                                style = MaterialTheme.typography.labelLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$activeStepCount",
                                        color = Color(0xFF0E6C4A),
                                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text("Steps", color = Color(0xFF19724F), style = MaterialTheme.typography.labelMedium)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = String.format("%.2f", liveDistanceKm),
                                        color = Color(0xFF0E6C4A),
                                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text("km", color = Color(0xFF19724F), style = MaterialTheme.typography.labelMedium)
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { viewModel.stopAndSaveWorkout() },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA1A1A)),
                                shape = RoundedCornerShape(999.dp)
                            ) {
                                Text("STOP & SAVE", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                TimeframeSelector(
                    selectedTimeframe = selectedTimeframe,
                    onTimeframeSelected = { viewModel.selectedTimeframe.value = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(filteredLogs) { summary ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE7E8E9)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Period: ${summary.period}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF191C1D)
                            )
                            Text(
                                text = "${summary.totalSteps} Steps • ${String.format("%.2f", summary.totalDistance)} km",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = "${String.format("%.0f", summary.totalCalories)} kcal",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF74C69D)
                        )
                    }
                }
            }
        }
    }
}
