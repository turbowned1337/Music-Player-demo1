package com.example.musicplayer.presentation

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.doOnLayout
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.load
import coil.request.ImageResult
import coil.request.SuccessResult
import coil.transition.CrossfadeTransition
import coil.transition.Transition
import coil.transition.TransitionTarget
import com.example.musicplayer.R
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.utils.PlayerState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

@ExperimentalCoilApi
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val loginViewModel by viewModel<LoginViewModel>()

    val playerViewModel by viewModel<PlayerViewModel>()

    val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navHostFragment.navController
    }

    private val trackIndicatorView by lazy { binding.bottomSheet.trackIndicator }

    private val playerView by lazy { binding.bottomSheet.playerLayout }

    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(binding.bottomSheet.root) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (!loginViewModel.isLogged()) {
            navController.navigate(R.id.loginFragment)
        }

        setNavigationBar()
        setBottomSheet()
        setButtons()
        setPlayer()
        setCoroutines()
    }

    private fun setProgressBars() {
        trackIndicatorView.progressBar.max = playerViewModel.getSongDuration()
        playerView.seekBar.max = playerViewModel.getSongDuration()

        playerView.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) {
                    playerViewModel.seekInSong(p1)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
    }

    private fun setNavigationBar() {
        binding.navigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.page_1 -> {
                    if (navController.currentDestination?.id != R.id.mainScreenFragment) {
                        navController.navigate(R.id.mainScreenFragment)
                    }
                    true
                }
                R.id.page_2 -> {
                    if (navController.currentDestination?.id != R.id.searchFragment) {
                        navController.navigate(R.id.searchFragment)
                    }
                    true
                }
                R.id.page_3 -> {
                    if (navController.currentDestination?.id != R.id.favouritesFragment) {
                        navController.navigate(R.id.favouritesFragment)
                    }
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment || destination.id == R.id.registerFragment
            ) {
                binding.navigationMenu.visibility = View.GONE
                binding.bottomSheet.root.visibility = View.GONE
            } else if (binding.navigationMenu.visibility != View.VISIBLE) {
                binding.navigationMenu.visibility = View.VISIBLE
                binding.bottomSheet.root.visibility = View.VISIBLE
            }

            when (destination.id) {
                R.id.mainScreenFragment -> binding.navigationMenu.menu
                    .getItem(0).isChecked = true
                R.id.searchFragment -> binding.navigationMenu.menu
                    .getItem(1).isChecked = true
                R.id.favouritesFragment -> binding.navigationMenu.menu
                    .getItem(2).isChecked = true
            }
        }
    }

    private fun setBottomSheet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.navigationMenu.foreground = ColorDrawable(getColor(R.color.background_grey))
            binding.navigationMenu.foreground.alpha = 0
        }

        trackIndicatorView.root.doOnLayout {
            bottomSheetBehavior.peekHeight = trackIndicatorView.root.height
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.navigationMenu.menu.forEach {
                            it.isEnabled = false
                        }
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.navigationMenu.menu.forEach {
                            it.isEnabled = true
                        }
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                trackIndicatorView.root.alpha = 1 - slideOffset
                binding.navigationMenu.foreground.alpha = (slideOffset * 255).toInt()
            }
        })
    }

    private fun setButtons() {
        trackIndicatorView.playButton.setOnClickListener {
            playerViewModel.togglePause()
        }

        playerView.playButton.setOnClickListener {
            playerViewModel.togglePause()
        }

        playerView.menuButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        trackIndicatorView.root.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        trackIndicatorView.likeButton.setOnClickListener {
            val newSongInfo = playerViewModel.currentTrackData.value
            if (newSongInfo != null) {
                newSongInfo.isFavorite = !newSongInfo.isFavorite
                playerViewModel.updateSong(newSongInfo)
                if (newSongInfo.isFavorite) {
                    trackIndicatorView.likeButton.setImageResource(R.drawable.heart_pressed)
                    playerView.likeButton.setImageResource(R.drawable.heart_pressed)
                } else {
                    trackIndicatorView.likeButton.setImageResource(R.drawable.heart)
                    playerView.likeButton.setImageResource(R.drawable.heart)
                }
            }
        }

        playerView.likeButton.setOnClickListener {
            val newSongInfo = playerViewModel.currentTrackData.value
            if (newSongInfo != null) {
                newSongInfo.isFavorite = !newSongInfo.isFavorite
                playerViewModel.updateSong(newSongInfo)
                if (newSongInfo.isFavorite) {
                    playerView.likeButton.setImageResource(R.drawable.heart_pressed)
                    trackIndicatorView.likeButton.setImageResource(R.drawable.heart_pressed)
                } else {
                    playerView.likeButton.setImageResource(R.drawable.heart)
                    trackIndicatorView.likeButton.setImageResource(R.drawable.heart)
                }
            }
        }

        playerView.nextButton.setOnClickListener {
            playerView.seekBar.progress = 0
            playerViewModel.nextTrack()
        }

        playerView.previousButton.setOnClickListener {
            playerView.seekBar.progress = 0
            playerViewModel.previousTrack()
        }
    }

    private fun setPlayer() {
        playerViewModel.currentTrackData.observe(this, {
            val durationMinutes = TimeUnit.MILLISECONDS.toMinutes(it.duration.toLong()) % 60
            val durationSeconds = TimeUnit.MILLISECONDS.toSeconds(it.duration.toLong()) % 60

            trackIndicatorView.apply {
                progressBar.progress = 0
                playButton.visibility = View.INVISIBLE
                indicatorPerformerText.text = it.performer
                indicatorTrackText.text = it.name
            }

            playerView.apply {
                songName.text = it.name
                performerText.text = it.performer
                timeFull.text = String.format("%d:%02d", durationMinutes, durationSeconds)
                playButton.visibility = View.INVISIBLE

                albumCover.load(it.coverURL) {
                    error(R.drawable.placeholder)
                    transition(object : Transition {
                        val crossfadeTransition = CrossfadeTransition(250)

                        override suspend fun transition(
                            target: TransitionTarget,
                            result: ImageResult
                        ) {
                            crossfadeTransition.transition(
                                target,
                                (result as? SuccessResult)?.takeIf { it ->
                                    it.metadata.dataSource == DataSource.MEMORY_CACHE
                                }?.run {
                                    copy(metadata = metadata.copy(dataSource = DataSource.MEMORY))
                                } ?: result)
                        }
                    })
                }
            }

            if (it.isFavorite) {
                trackIndicatorView.likeButton.setImageResource(R.drawable.heart_pressed)
                playerView.likeButton.setImageResource(R.drawable.heart_pressed)
            } else {
                trackIndicatorView.likeButton.setImageResource(R.drawable.heart)
                playerView.likeButton.setImageResource(R.drawable.heart)
            }
        })

        playerViewModel.playerState.observe(this, {
            when (it) {
                PlayerState.PREPARED -> {
                    trackIndicatorView.playButton.visibility = View.VISIBLE
                    playerView.playButton.visibility = View.VISIBLE
                    trackIndicatorView.playButton.setImageResource(R.drawable.pause_icon_small)

                    playerView.playButton.setImageResource(R.drawable.pause_icon)
                    setProgressBars()
                }
                PlayerState.PAUSED -> {
                    trackIndicatorView.playButton.visibility = View.VISIBLE
                    trackIndicatorView.playButton.setImageResource(R.drawable.play_icon_small)

                    playerView.playButton.setImageResource(R.drawable.play_icon)
                }
                PlayerState.PLAYING -> {
                    trackIndicatorView.playButton.visibility = View.VISIBLE
                    trackIndicatorView.playButton.setImageResource(R.drawable.pause_icon_small)

                    playerView.playButton.setImageResource(R.drawable.pause_icon)
                }
                PlayerState.ENDED -> {
                    playerViewModel.nextTrack()
                }
                else -> {}
            }
        })
    }

    private fun setCoroutines() {
        lifecycleScope.launch {
            while (isActive) {
                if (playerViewModel.isTrackPlaying()) {
                    val currentPosition = playerViewModel.getPlayerPosition()

                    trackIndicatorView.progressBar.progress = currentPosition
                    playerView.seekBar.progress = currentPosition
                }
                delay(20)
            }
        }

        lifecycleScope.launch {
            while (isActive) {
                if (playerViewModel.isTrackPlaying()) {
                    val currentPosition = playerViewModel.getPlayerPosition()
                    val durationMinutes =
                        TimeUnit.MILLISECONDS.toMinutes(currentPosition.toLong()) % 60
                    val durationSeconds =
                        TimeUnit.MILLISECONDS.toSeconds(currentPosition.toLong()) % 60

                    playerView.timeCurrent.text =
                        String.format("%d:%02d", durationMinutes, durationSeconds)
                }
                delay(1000)
            }
        }
    }
}