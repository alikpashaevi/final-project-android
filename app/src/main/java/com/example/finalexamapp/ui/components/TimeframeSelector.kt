package com.example.finalexamapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.finalexamapp.viewmodel.Timeframe

@Composable
fun TimeframeSelector(
    selectedTimeframe: Timeframe,
    onTimeframeSelected: (Timeframe) -> Unit
) {
    val mintContainer = Color(0xFFD8F3DC)
    val forestGreenText = Color(0xFF1B4332)
    val inactiveBg = Color(0xFFF3F4F5)
    val inactiveText = Color(0xFF717973)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(inactiveBg, RoundedCornerShape(24.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Timeframe.entries.forEach { timeframe ->
            val isSelected = selectedTimeframe == timeframe
            val bgColor = if (isSelected) mintContainer else Color.Transparent
            val textColor = if (isSelected) forestGreenText else inactiveText

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .clickable { onTimeframeSelected(timeframe) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = timeframe.name.lowercase().replaceFirstChar { it.uppercase() },
                    color = textColor,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
