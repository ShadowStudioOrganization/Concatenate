package org.shadow.studio.concatenate.frontend.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.shadow.studio.concatenate.frontend.data.User
import org.shadow.studio.concatenate.frontend.util.openUrl

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun navigationHomepage(currentUser: User) {
    var boxIndex by remember { mutableStateOf(0) }
    var currentUser by remember { mutableStateOf(currentUser) }
    if (boxIndex == 0) {
        startGame()
    }
    Row {
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
                                Box(modifier = Modifier.fillMaxHeight(0.35f).fillMaxWidth(0.5f).padding(top = 15.dp)
                                    .background(color = Color.White.copy(alpha = 0f))) {
                                    Button(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.85f).padding(start = 16.dp),
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
                                            boxIndex = 1
                                        }) {
                                    }
                                    Image(painter = painterResource(if(currentUser.userName == "") "icons/add.png" else "G_Breeze_avatar.png"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxHeight().fillMaxWidth()
                                            .clip(RoundedCornerShape(2.dp)))
                                }
                                Text(textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 20.dp).width(150.dp),
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    text = if(currentUser.userName == "") "暂无用户" else currentUser.userName)
                                Text(textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 5.dp).width(95.dp),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color(0, 170, 230),
                                    text = if(currentUser.userName == "") "未登录" else currentUser.loginType)
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
                                                boxIndex = 1
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
                    }
                }
            }
        }
        if (boxIndex == 1) {
            Box(modifier = Modifier
                .fillMaxSize()) {
                Box(modifier = Modifier.size(height = 50.dp, width = 35.dp)
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
                            boxIndex = 0
                        }) {
                    }
                    Image(painter = painterResource("icons/back.png"),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxHeight(0.8f).fillMaxWidth(0.9f).padding(start = 5.dp, top = 10.dp))
                }
                var addUser by remember { mutableStateOf(false) }
                var testList = remember { mutableListOf(User(userName = "G_Breeze", uuid = "12345", token = "null", refreshToken = "null", loginType = "微软"),
                    User(userName = "whiterasbk", uuid = "12345", token = "null", refreshToken = "null", loginType = "微软"),
                    User(userName = "whiterasbk", uuid = "12345", token = "null", refreshToken = "null", loginType = "微软"),
                    User(userName = "whiterasbk", uuid = "12345", token = "null", refreshToken = "null", loginType = "微软"),
                    User(userName = "whiterasbk", uuid = "12345", token = "null", refreshToken = "null", loginType = "微软"),
                    User(userName = "whiterasbk", uuid = "12345", token = "null", refreshToken = "null", loginType = "微软"),
                ) }
                /*
                * add user page
                * */
                if (addUser) {
                    var userName by remember { mutableStateOf("") }
                    val focus = remember { FocusRequester() }
                    AlertDialog(
                        onDismissRequest = {addUser = false},
                        title = { Text(text = "添加用户(离线)") },
                        text = {
                            Column {
                                Divider(color = Color.DarkGray,
                                    thickness = 1.dp,
                                    startIndent = 5.dp,
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(top = 5.dp, bottom = 5.dp, end = 5.dp))
                                TextField(
                                    modifier = Modifier.focusRequester(focus),
                                    value = userName,
                                    onValueChange = { userName = it },
                                    label = { Text("用户名:") },
                                    singleLine = true,
                                    textStyle = TextStyle(color = Color.Black),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        errorIndicatorColor = Color.Transparent),
                                    shape = RoundedCornerShape(12.dp),

                                    )
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                testList.add(User(userName,"","","","离线"))
                                addUser = false
                            }) {
                                Text("确认")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {addUser = false}) {
                                Text("取消")
                            }
                        }
                    )
                    focus.requestFocus()
                }
                /*
                * user manage page
                * */
                Column {
                    Row {
                        Text(modifier = Modifier.padding(start = 50.dp, top = 15.dp),
                            fontSize = 17.sp,
                            color = Color(0, 170, 230),
                            text = "所有账户")
                        Box(modifier = Modifier
                            .padding(top = 5.dp, start = 275.dp)
                            .size(height = 40.dp, width = 115.dp)
                            .background(color = Color.White.copy(alpha = 0f))) {
                            Button(modifier = Modifier
                                .fillMaxSize(),
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
                                    addUser = true
                                }) {
                            }
                            Row {
                                Text(modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 5.dp),
                                    fontSize = 16.sp,
                                    color = Color(0, 170, 230),
                                    text = "添加用户")
                                Divider(color = Color.LightGray,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(1.dp))
                                Image(painter = painterResource("icons/add.png"),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.height(32.dp).width(28.dp).padding(start = 5.dp, top = 10.dp))
                            }
                        }
                    }
                    Divider(color = Color.Gray,
                        thickness = 1.dp,
                        startIndent = 5.dp,
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 5.dp, bottom = 10.dp, end = 5.dp))
                    Surface {
                        val scrollState = rememberScrollState()
                        Column(modifier = Modifier
                            .verticalScroll(scrollState)) {
                            if (testList.isNotEmpty()) {
                                testList.forEach() {
                                    Box() {
                                        Button(modifier = Modifier.fillMaxWidth().height(50.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color.Unspecified,
                                                contentColor = Color.White.copy(0f)
                                            ),
                                            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                                pressedElevation = 0.dp,
                                                disabledElevation = 0.dp,
                                                hoveredElevation = 0.dp,
                                                focusedElevation = 0.dp),
                                            onClick = {
                                                currentUser = it
                                            }) {
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                painter = painterResource("G_Breeze_avatar.png"),
                                                contentDescription = null,
                                                contentScale = ContentScale.FillBounds,
                                                modifier = Modifier.size(height = 50.dp, width = 75.dp).padding(start = 10.dp, end = 15.dp)
                                                    .clip(RoundedCornerShape(2.dp))
                                            )
                                            Column(modifier = Modifier.width(400.dp)) {
                                                Text(fontSize = 16.sp,
                                                    text = it.userName)
                                                Text(fontSize = 11.sp,
                                                    color = Color(0, 170, 230),
                                                    text = it.loginType)
                                            }
                                            Box(modifier = Modifier.size(height = 40.dp, width = 40.dp)
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
                                                        testList.remove(it)
                                                        addUser = true
                                                        addUser = false
                                                    }) {
                                                }
                                                Image(painter = painterResource("icons/exit_red.png"),
                                                    contentDescription = null,
                                                    contentScale = ContentScale.FillBounds,
                                                    modifier = Modifier.fillMaxSize(0.8f).padding(top = 10.dp, start = 10.dp))
                                            }
                                        }
                                    }
                                    Divider(color = Color.LightGray,
                                        thickness = 1.dp,
                                        startIndent = 10.dp,
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(top = 5.dp, bottom = 5.dp, end = 10.dp))
                                }
                            } else {
                                Text(modifier = Modifier
                                    .fillMaxSize().height(500.dp)
                                    .padding(start = 10.dp, top = 10.dp, end = 5.dp),
                                    fontSize = 26.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Red,
                                    text = "暂无账户,请添加/登录!")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun startGame() {
    Box {
//        Image(
//            painter = painterResource("background.jpg"),
//            contentDescription = null,
//            contentScale = ContentScale.FillBounds,
//            modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(start = 200.dp)
//                .blur(radius = 2.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
//        )
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