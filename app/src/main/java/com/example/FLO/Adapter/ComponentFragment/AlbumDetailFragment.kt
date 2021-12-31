package com.example.FLO.Adapter.ComponentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.databinding.FragmentMainHomeAlbumDetailBinding

class AlbumDetailFragment : Fragment() {
    lateinit var binding : FragmentMainHomeAlbumDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainHomeAlbumDetailBinding.inflate(inflater, container, false)

        return binding.root
    }
}