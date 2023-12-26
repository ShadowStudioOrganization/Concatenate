package org.shadow.studio.concatenate.frontend.pages

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.shadow.studio.concatenate.frontend.data.Mod
import org.shadow.studio.concatenate.frontend.data.User
import org.shadow.studio.concatenate.frontend.util.*

@Composable
fun gamePage() {
    var boxIndex by remember { mutableStateOf(0) }
    var ramSize by remember { mutableStateOf("2048") }
    var jvmArgs by remember { mutableStateOf("") }
    var select1 by remember { mutableStateOf(false) }
    var typeMap = remember { mutableMapOf(0 to "原版", 1 to "整合包", 2 to "模组") }
    var searchKey by remember { mutableStateOf("") }
    var currentType by remember { mutableStateOf(1) }
    var testModList = remember { mutableListOf(Mod("mod1","1.0","unknown",true),
        Mod("mod2","1.0","unknown",true),
        Mod("mod3","1.0","unknown",true),
        Mod("mod4","1.0","unknown",true),
        Mod("mod5","1.0","unknown",true),
        Mod("mod6","1.0","unknown",true)) }
    Row {
        Box(modifier = Modifier.fillMaxHeight()
            .width(200.dp)
            .background(Color.LightGray.copy(0.25f))) {
            Column {
                var selectedIndex by remember { mutableStateOf(0) }
                val interactionSource1: MutableInteractionSource = remember { MutableInteractionSource() }
                val isHovered1 by interactionSource1.collectIsHoveredAsState()
                val interactionSource2: MutableInteractionSource = remember { MutableInteractionSource() }
                val isHovered2 by interactionSource2.collectIsHoveredAsState()
                val interactionSource3: MutableInteractionSource = remember { MutableInteractionSource() }
                val isHovered3 by interactionSource3.collectIsHoveredAsState()
                Box(modifier = Modifier.fillMaxWidth()
                    .hoverable(interactionSource = interactionSource1, true)
                    .clickable(onClick = {
                        selectedIndex = 0
                        boxIndex = 0
                    })
                    .height(65.dp)
                    .background(if (isHovered1) Color.LightGray.copy(0.1f) else if (selectedIndex == 0) Color.White.copy(0.9f) else Color.Unspecified)){
                    Row(modifier = Modifier.height(65.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(if(selectedIndex == 0) "icons/gear.png" else "icons/gear_black.png"),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.height(50.dp).width(50.dp).padding(start = 10.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )
                        Text(modifier = Modifier.padding(start = 10.dp),
                            fontSize = 15.sp,
                            color = if (selectedIndex == 0) Color(0, 170, 230) else Color.Black,
                            text = "全局设置")
                    }
                }
                Box(modifier = Modifier.fillMaxWidth()
                    .hoverable(interactionSource = interactionSource2, true)
                    .clickable(onClick = {
                        selectedIndex = 1
                        boxIndex = 1
                    })
                    .height(65.dp)
                    .background(if (isHovered2) Color.LightGray.copy(0.1f) else if (selectedIndex == 1) Color.White.copy(0.9f) else Color.Unspecified)){
                    Row(modifier = Modifier.height(65.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(if(selectedIndex == 1) "icons/list_blue.png" else "icons/list.png"),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.height(50.dp).width(50.dp).padding(start = 10.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )
                        Text(modifier = Modifier.padding(start = 10.dp),
                            fontSize = 15.sp,
                            color = if (selectedIndex == 1) Color(0, 170, 230) else Color.Black,
                            text = "模组管理")
                    }
                }
                Box(modifier = Modifier.fillMaxWidth()
                    .hoverable(interactionSource = interactionSource3, true)
                    .clickable(onClick = {
                        selectedIndex = 2
                        boxIndex = 2
                    })
                    .height(65.dp)
                    .background(if (isHovered3) Color.LightGray.copy(0.1f) else if (selectedIndex == 2) Color.White.copy(0.9f) else Color.Unspecified)){
                    Row(modifier = Modifier.height(65.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(if(selectedIndex == 2) "icons/arrow_down_blue.png" else "icons/arrow_down.png"),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.height(50.dp).width(50.dp).padding(start = 10.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )
                        Text(modifier = Modifier.padding(start = 10.dp),
                            fontSize = 15.sp,
                            color = if (selectedIndex == 2) Color(0, 170, 230) else Color.Black,
                            text = "游戏下载")
                    }
                }
                Divider(color = Color.DarkGray,
                    thickness = 1.dp,
                    startIndent = 5.dp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp, end = 5.dp))
            }
        }
        if (boxIndex == 0) {
            /*
             * global settings page
             * */
            Column{
                Text(modifier = Modifier.padding(start = 50.dp, top = 15.dp),
                    fontSize = 17.sp,
                    color = Color(0, 170, 230),
                    text = "全局")
                Divider(color = Color.Gray,
                    thickness = 1.dp,
                    startIndent = 5.dp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 5.dp, bottom = 10.dp, end = 5.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = "内存:",
                        fontSize = 18.sp
                    )
                    Row {
                        Slider(
                            value = ramSize.toFloat(),
                            onValueChange = {ramSize = it.toInt().toString()},
                            valueRange = 1024f..16384f,
                            steps = 15,
                            modifier = Modifier
                                .width(400.dp)
                                .border(BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(2.dp))
                        )
                        TextField(
                            value = ramSize,
                            onValueChange = {ramSize = it},
                            singleLine = true,
                            modifier = Modifier
                                .width(85.dp)
                                .height(48.dp),
                            textStyle = TextStyle(
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            ),

                            )
                    }
                    Text(
                        text = "MB",
                        fontSize = 18.sp
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = "jvm参数:",
                        fontSize = 18.sp
                    )
                    TextField(
                        value = jvmArgs,
                        onValueChange = {jvmArgs = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(48.dp),
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        ),

                        )
                }
            }
        } else if (boxIndex == 1) {
            /*
             * mods manage page
             * */
            Column {
                Text(modifier = Modifier.padding(start = 50.dp, top = 15.dp),
                    fontSize = 17.sp,
                    color = Color(0, 170, 230),
                    text = "模组")
                Divider(color = Color.Gray,
                    thickness = 1.dp,
                    startIndent = 5.dp,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 5.dp, bottom = 10.dp, end = 5.dp))
                Surface(color = Color.LightGray.copy(alpha = 0.2f)) {
                    val scrollState = rememberScrollState()
                    Column(modifier = Modifier
                        .verticalScroll(scrollState)) {
                        if (testModList.isNotEmpty()) {
                            testModList.forEach() {
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
                                            it.isEnabled = !it.isEnabled
                                            boxIndex = 0
                                            boxIndex = 1
                                        }) {
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource("icons/mod_default.png"),
                                            contentDescription = null,
                                            contentScale = ContentScale.FillBounds,
                                            modifier = Modifier.size(height = 50.dp, width = 75.dp).padding(start = 10.dp, end = 15.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                        )
                                        Column(modifier = Modifier.width(400.dp)) {
                                            Text(fontSize = 16.sp,
                                                text = it.modName)
                                            Text(fontSize = 11.sp,
                                                color = Color(0, 170, 230),
                                                text = "作者-"+it.author+" 版本-"+it.version)
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
                                                    it.isEnabled = !it.isEnabled
                                                    boxIndex = 0
                                                    boxIndex = 1
                                                }) {
                                            }
                                            Image(painter = painterResource(if(it.isEnabled) "icons/enable.png" else "icons/disable.png"),
                                                contentDescription = null,
                                                contentScale = ContentScale.FillBounds,
                                                modifier = Modifier.fillMaxSize(0.85f).padding(top = 5.dp, start = 5.dp))
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
                                text = "未选择游戏/整合包,请先选择!")
                        }
                    }
                }
            }
        } else if (boxIndex == 2) {
            /*
             * download page
             * */
            Column {
                Row {
                    Text(modifier = Modifier.padding(start = 50.dp, top = 15.dp),
                        fontSize = 17.sp,
                        color = Color(0, 170, 230),
                        text = "搜索下载")
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    ) {
                        Text(modifier = Modifier.padding(start = 20.dp, top = 0.dp),
                            fontSize = 16.sp,
                            text = "下载类型:")
                        Box(
                            modifier = Modifier.height(25.dp).width(65.dp).padding(top = 0.dp)
                        ) {
                            lightButton {
                                select1 = !select1
                            }
                            typeMap.get(currentType)?.let {
                                Text(modifier = Modifier.width(65.dp).padding(top = 5.dp),
                                    fontSize = 14.sp,
                                    color = Color(0, 170, 230),
                                    textAlign = TextAlign.Center,
                                    text = it
                                )
                            }
                            DropdownMenu(
                                expanded = select1,
                                onDismissRequest = { select1 = false },
                                modifier = Modifier.width(65.dp)
                            ) {
                                typeMap.forEach {
                                    Box(
                                        modifier = Modifier.width(65.dp).height(30.dp)
                                    ) {
                                        invisibleButton {
                                            currentType = it.key
                                            select1 = !select1
                                        }
                                        Text(
                                            modifier = Modifier.width(65.dp).padding(top = 5.dp),
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Center,
                                            text = it.value
                                        )
                                    }
                                }
                            }
                        }
                        Text(modifier = Modifier.padding(start = 20.dp, top = 0.dp),
                            fontSize = 16.sp,
                            text = "搜索:")
                        Box(
                            modifier = Modifier.height(25.dp).padding(top = 0.dp)
                        ) {
                            BasicTextField(
                                value = searchKey,
                                onValueChange = { searchKey = it },
                                singleLine = true,
                                textStyle = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.height(35.dp).fillMaxWidth(0.8f).background(color = Color.LightGray.copy(0.5f), shape = CircleShape),
                                decorationBox = {
                                    innerTextField ->
                                    Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
                                        Box(modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.CenterStart) {
                                            if (searchKey.isEmpty()) {
                                                Text(
                                                    modifier = Modifier,
                                                    fontSize = 16.sp,
                                                    color = Color(0,0,0,128),
                                                    text = "请输入"
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }
                                }
                            )
                        }
                        Box(modifier = Modifier.size(height = 30.dp, width = 40.dp).padding(start = 0.dp)
                            .background(color = Color.White.copy(alpha = 0f))) {
                            Button(modifier = Modifier.fillMaxSize(),
                                shape = CircleShape,
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
                                    // todo search by key
                                }) {
                            }
                            Image(painter = painterResource("icons/search.png"),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize(0.85f).padding(top = 5.dp, start = 5.dp))
                        }
                    }
                }
                divider(Color.Gray, 5)
                Surface(color = Color.LightGray.copy(alpha = 0.2f)) {
                    val scrollState = rememberScrollState()
                    Column(modifier = Modifier
                        .verticalScroll(scrollState)) {}
                }
            }
        }
    }
}