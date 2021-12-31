package com.example.FLO.Adapter.ComponentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.databinding.FragmentMainHomeAlbumSonglistBinding

class AlbumSonglistFragment : Fragment() {
    lateinit var binding : FragmentMainHomeAlbumSonglistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainHomeAlbumSonglistBinding.inflate(inflater, container, false)

        return binding.root
    }
}