package org.shadow.studio.concatenate.frontend

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.shadow.studio.concatenate.frontend.data.User
import org.shadow.studio.concatenate.frontend.pages.divider
import org.shadow.studio.concatenate.frontend.pages.gamePage
import org.shadow.studio.concatenate.frontend.pages.invisibleButton
import org.shadow.studio.concatenate.frontend.pages.navigationHomepage
import org.shadow.studio.concatenate.frontend.util.openUrl
import java.lang.Thread.sleep

@OptIn(ExperimentalMaterialApi::class)
fun main() = application {
    var boxIndex by remember { mutableStateOf(0) }
    var isDarkMod by remember { mutableStateOf(false) }
    val topbarColor by animateColorAsState(if (isDarkMod) Color.DarkGray else Color(10, 165, 230).copy(alpha = 0.8f))
    val pageColor by animateColorAsState(if (isDarkMod) Color.Gray else Color.White)

    Window(onCloseRequest = ::exitApplication, title = "Concatenate Minecraft Launcher", resizable = false) {
        Scaffold(modifier = Modifier.border(width = 1.dp, color = Color.Unspecified),
            backgroundColor = topbarColor,
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
                                ,text = "游戏")
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
                                ,text = "关于")
                        }
                    }
            }
        ) {
        }
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(top = 55.dp)
                .background(color = pageColor)
        ) {
            if (boxIndex == 0) {
                navigationHomepage()
            } else if (boxIndex == 1) {
                gamePage()
            } else if (boxIndex == 2) {
                Box() {
                    Column {
                        Text(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                            fontSize = 18.sp,
                            color = Color(0, 170, 230),
                            text = "关于我们")
                        divider(Color.DarkGray, 5)
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)) {
                            Text(modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                                fontSize = 15.sp,
                                text = "Github项目地址：")
                            Box(modifier = Modifier.fillMaxWidth(0.8f)) {
                                Text(modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                                    fontSize = 15.sp,
                                    color = Color(0, 170, 230),
                                    textDecoration = TextDecoration.Underline,
                                    text = "https://github.com/ShadowStudioOrganization/Concatenate")
                                invisibleButton { openUrl("https://github.com/ShadowStudioOrganization/Concatenate") }
                            }
                            Box(modifier = Modifier.fillMaxHeight().width(80.dp)
                                .background(color = Color.White.copy(alpha = 0f))) {
                                Button(modifier = Modifier.size(35.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.LightGray.copy(0.35f),
                                        contentColor = Color.Unspecified
                                    ),
                                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                        pressedElevation = 0.dp,
                                        disabledElevation = 0.dp,
                                        hoveredElevation = 0.dp,
                                        focusedElevation = 0.dp),
                                    onClick = {
                                        openUrl("https://github.com/ShadowStudioOrganization/Concatenate")
                                    }) {
                                }
                                Image(painter = painterResource("icons/open_url.png"),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.size(32.dp).padding(top = 5.dp, start = 5.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}


