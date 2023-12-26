package org.shadow.studio.concatenate.frontend.util

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun divider(color: Color, cutDistance: Int) {
    Divider(color = color,
        thickness = 1.dp,
        startIndent = cutDistance.dp,
        modifier = Modifier.fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp, end = cutDistance.dp))
}

@Composable
fun verticalDivider(color: Color, wide: Int ) {
    Divider(color = color,
        modifier = Modifier
            .fillMaxHeight()
            .width(wide.dp))
}