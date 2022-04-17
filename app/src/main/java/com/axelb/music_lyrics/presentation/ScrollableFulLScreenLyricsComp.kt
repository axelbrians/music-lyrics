package com.axelb.music_lyrics.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.axelb.music_lyrics.core.linearGradientBackground
import com.axelb.music_lyrics.presentation.model.LyricsTimestampData
import com.axelb.music_lyrics.ui.theme.MyColor

@Composable
fun BoxScope.ScrollableFullScreenLyricsComp(
  modifier: Modifier = Modifier,
  lazyListState: LazyListState = rememberLazyListState(),
  lyricsDataSet: List<LyricsTimestampData>,
  animatedFloat: Float,
  playerPosition: Long
) {
  Box(
    modifier = Modifier
      .align(Alignment.TopCenter)
      .fillMaxWidth()
      .height(64.dp)
      .zIndex(2f)
      .alpha(animatedFloat)
      .linearGradientBackground(
        0f to MyColor.PrimaryBlue.copy(alpha = 1f),
        1f to MyColor.PrimaryBlue.copy(alpha = 0f),
        angle = -90f
      )
  )
  Box(
    modifier = Modifier
      .align(Alignment.BottomCenter)
      .fillMaxWidth()
      .height(64.dp)
      .zIndex(2f)
      .linearGradientBackground(
        0f to MyColor.PrimaryBlue.copy(alpha = 1f),
        1f to MyColor.PrimaryBlue.copy(alpha = 0f),
        angle = 90f
      )
  )

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    state = lazyListState,
    contentPadding = PaddingValues(vertical = 2.dp, horizontal = 20.dp),
    horizontalAlignment = Alignment.Start,
  ) {
    items(
      lyricsDataSet,
      key = { it.timestampInMillis }
    ) { item ->
      RowLyricsItemComposable(
        lyrics = item.lyrics,
        isActive = playerPosition > item.timestampInMillis
      )
    }

    item(key = -1) {
      Text(
        text = "Licensed and provided by Nobody",
        fontSize = 13.sp,
        fontWeight = FontWeight.W600,
        color = MyColor.Black,
        modifier = Modifier.padding(top = 120.dp, bottom = 48.dp)
      )
    }
  }
}