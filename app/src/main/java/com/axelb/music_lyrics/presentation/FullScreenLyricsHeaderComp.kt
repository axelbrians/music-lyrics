package com.axelb.music_lyrics.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axelb.music_lyrics.R
import com.axelb.music_lyrics.ui.theme.MyColor

@Composable
fun FullScreenLyricsComp(
  modifier: Modifier = Modifier
) {
  Row(modifier = modifier) {
    Image(painter = painterResource(
      id = R.drawable.thumbnail_the_other_side),
      contentDescription = "The Other Side - Thumbnail",
      modifier = Modifier
        .size(72.dp)
        .shadow(elevation = 8.dp)
    )

    Column(modifier = Modifier
      .padding(start = 18.dp, top = 6.dp)
      .weight(1f)
    ) {
      Text(
        text = "The Other Side",
        fontSize = 16.sp,
        fontWeight = FontWeight.W600,
        color = MyColor.White
      )

      Text(
        text = "Hugh Jackman",
        color = MyColor.White,
        fontSize = 12.sp,
        fontWeight = FontWeight.W400,
        modifier = Modifier.padding(top = 6.dp)
      )
    }

    Box(modifier = Modifier
      .clip(RoundedCornerShape(50))
      .background(Color(0f, 0f, 0f, 0.3f))
      .padding(2.dp)
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_close_line),
        contentDescription = "Play",
        modifier = Modifier.size(32.dp),
        tint = MyColor.White
      )
    }
  }
}