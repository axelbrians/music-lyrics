package com.axelb.music_lyrics.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axelb.music_lyrics.ui.theme.MyColor

@Composable
fun RowLyricsItemComposable(
  modifier: Modifier = Modifier,
  lyrics: String,
  isActive: Boolean
) {
  Text(
    text = lyrics,
    fontSize = 18.sp,
    fontWeight = FontWeight.W600,
    color = if (isActive) {
      MyColor.White
    } else {
      MyColor.Black
    },
    modifier = Modifier.padding(vertical = 2.dp)
  )
}