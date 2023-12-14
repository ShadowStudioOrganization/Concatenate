package org.shadow.studio.concatenate.frontend.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun startGame() {
    Box {
        var text by remember { mutableStateOf("启动游戏") }
        Button(modifier = Modifier.padding(start = 525.dp, top = 400.dp)
            .width(200.dp)
            .height(75.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0, 155, 0).copy(alpha = 0.7f),
                contentColor = Color(35,35,35)
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                disabledElevation = 0.dp,
                hoveredElevation = 2.dp,
                focusedElevation = 0.dp),
            onClick = {
                text = "启动(todo...)"
            }) {
            Text(fontSize = 20.sp,
                text = text)
        }
    }
}