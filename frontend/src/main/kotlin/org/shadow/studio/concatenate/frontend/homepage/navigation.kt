package org.shadow.studio.concatenate.frontend.homepage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun navigationHomepage() {
    Box(modifier = Modifier.fillMaxHeight()
        .padding(start = 0.dp)
        .width(200.dp)
        .background(Color.White.copy(0.35f))) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()
                .height(125.dp)) {
                Column {
                    Text(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                        fontSize = 15.sp,
                        text = "账户")
                    Divider(color = Color.DarkGray,
                        thickness = 1.dp,
                        startIndent = 5.dp,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp, end = 5.dp))
                    Box(modifier = Modifier.fillMaxWidth()
                        .height(75.dp)
                        .background(color = Color.LightGray.copy(alpha = 0.1f))){
                        Row {
                            Image(
                                painter = painterResource("G_Breeze_avatar.png"),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxHeight(0.8f).fillMaxWidth(0.3f).padding(top = 15.dp, start = 10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                            Column {
                                Text(modifier = Modifier.padding(top = 20.dp, start = 15.dp).width(75.dp),
                                    fontSize = 14.sp,
                                    text = "G_Breeze")
                                Text(modifier = Modifier.padding(top = 5.dp, start = 15.dp).width(75.dp),
                                    fontSize = 12.sp,
                                    color = Color(0, 170, 230),
                                    text = "微软")
                            }
                            Box(modifier = Modifier.fillMaxHeight(0.85f).padding(top = 15.dp).background(color = Color.White.copy(alpha = 0f))) {
                                Button(modifier = Modifier.fillMaxHeight().width(45.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.White.copy(0f),
                                        contentColor = Color.White.copy(0f)
                                    ),
                                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                        pressedElevation = 0.dp,
                                        disabledElevation = 0.dp,
                                        hoveredElevation = 0.dp,
                                        focusedElevation = 0.dp),
                                    onClick = {
                                        // TODO: login/switch user
                                    }) {
                                }
                                Image(painter = painterResource("icons/pen.png"),
                                    contentDescription = null,
                                    contentScale = ContentScale.Inside,
                                    modifier = Modifier.fillMaxSize(0.9f).padding(top = 10.dp))
                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth()
                .height(300.dp)) {
                Column {
                    Text(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                        fontSize = 15.sp,
                        text = "游戏")
                    Divider(color = Color.DarkGray,
                        thickness = 1.dp,
                        startIndent = 5.dp,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp, end = 5.dp))
                    Box(modifier = Modifier.fillMaxWidth()
                        .height(250.dp)
                        .background(color = Color.DarkGray.copy(0.5f))) {
                        Text(text = "选择游戏版本/添加游戏版本/下载...todo")
                    }
                }
            }
        }
    }
}