package org.shadow.studio.concatenate.frontend.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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