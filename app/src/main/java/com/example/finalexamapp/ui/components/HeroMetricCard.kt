package com.example.finalexamapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HeroMetricCard(
    totalSteps: Int,
    totalDistance: Double,
    totalCalories: Double
) {
    val sageAccent = Color(0xFF74C69D)
    val mint = Color(0xFFD8F3DC)
    val deepForest = Color(0xFF1B4332)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(listOf(sageAccent, mint)),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CustomProgressRing(
                currentSteps = totalSteps,
                currentDistance = totalDistance,
                modifier = Modifier.size(120.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Total Distance",
                    color = deepForest.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${String.format("%.2f", totalDistance)} km",
                    color = deepForest,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Calories",
                    color = deepForest.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${String.format("%.0f", totalCalories)} kcal",
                    color = deepForest,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
