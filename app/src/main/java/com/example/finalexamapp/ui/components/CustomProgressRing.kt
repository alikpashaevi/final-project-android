package com.example.finalexamapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomProgressRing(
    currentSteps: Int,
    currentDistance: Double,
    targetSteps: Int = 10000,
    modifier: Modifier = Modifier
) {
    val primaryGreen = Color(0xFF1B4332)
    val softSage = Color(0xFF74C69D)
    val mint = Color(0xFFD8F3DC)
    
    val distanceBrush = Brush.linearGradient(colors = listOf(Color(0xFF0E6C4A), Color(0xFFA0F4C8)))

    val targetDistance = (targetSteps * 0.76) / 1000.0

    val stepProgress = (currentSteps.toFloat() / targetSteps).coerceIn(0f, 1f)
    val distanceProgress = (currentDistance.toFloat() / targetDistance.toFloat()).coerceIn(0f, 1f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(160.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 16f
            val spacing = 24f
            
            val outerSize = size.minDimension - strokeWidth
            val innerSize = outerSize - spacing * 2
            
            val outerTopLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val innerTopLeft = Offset(strokeWidth / 2 + spacing, strokeWidth / 2 + spacing)

            // Outer ring bg (Distance)
            drawArc(
                color = primaryGreen.copy(alpha = 0.15f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = outerTopLeft,
                size = Size(outerSize, outerSize),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Inner ring bg (Steps)
            drawArc(
                color = primaryGreen.copy(alpha = 0.15f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = innerTopLeft,
                size = Size(innerSize, innerSize),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            if (distanceProgress > 0f) {
                drawArc(
                    brush = distanceBrush,
                    startAngle = -90f,
                    sweepAngle = distanceProgress * 360f,
                    useCenter = false,
                    topLeft = outerTopLeft,
                    size = Size(outerSize, outerSize),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }

            if (stepProgress > 0f) {
                drawArc(
                    brush = Brush.linearGradient(colors = listOf(softSage, mint)),
                    startAngle = -90f,
                    sweepAngle = stepProgress * 360f,
                    useCenter = false,
                    topLeft = innerTopLeft,
                    size = Size(innerSize, innerSize),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }
        
        Text(
            text = "$currentSteps",
            color = primaryGreen,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}
