package org.shadow.studio.concatenate.frontend.homepage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.shadow.studio.concatenate.frontend.util.openUrl

@Composable
fun navigationHomepage() {
    Box(modifier = Modifier.fillMaxHeight()
        .width(200.dp)
        .background(Color.LightGray.copy(0.25f))) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()
                .height(275.dp)) {
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
                        .background(if (isHovered) Color.LightGray.copy(0.5f) else Color.Unspecified)){
                        Column(modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,) {
                            Image(
                                painter = painterResource("G_Breeze_avatar.png"),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxHeight(0.35f).fillMaxWidth(0.5f).padding(top = 15.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                            Text(textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 20.dp).width(150.dp),
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                text = "G_Breeze")
                            Text(textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 5.dp).width(95.dp),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color(0, 170, 230),
                                text = "微软")
                            Row(modifier = Modifier.fillMaxWidth(0.65f).padding(top = 18.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Box(modifier = Modifier.size(height = 30.dp, width = 40.dp).padding(start = 0.dp)
                                    .background(color = Color.White.copy(alpha = 0f))) {
                                    Button(modifier = Modifier.fillMaxSize(),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.LightGray.copy(0.35f),
                                            contentColor = Color.White.copy(0f)
                                        ),
                                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                            pressedElevation = 0.dp,
                                            disabledElevation = 0.dp,
                                            hoveredElevation = 0.dp,
                                            focusedElevation = 0.dp),
                                        onClick = {
                                            // TODO: download skin and cape
                                        }) {
                                    }
                                    Image(painter = painterResource("icons/cloth.png"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize(0.85f).padding(top = 5.dp, start = 5.dp))
                                }
                                Box(modifier = Modifier.size(height = 30.dp, width = 40.dp).padding(start = 0.dp)
                                    .background(color = Color.White.copy(alpha = 0f))) {
                                    Button(modifier = Modifier.fillMaxSize(),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.LightGray.copy(0.35f),
                                            contentColor = Color.White.copy(0f)
                                        ),
                                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                            pressedElevation = 0.dp,
                                            disabledElevation = 0.dp,
                                            hoveredElevation = 0.dp,
                                            focusedElevation = 0.dp),
                                        onClick = {
                                            // TODO: exit account
                                        }) {
                                    }
                                    Image(painter = painterResource("icons/exit.png"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize(0.85f).padding(top = 5.dp, start = 5.dp))
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth(0.65f).padding(top = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Box(modifier = Modifier.fillMaxHeight().width(80.dp)
                                    .background(color = Color.White.copy(alpha = 0f))) {
                                    Button(modifier = Modifier.fillMaxHeight(0.8f).width(95.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.LightGray.copy(0.35f),
                                            contentColor = Color.White.copy(0f)
                                        ),
                                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                            pressedElevation = 0.dp,
                                            disabledElevation = 0.dp,
                                            hoveredElevation = 0.dp,
                                            focusedElevation = 0.dp),
                                        onClick = {
                                            openUrl("https://www.minecraft.net/zh-hans")
                                        }) {
                                        Text(textAlign = TextAlign.Center,
                                            fontSize = 14.sp,
                                            color = Color(0, 170, 230),
                                            text = "官网")
                                    }
                                }
                                Box(modifier = Modifier.fillMaxHeight().width(45.dp).padding(start = 5.dp)
                                    .background(color = Color.White.copy(alpha = 0f))) {
                                    Button(modifier = Modifier.fillMaxHeight(0.8f).fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            backgroundColor = Color.LightGray.copy(0.35f),
                                            contentColor = Color.White.copy(0f)
                                        ),
                                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                            pressedElevation = 0.dp,
                                            disabledElevation = 0.dp,
                                            hoveredElevation = 0.dp,
                                            focusedElevation = 0.dp),
                                        onClick = {
                                            // TODO: set user
                                        }) {
                                    }
                                    Image(painter = painterResource("icons/gear.png"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxHeight(0.675f).width(65.dp).padding(top = 7.dp))
                                }
                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxWidth()
                .height(200.dp)) {
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