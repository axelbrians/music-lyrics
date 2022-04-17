package com.axelb.music_lyrics.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.axelb.music_lyrics.R
import com.axelb.music_lyrics.core.linearGradientBackground
import com.axelb.music_lyrics.presentation.model.LyricsTimestampData
import com.axelb.music_lyrics.ui.theme.MyColor
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.delay
import timber.log.Timber

@Composable
fun ScrollableFullScreenLyricsScreen(
	modifier: Modifier = Modifier,
	exoPlayer: ExoPlayer
) {
	val lyricsDataSet = remember { LyricsTimestampData.getTheOtherSideLyrics() }
	val isPlaying = remember { mutableStateOf(false) }
	val shouldUpdateProgress = remember { mutableStateOf(true) }
	val playerPosition = remember { mutableStateOf(0L) }
	val playerElapsedText = remember { mutableStateOf("00:00") }
	val playerDurationText = remember { mutableStateOf("00:00") }
	val scrollableLyricsOffset = remember { mutableStateOf(0)}
	val currentPlayingLyricsIndex = remember { mutableStateOf(0) }
	val lazyListState = rememberLazyListState()

	val animatedFloat = animateFloatAsState(
		targetValue = if ((lazyListState.firstVisibleItemIndex == 0 &&
				lazyListState.firstVisibleItemScrollOffset == 0)) 0f else 1f,
		animationSpec = tween(durationMillis = 300, easing = LinearEasing)
	)

	Timber.d("animatedFloat: ${animatedFloat.value}")


	val onSeekbarChangeListener = object : SeekBar.OnSeekBarChangeListener {
		override fun onProgressChanged(
			seekBar: SeekBar?,
			progress: Int,
			fromUser: Boolean
		) {
			if (fromUser) {
				exoPlayer.seekTo(progress.toLong() * 1_000)
			}
		}

		override fun onStartTrackingTouch(seekBar: SeekBar?) {
			shouldUpdateProgress.value = false
		}

		override fun onStopTrackingTouch(seekBar: SeekBar?) {
			shouldUpdateProgress.value = true
		}
	}

	val seekBarView = (LayoutInflater.from(LocalContext.current).inflate(
		R.layout.playback_seekbar,
		null
	) as SeekBar).apply {
		setOnSeekBarChangeListener(onSeekbarChangeListener)
		this.setPadding(20, 0, 0, 20)
		this.max = 0
	}

	LaunchedEffect(exoPlayer.currentMediaItem) {
		while (true) {
			seekBarView.max = (exoPlayer.duration / 1_000).toInt()
			if (exoPlayer.duration / 1_000 > 1) {
				break
			}
			delay(200)
		}
	}

	LaunchedEffect(exoPlayer) {
		while (true) {
			if (shouldUpdateProgress.value) {
				playerPosition.value = exoPlayer.currentPosition
				seekBarView.progress = (playerPosition.value / 1_000).toInt()
			}
			if (isPlaying.value) {
				val nextIndex = lyricsDataSet.indexOfFirst {
					it.timestampInMillis > playerPosition.value
				}

				if (nextIndex >= 0 && nextIndex != currentPlayingLyricsIndex.value) {
					currentPlayingLyricsIndex.value = nextIndex
				}

				lazyListState.animateScrollToItem(
					currentPlayingLyricsIndex.value,
					scrollOffset = -scrollableLyricsOffset.value
				)
			}
			playerElapsedText.value = longMillisToTime(exoPlayer.currentPosition)
			playerDurationText.value = longMillisToTime(exoPlayer.duration)
			delay(200)
		}
	}

	Column(modifier = modifier) {
		FullScreenLyricsComp(
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = 24.dp, top = 24.dp, end = 24.dp)
		)

		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 18.dp, bottom = 8.dp)
				.weight(1f)
				.onGloballyPositioned {
					scrollableLyricsOffset.value = it.size.height / 2
				}
		) {
			ScrollableFullScreenLyricsComp(
				lazyListState = lazyListState,
				lyricsDataSet = lyricsDataSet,
				animatedFloat = animatedFloat.value,
				playerPosition = playerPosition.value
			)
		}

		PlaybackControlFullScreenLyricsComp(
			modifier = Modifier.fillMaxWidth(),
			seekBarView = seekBarView,
			playerElapsedText = playerElapsedText.value,
			playerDurationText = playerDurationText.value,
			onClick = {
				if (isPlaying.value) {
					exoPlayer.pause()
					isPlaying.value = false
				} else {
					exoPlayer.play()
					isPlaying.value = true
				}
			}
		)
	}
}

private fun floatToTime(time: Float): String {
	val minutes = (time / 60).toInt()
	val seconds = (time % 60).toInt()
	return String.format("%02d:%02d", minutes, seconds)
}

private fun longMillisToTime(time: Long): String {
	val minutes = (time / 1_000 / 60).toInt()
	val seconds = (time / 1_000 % 60).toInt()
	return String.format("%02d:%02d", minutes, seconds)
}

