package org.shadow.studio.concatenate.frontend.util

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun invisibleButton(onClick: () -> Unit) {
    Button(modifier = Modifier.fillMaxSize(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Unspecified,
            contentColor = Color.Unspecified
        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp),
        onClick = onClick) {
    }
}

@Composable
fun lightButton(onClick: () -> Unit) {
    Button(modifier = Modifier.fillMaxSize(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.LightGray.copy(0.5f),
            contentColor = Color.Unspecified
        ),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp),
        onClick = onClick) {
    }
}