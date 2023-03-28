package com.example.musicplayer.presentation.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentSearchBinding
import com.example.musicplayer.presentation.PlayerViewModel
import com.example.musicplayer.utils.SongListAdapter
import com.example.musicplayer.utils.afterTextChanged
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val playerViewModel by sharedViewModel<PlayerViewModel>()
    private lateinit var rvAdapter: SongListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.topText.applyInsetter {
            type(statusBars = true) {
                margin()
            }
        }

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.root.applyInsetter {
                type(navigationBars = true) {
                    margin()
                }
            }
            return binding.root
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.songListRV

        rvAdapter = SongListAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = rvAdapter

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchEditText.text?.isEmpty() == true) {
                    discardSearch()
                } else {
                    binding.searchEditTextLayout.clearFocus()
                }
            }
            false
        }

        binding.searchEditText.afterTextChanged {
            if (it.isNotEmpty()) {
                searchForSongs(it)
            }
        }
    }

    private fun searchForSongs(query: String) {
        playerViewModel.searchForTracks(query).observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                binding.statusTextView.visibility = View.VISIBLE
                binding.statusTextView.text = getString(R.string.nothing_found_string)
            } else {
                binding.statusTextView.visibility = View.INVISIBLE
            }
            rvAdapter.updateSongList(it)
        })
    }

    private fun discardSearch() {
        binding.searchEditTextLayout.clearFocus()
        binding.statusTextView.visibility = View.VISIBLE
        binding.statusTextView.text = getString(R.string.start_typing_string)
        binding.searchEditText.setText("")
        rvAdapter.updateSongList(listOf())
    }
}