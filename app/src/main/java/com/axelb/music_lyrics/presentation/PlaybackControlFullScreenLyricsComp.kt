package com.axelb.music_lyrics.presentation

import android.widget.SeekBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintLayoutScope
import com.axelb.music_lyrics.R
import com.axelb.music_lyrics.ui.theme.MyColor

@Composable
fun PlaybackControlFullScreenLyricsComp(
  modifier: Modifier = Modifier,
  seekBarView: SeekBar,
  playerElapsedText: String,
  playerDurationText: String,
  onClick: () -> Unit
) {
  ConstraintLayout(
    modifier = modifier
  ) {
    val (
      sliderRef,
      elapsedTimeRef,
      totalTimeRef,
      playbackControlsRef,
      shareButtonRef
    ) = createRefs()

    AndroidView(
      factory = { _ ->
        seekBarView
      },
      modifier = Modifier
        .constrainAs(sliderRef) {
          top.linkTo(parent.top)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
        }
        .fillMaxWidth()
        .padding(horizontal = 12.dp, vertical = 0.dp)
    )

    Text(
      text = playerElapsedText,
      color = MyColor.Grey70,
      fontSize = 12.sp,
      fontWeight = FontWeight.W400,
      modifier = Modifier
        .padding(start = 12.dp, end = 12.dp)
        .constrainAs(elapsedTimeRef) {
          top.linkTo(sliderRef.bottom)
          start.linkTo(sliderRef.start)
        }
    )
    Text(
      text = playerDurationText,
      color = MyColor.Grey70,
      fontSize = 12.sp,
      fontWeight = FontWeight.W400,
      modifier = Modifier
        .padding(start = 12.dp, end = 12.dp)
        .constrainAs(totalTimeRef) {
          top.linkTo(sliderRef.bottom)
          end.linkTo(sliderRef.end)
        }
    )

    PlaybackControlsButtonComposable(
      constraintRef = playbackControlsRef,
      constraintTop = sliderRef.bottom,
      onClick = { onClick() }
    )
  }
}

@Composable
fun ConstraintLayoutScope.PlaybackControlsButtonComposable(
  constraintRef: ConstrainedLayoutReference,
  constraintTop: ConstraintLayoutBaseScope.HorizontalAnchor,
  onClick: () -> Unit
) {
  Icon(
    painter = painterResource(id = R.drawable.ic_play_filled),
    contentDescription = "Playback Button",
    modifier = Modifier
      .padding(bottom = 28.dp)
      .size(72.dp)
      .constrainAs(constraintRef) {
        top.linkTo(constraintTop)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
      }
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = { onClick() }
      )
  )
}