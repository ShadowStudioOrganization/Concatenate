package org.shadow.studio.concatenate.frontend.homepage

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*

@Composable
fun startGame() {
    var text by remember { mutableStateOf("启动游戏") }
Button(onClick = {
    text = if (text == "启动游戏") "启动中...(todo)" else "启动游戏"
}) {
    Text(text)
    }
}