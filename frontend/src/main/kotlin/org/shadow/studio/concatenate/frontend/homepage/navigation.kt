package org.shadow.studio.concatenate.frontend.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun navigationHomepage() {
    var text1 by remember { mutableStateOf("启动游戏") }
    var text2 by remember { mutableStateOf("用户") }
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
                        .background(color = Color.DarkGray.copy(0.5f))) {
                        Text(text = "暂无用户/添加用户...todo")
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