package org.shadow.studio.concatenate.frontend.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.shadow.studio.concatenate.frontend.data.Mod
import org.shadow.studio.concatenate.frontend.data.Package
import org.shadow.studio.concatenate.frontend.util.divider

@Composable
fun packageinfo(currentPackage: Package, modList: MutableList<Mod>) {
    Box(modifier = Modifier
        .fillMaxSize()) {
        val scrollState = rememberScrollState()
        Column(modifier = Modifier
            .verticalScroll(scrollState)) {
            Text(modifier = Modifier.padding(15.dp),
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                text = currentPackage.packageName)
            Text(modifier = Modifier.padding(start = 15.dp),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                color = Color(0, 170, 230),
                text = "作者:"+currentPackage.author+" 版本:"+currentPackage.version)
            Row {
                Image(painter = painterResource("icons/unknown_pack.png"),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(150.dp).padding(15.dp))
                Text(modifier = Modifier.width(425.dp).padding(top = 18.dp),
                    fontSize = 16.sp,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    text = currentPackage.brief)
            }
            Text(modifier = Modifier.padding(10.dp),
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                text = "整合包模组")
            divider(Color.DarkGray, 5)
            if (modList.isNotEmpty()) {
                modList.forEach() {
                    Text(modifier = Modifier.padding(start = 18.dp, top = 5.dp, bottom = 5.dp),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                        text = it.modName + " 版本:" + it.version)
                }
            } else {
                Text(modifier = Modifier.padding(10.dp),
                    fontSize = 18.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    text = "该整合包无模组")
            }
        }
    }
}