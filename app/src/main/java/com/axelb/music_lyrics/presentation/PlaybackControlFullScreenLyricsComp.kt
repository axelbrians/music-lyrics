package com.axelb.music_lyrics.presentation

import android.view.RoundedCorner
import android.widget.SeekBar
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
  isPlaying: Boolean,
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
      drawableId = if (isPlaying) R.drawable.img_pause_filled else R.drawable.img_play_filled,
      onClick = { onClick() }
    )
  }
}

@Composable
fun ConstraintLayoutScope.PlaybackControlsButtonComposable(
  constraintRef: ConstrainedLayoutReference,
  constraintTop: ConstraintLayoutBaseScope.HorizontalAnchor,
  @DrawableRes drawableId: Int,
  onClick: () -> Unit
) {
  Image(
    painter = painterResource(id = drawableId),
    contentDescription = "Playback Button",
    contentScale = ContentScale.Crop,
    modifier = Modifier
      .padding(bottom = 28.dp)
      .size(64.dp)
      .constrainAs(constraintRef) {
        top.linkTo(constraintTop)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
      }
      .clip(RoundedCornerShape(50))
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(
          bounded = true,
          radius = 64.dp,
        ),
        onClick = { onClick() }
      )
  )
}