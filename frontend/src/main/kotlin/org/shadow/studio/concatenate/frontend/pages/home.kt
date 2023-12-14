package org.shadow.studio.concatenate.frontend.pages

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.shadow.studio.concatenate.frontend.data.Mod
import org.shadow.studio.concatenate.frontend.data.Package
import org.shadow.studio.concatenate.frontend.data.User
import org.shadow.studio.concatenate.frontend.util.openUrl

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun navigationHomepage() {
    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current

    var subBoxIndex by remember { mutableStateOf(0) }
    var addUser by remember { mutableStateOf(false) }
    var selectPackage by remember { mutableStateOf(false) }
    var currentUser by remember { mutableStateOf(User(userName = "", uuid = "12345", token = "null", refreshToken = "null", "微软")) }
    var currentPackage by remember { mutableStateOf(Package("1.12.2-原版整合(假)测试长度测试长度测试长度", "unknown", "1.0", "简介")) }

    var testList = remember { mutableListOf(User(userName = "G_Breeze", uuid = "12345", token = "null", refreshToken = "null", loginType = "微软"),
        User(userName = "whiterasbk", uuid = "123456", token = "null", refreshToken = "null", loginType = "微软"),
        User(userName = "whiterasbk", uuid = "123457", token = "null", refreshToken = "null", loginType = "微软"),
        User(userName = "whiterasbk", uuid = "123458", token = "null", refreshToken = "null", loginType = "微软"),
        User(userName = "whiterasbk", uuid = "123459", token = "null", refreshToken = "null", loginType = "微软"),
        User(userName = "whiterasbk", uuid = "123455", token = "null", refreshToken = "null", loginType = "微软"),
    ) }
    var testPackageList = remember { mutableListOf(Package("1.12.2-原版整合(假)测试长度测试长度测试长度", "unknown", "1.0", "简介"),
        Package("1.21.2-原版整合(假)", "unknown", "1.0", "简介"),
        Package("1.12.1-原版整合(假)", "unknown", "1.0", "简介"),
        Package("1.9-原版整合(假)", "unknown", "1.0", "简介"),
        Package("1.4.7-原版整合(假)", "unknown", "1.0", "简介"),
        Package("1.16-原版整合(假)", "unknown", "1.0", "简介"),
    ) }
    var testModList = remember { mutableListOf(
        Mod("mod1","1.0","unknown",true),
        Mod("mod2","1.0","unknown",true),
        Mod("mod3","1.0","unknown",true),
        Mod("mod4","1.0","unknown",true),
        Mod("mod5","1.0","unknown",true),
        Mod("mod6","1.0","unknown",true),
        Mod("mod7","1.0","unknown",true),
        Mod("mod8","1.0","unknown",true),
        Mod("mod9","1.0","unknown",true),
        Mod("mod10","1.0","unknown",true),
        Mod("mod11","1.0","unknown",true),
        Mod("mod12","1.0","unknown",true),
        Mod("mod13","1.0","unknown",true),
        Mod("mod14","1.0","unknown",true)
    ) }
    if (subBoxIndex == 0) {
        visible = true
        startGame()
    }
    Row {
        AnimatedVisibility(
            visible = visible,
            enter = expandHorizontally {
                with(density) { -200.dp.roundToPx() }
            },
            exit = shrinkHorizontally()
        ) {
            Box(modifier = Modifier.fillMaxHeight()
                .width(200.dp)
                .background(Color.LightGray.copy(0.25f))) {
//      Image(
//          painter = painterResource("background.jpg"),
//          contentDescription = null,
//          contentScale = ContentScale.FillBounds,
//          modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(start = 200.dp)
//              .blur(radius = 2.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
//        )
                Column {
                    Box(modifier = Modifier.fillMaxWidth()
                        .height(275.dp)) {
                        Column {
                            val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
                            val isHovered by interactionSource.collectIsHoveredAsState()
                            Text(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                                fontSize = 15.sp,
                                text = "账户")
                            divider(Color.DarkGray, 5)
                            Box(modifier = Modifier.fillMaxWidth()
                                .hoverable(interactionSource = interactionSource, true)
                                .background(if (isHovered) Color.LightGray.copy(0.5f) else Color.Unspecified)){
                                invisibleButton { subBoxIndex = 1 }
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
                                                subBoxIndex = 1
                                                if (currentUser.userName == "") {
                                                    addUser = true
                                                }
                                            }) {
                                        }
                                        Image(painter = painterResource(if(currentUser.userName == "") "icons/add.png" else "steve_avatar.png"),
                                            contentDescription = null,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.size(height = 75.dp, width = 100.dp).padding(11.dp)
                                                .clip(RoundedCornerShape(2.dp)))
                                    }
                                    Text(textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 20.dp).width(150.dp).height(16.dp),
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        text = if(currentUser.userName == "") "暂无用户" else currentUser.userName)
                                    Text(textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 5.dp).width(95.dp).height(14.dp),
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
                                                    currentUser = User("","","","","")
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
                                                    subBoxIndex = 1
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
                            val isFocused1 by interactionSource3.collectIsFocusedAsState()
                            Text(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                                fontSize = 15.sp,
                                text = "版本 | 整合包")
                            divider(Color.DarkGray, 5)
                            Box(modifier = Modifier.fillMaxWidth()
                                .hoverable(interactionSource = interactionSource1, true)
                                .height(75.dp)
                                .background(if (isHovered1) Color.LightGray.copy(0.5f) else Color.Unspecified)){
                                invisibleButton { subBoxIndex = 2 }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource("icons/unknown_pack.png"),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.height(55.dp).width(55.dp).padding(top = 20.dp, start = 10.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                    )
                                    Column {
                                        Text(modifier = Modifier.padding(top = 20.dp, start = 1.dp).width(125.dp),
                                            fontSize = 14.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            text = currentPackage.packageName)
                                        Text(modifier = Modifier.padding(top = 5.dp, start = 1.dp).width(125.dp),
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = Color(0, 170, 230),
                                            text = "作者:"+currentPackage.author)
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
                                    Text(modifier = Modifier.padding(start = 10.dp).width(100.dp),
                                        fontSize = 14.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        text = "游戏/版本列表")
                                    Box(modifier = Modifier
                                        .size(height = 44.dp, width = 35.dp)
                                        .background(color = Color.Unspecified)) {
                                        Button(modifier = Modifier.fillMaxSize(),
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
                                                selectPackage = !selectPackage
                                            }) {
                                        }
                                        Image(painter = painterResource("icons/right.png"),
                                            contentDescription = null,
                                            contentScale = ContentScale.FillBounds,
                                            modifier = Modifier.fillMaxHeight(0.8f).fillMaxWidth(0.9f).padding(start = 5.dp, top = 10.dp))
                                        DropdownMenu(
                                            expanded = selectPackage,
                                            onDismissRequest = {selectPackage = false},
                                            modifier = Modifier.width(250.dp)
                                        ) {
                                            if (testPackageList.isNotEmpty()) {
                                                testPackageList.forEach {
                                                    DropdownMenuItem(onClick = {
                                                        currentPackage = it
                                                        selectPackage = false
                                                    }) {
                                                        Box(modifier = Modifier.fillMaxWidth()
                                                            .height(60.dp)){
                                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                                Image(
                                                                    painter = painterResource("icons/unknown_pack.png"),
                                                                    contentDescription = null,
                                                                    contentScale = ContentScale.FillBounds,
                                                                    modifier = Modifier.height(48.dp).width(35.dp).padding(top = 13.dp)
                                                                        .clip(RoundedCornerShape(2.dp))
                                                                )
                                                                Column {
                                                                    Text(modifier = Modifier.padding(top = 13.dp, start = 5.dp).width(200.dp),
                                                                        fontSize = 14.sp,
                                                                        maxLines = 1,
                                                                        overflow = TextOverflow.Ellipsis,
                                                                        text = it.packageName)
                                                                    Text(modifier = Modifier.padding(top = 5.dp, start = 5.dp).width(200.dp),
                                                                        fontSize = 12.sp,
                                                                        maxLines = 1,
                                                                        overflow = TextOverflow.Ellipsis,
                                                                        color = Color(0, 170, 230),
                                                                        text = "作者:"+it.author+" 版本:"+it.version)
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                DropdownMenuItem(onClick = {
                                                    selectPackage = false
                                                }) {
                                                    Text(modifier = Modifier.padding(start = 40.dp),
                                                        fontSize = 18.sp,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        color = Color.Red,
                                                        textAlign = TextAlign.Center,
                                                        text = "暂无游戏|整合包")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (subBoxIndex == 1) {
            Box(modifier = Modifier
                .fillMaxSize()) {
                Box(modifier = Modifier.size(height = 50.dp, width = 35.dp)
                    .background(color = Color.White.copy(alpha = 0f))) {
                    invisibleButton { subBoxIndex = 0 }
                    Image(painter = painterResource("icons/left.png"),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxHeight(0.8f).fillMaxWidth().padding(start = 5.dp, top = 10.dp))
                }
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
                                    shape = RoundedCornerShape(12.dp))
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                val newUser = User(userName,"","","","离线")
                                if (!testList.contains(newUser)) {
                                    currentUser = newUser
                                    testList.add(currentUser)
                                }
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
                                verticalDivider(Color.LightGray, 1)
                                Image(painter = painterResource("icons/add.png"),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.height(32.dp).width(28.dp).padding(start = 5.dp, top = 10.dp))
                            }
                        }
                    }
                    divider(Color.Gray, 5)
                    Surface(color = Color.LightGray.copy(alpha = 0.2f)) {
                        val scrollState = rememberScrollState()
                        Column(modifier = Modifier
                            .verticalScroll(scrollState)) {
                            if (testList.isNotEmpty()) {
                                testList.forEach() {
                                    Box() {
                                        Button(modifier = Modifier.fillMaxWidth().height(50.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color.Unspecified,
                                                contentColor = Color.Unspecified
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
                                                painter = painterResource("steve_avatar.png"),
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
                                                        contentColor = Color.Unspecified
                                                    ),
                                                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp,
                                                        pressedElevation = 0.dp,
                                                        disabledElevation = 0.dp,
                                                        hoveredElevation = 0.dp,
                                                        focusedElevation = 0.dp),
                                                    onClick = {
                                                        testList.remove(it)
                                                        if (currentUser.equals(it)) {
                                                            currentUser = User("","","","","")
                                                        }
                                                        addUser = true
                                                        addUser = false
                                                    }) {
                                                }
                                                Image(painter = painterResource("icons/no.png"),
                                                    contentDescription = null,
                                                    contentScale = ContentScale.FillBounds,
                                                    modifier = Modifier.fillMaxSize(0.85f).padding(top = 5.dp, start = 5.dp))
                                            }
                                        }
                                    }
                                    divider(Color.LightGray, 5)
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
        if (subBoxIndex == 2) {
            packageinfo(currentPackage, testModList)
        }
    }
}