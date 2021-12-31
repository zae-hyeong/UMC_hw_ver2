package com.example.FLO.Adapter.ComponentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.databinding.FragmentMainHomeAlbumVideoBinding

class AlbumVideoFragment : Fragment() {
    lateinit var binding : FragmentMainHomeAlbumVideoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainHomeAlbumVideoBinding.inflate(inflater, container, false)

        return binding.root
    }
}