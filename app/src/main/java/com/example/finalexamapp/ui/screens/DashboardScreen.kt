package com.example.finalexamapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finalexamapp.ui.components.TimeframeSelector
import com.example.finalexamapp.viewmodel.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: FitnessViewModel) {
    val filteredLogs by viewModel.filteredProgressLogs.collectAsState()
    val selectedTimeframe by viewModel.selectedTimeframe.collectAsState()
    val dailyStepGoal by viewModel.dailyStepGoal.collectAsState()
    
    var showGoalInput by remember { mutableStateOf(false) }
    var goalInput by remember { mutableStateOf(dailyStepGoal.toString()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Daily Goal", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color(0xFF1B4332))
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (showGoalInput) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = goalInput,
                                onValueChange = { goalInput = it },
                                label = { Text("Steps") },
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = { 
                                    goalInput.toIntOrNull()?.let { 
                                        viewModel.updateDailyGoal(it)
                                        showGoalInput = false
                                    } 
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332))
                            ) {
                                Text("Save")
                            }
                        }
                    } else {
                        Text(
                            text = if (dailyStepGoal > 0) "Your daily goal is $dailyStepGoal steps." else "Set a daily goal to keep you motivated.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF1B4332)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { 
                                goalInput = dailyStepGoal.toString()
                                showGoalInput = true 
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332))
                        ) {
                            Text("Update Goal")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
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
