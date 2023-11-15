package org.shadow.studio.concatenate.frontend

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.shadow.studio.concatenate.frontend.homepage.startGame

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = {
            text = "Hello, Desktop!!"
        }) {
            Text(text)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun main() = application {
    var boxIndex by remember { mutableStateOf(0) }


    Window(onCloseRequest = ::exitApplication, title = "Concatenate Minecraft Launcher") {
        Scaffold(backgroundColor = Color.LightGray,
            topBar = {
                TopAppBar(backgroundColor = Color.LightGray.copy(alpha = 0.95f)) {
                    val text1 by remember { mutableStateOf("首页") }
                    val text2 by remember { mutableStateOf("管理") }
                    val text3 by remember { mutableStateOf("设置") }
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()) {
                        Button(
                            interactionSource = MutableInteractionSource(),
                            enabled = true,
                            onClick = {
                                boxIndex = 0
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor =  if(boxIndex == 0) Color.LightGray.copy(alpha = 1f) else Color.LightGray.copy(alpha = 0.95f),
                                contentColor = if (boxIndex == 0) Color.Black else Color.Black.copy(alpha = 0.2f),
                            ),
                            shape = RoundedCornerShape(50,50,50,50),
                            contentPadding = PaddingValues(
                                start = 30.dp,
                                top = 10.dp,
                                end = 30.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Text(text1)
                        }
                        Button(
                            enabled = true,
                            onClick = {
                                boxIndex = 1
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor =  if(boxIndex == 1) Color.LightGray.copy(alpha = 1f) else Color.LightGray.copy(alpha = 0.95f),
                                contentColor = if (boxIndex == 1) Color.Black else Color.Black.copy(alpha = 0.2f),
                            ),
                            shape = RoundedCornerShape(50,50,50,50),
                            contentPadding = PaddingValues(
                                start = 30.dp,
                                top = 10.dp,
                                end = 30.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Text(text2)
                        }
                        Button(
                            enabled = true,
                            onClick = {
                                boxIndex = 2
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor =  if(boxIndex == 2) Color.LightGray.copy(alpha = 1f) else Color.LightGray.copy(alpha = 0.95f),
                                contentColor = if (boxIndex == 2) Color.Black else Color.Black.copy(alpha = 0.2f),
                            ),
                            shape = RoundedCornerShape(50,50,50,50),
                            contentPadding = PaddingValues(
                                start = 30.dp,
                                top = 10.dp,
                                end = 30.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Text(text3)
                        }
                    }
                }
            }
        ) {
            PaddingValues(
                start = 100.dp
            )
        }
        Box(
            modifier = Modifier.fillMaxSize()
                .fillMaxHeight(0.9f)
                .padding(top = 60.dp)
        ) {
            if (boxIndex == 0) {
                startGame()
            }
        }
    }
}

