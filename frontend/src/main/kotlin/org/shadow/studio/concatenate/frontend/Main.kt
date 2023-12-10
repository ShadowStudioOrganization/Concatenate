package org.shadow.studio.concatenate.frontend

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.shadow.studio.concatenate.frontend.data.User
import org.shadow.studio.concatenate.frontend.pages.navigationHomepage

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

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
fun main() = application {
    var boxIndex by remember { mutableStateOf(0) }
    var text1 by remember { mutableStateOf("管理") }
    var text2 by remember { mutableStateOf("设置") }
    var currentUser by remember { mutableStateOf(User(userName = "", uuid = "12345", token = "null", refreshToken = "null", "微软")) }

    Window(onCloseRequest = ::exitApplication, title = "Concatenate Minecraft Launcher", resizable = false) {
        Scaffold(modifier = Modifier.border(width = 1.dp, color = Color.White.copy(0f)),
            backgroundColor = Color(10, 165, 230).copy(alpha = 0.8f),
            topBar = {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()) {
                        Button(
                            modifier = Modifier.padding(start = 75.dp)
                                .align(Alignment.CenterVertically)
                                .height(55.dp),
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                disabledElevation = 0.dp,
                                hoveredElevation = 2.dp,
                                focusedElevation = 0.dp),
                            onClick = {
                                boxIndex = 0
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor =  if(boxIndex == 0) Color.White.copy(alpha = 0.7f) else Color.White.copy(0.1f),
                                contentColor = if (boxIndex == 0) Color.Black else Color.Black.copy(alpha = 0.4f),
                            ),
                            shape = RoundedCornerShape(0,0,0,0),
                            contentPadding = PaddingValues(
                                start = 30.dp,
                                top = 10.dp,
                                end = 30.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Text(fontSize = 15.sp
                                ,text = "首页")
                        }
                        Button(
                            modifier = Modifier.padding(start = 20.dp)
                                .align(Alignment.CenterVertically)
                                .height(55.dp),
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                disabledElevation = 0.dp,
                                hoveredElevation = 2.dp,
                                focusedElevation = 0.dp),
                            onClick = {
                                boxIndex = 1
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor =  if(boxIndex == 1) Color.White.copy(alpha = 0.7f) else Color.White.copy(0.1f),
                                contentColor = if (boxIndex == 1) Color.Black else Color.Black.copy(alpha = 0.4f),
                            ),
                            shape = RoundedCornerShape(0,0,0,0),
                            contentPadding = PaddingValues(
                                start = 30.dp,
                                top = 10.dp,
                                end = 30.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Text(fontSize = 15.sp
                                ,text = "管理")
                        }
                        Button(
                            modifier = Modifier.padding(start = 350.dp)
                                .align(Alignment.CenterVertically)
                                .height(55.dp),
                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                disabledElevation = 0.dp,
                                hoveredElevation = 2.dp,
                                focusedElevation = 0.dp),
                            onClick = {
                                boxIndex = 2
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor =  if(boxIndex == 2) Color.White.copy(alpha = 0.7f) else Color.White.copy(0.1f),
                                contentColor = if (boxIndex == 2) Color.Black else Color.Black.copy(alpha = 0.4f),
                            ),
                            shape = RoundedCornerShape(0,0,0,0),
                            contentPadding = PaddingValues(
                                start = 30.dp,
                                top = 10.dp,
                                end = 30.dp,
                                bottom = 10.dp
                            )
                        ) {
                            Text(fontSize = 15.sp
                                ,text = "设置")
                        }
                    }
            }
        ) {
        }
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(top = 55.dp)
                .background(color = Color.White)
        ) {
            if (boxIndex == 0) {
                    navigationHomepage(currentUser)
            } else if (boxIndex == 1) {
                Button(onClick = {
                    text1 = "管理(todo)"
                }) {
                    Text(text1)
                }
            } else if (boxIndex == 2) {
                    Button(onClick = {
                        text2 = "设置(todo)"
                    }) {
                        Text(text2)
                    }
            }
        }
    }
}


