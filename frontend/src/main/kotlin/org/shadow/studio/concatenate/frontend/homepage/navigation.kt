package org.shadow.studio.concatenate.frontend.homepage

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun navigationHomepage() {
    Box(modifier = Modifier.fillMaxHeight()
        .padding(start = 0.dp)
        .width(200.dp)
        .background(Color.LightGray.copy(0.25f))) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()
                .height(250.dp)) {
                Column {
                    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
                    val isHovered by interactionSource.collectIsHoveredAsState()
                    Text(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                        fontSize = 15.sp,
                        text = "账户")
                    Divider(color = Color.DarkGray,
                        thickness = 1.dp,
                        startIndent = 5.dp,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp, end = 5.dp))
                    Box(modifier = Modifier.fillMaxWidth()
                        .hoverable(interactionSource = interactionSource, true)
                        .height(75.dp)
                        .background(if (isHovered) Color.LightGray.copy(0.5f) else Color.Unspecified)){
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
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    text = "G_Breeze")
                                Text(modifier = Modifier.padding(top = 5.dp, start = 15.dp).width(75.dp),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
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
                    val interactionSource1: MutableInteractionSource = remember { MutableInteractionSource() }
                    val isHovered1 by interactionSource1.collectIsHoveredAsState()
                    val interactionSource2: MutableInteractionSource = remember { MutableInteractionSource() }
                    val isHovered2 by interactionSource2.collectIsHoveredAsState()
                    val interactionSource3: MutableInteractionSource = remember { MutableInteractionSource() }
                    val isHovered3 by interactionSource3.collectIsHoveredAsState()
                    Text(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                        fontSize = 15.sp,
                        text = "游戏")
                    Divider(color = Color.DarkGray,
                        thickness = 1.dp,
                        startIndent = 5.dp,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 5.dp, bottom = 5.dp, end = 5.dp))
                    Box(modifier = Modifier.fillMaxWidth()
                        .hoverable(interactionSource = interactionSource1, true)
                        .height(75.dp)
                        .background(if (isHovered1) Color.LightGray.copy(0.5f) else Color.Unspecified)){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource("G_Breeze_avatar.png"),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.height(50.dp).width(50.dp).padding(top = 20.dp, start = 10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                            Column {
                                Text(modifier = Modifier.padding(top = 20.dp, start = 15.dp).width(125.dp),
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    text = "2.33-不存在的整合包和版本")
                                Text(modifier = Modifier.padding(top = 5.dp, start = 15.dp).width(125.dp),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color(0, 170, 230),
                                    text = "作者:xxx")
                            }
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth()
                        .hoverable(interactionSource = interactionSource2, true)
                        .height(55.dp)
                        .padding(top = 5.dp, bottom = 5.dp)
                        .background(if (isHovered2) Color.LightGray.copy(0.5f) else Color.Unspecified)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource("icons/list.png"),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.height(55.dp).width(55.dp).padding(start = 10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                            Text(modifier = Modifier.padding(start = 10.dp).width(125.dp),
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                text = "游戏/版本列表")
                        }
                    }
//                    Box(modifier = Modifier.fillMaxWidth()
//                        .hoverable(interactionSource = interactionSource3, true)
//                        .height(55.dp)
//                        .padding(top = 5.dp, bottom = 5.dp)
//                        .background(if (isHovered3) Color.LightGray.copy(0.8f) else Color.Unspecified)) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Image(
//                                painter = painterResource("icons/arrow_down.png"),
//                                contentDescription = null,
//                                contentScale = ContentScale.Fit,
//                                modifier = Modifier.height(55.dp).width(55.dp).padding(start = 10.dp)
//                                    .clip(RoundedCornerShape(2.dp))
//                            )
//                            Text(modifier = Modifier.padding(start = 10.dp).width(125.dp),
//                                fontSize = 14.sp,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis,
//                                text = "游戏/整合包下载")
//                        }
//                    }
                }
            }
        }
    }
}