package com.axelb.music_lyrics

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.axelb.music_lyrics.presentation.ScrollableFullScreenLyricsScreen
import com.axelb.music_lyrics.ui.theme.MusicLyricsTheme
import com.axelb.music_lyrics.ui.theme.MyColor
import com.google.accompanist.pager.VerticalPager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.RawResourceDataSource

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		window.decorView.fitsSystemWindows = false
		window.decorView.systemUiVisibility = (
			View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				or View.SYSTEM_UI_FLAG_FULLSCREEN
				or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
		)

		val videoUri = RawResourceDataSource.buildRawResourceUri(R.raw.the_other_side)
		val mediaItem = MediaItem.Builder()
			.setUri(videoUri)
			.setMediaId("the_other_side")
			.setTag(videoUri)
			.setMediaMetadata(
				MediaMetadata.Builder()
					.setDisplayTitle("The Other Side")
					.build()
			).build()

		setContent {
			MusicLyricsTheme {
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MyColor.PrimaryBlue
				) {
					val context = LocalContext.current
					val exoPlayer = remember {
						ExoPlayer.Builder(context).build().apply {
							setPlaybackSpeed(1f)
							setMediaItem(mediaItem)
							prepare()
						}
					}
					DisposableEffect(
						ScrollableFullScreenLyricsScreen(
							modifier = Modifier.fillMaxSize(),
							exoPlayer = exoPlayer
						)
					) {
						onDispose {
							exoPlayer.release()
						}
					}
				}
			}
		}
	}
}