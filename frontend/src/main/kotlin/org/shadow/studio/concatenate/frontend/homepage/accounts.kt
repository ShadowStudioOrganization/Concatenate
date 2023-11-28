package org.shadow.studio.concatenate.frontend.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.shadow.studio.concatenate.frontend.data.User

@Composable
fun userPage() {
    var testList: List<User> = listOf(User(userName = "G_Breeze", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null"),
        User(userName = "whiterasbk", uuid = "12345", token = "null")
    )
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
                        // TODO: add user
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
                                Text(fontSize = 10.sp,
                                    color = Color(0, 170, 230),
                                    text = "xx登录")
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
                                        // TODO: remove user
                                    }) {
                                }
                                Image(painter = painterResource("icons/exit_red.png"),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier.fillMaxSize(0.8f).padding(top = 10.dp, start = 10.dp))
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