package com.example.FLO.Adapter.ComponentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.databinding.FragmentMainLookerMusicfileBinding

class LookerMusicfileFragment : Fragment() {

    lateinit var binding : FragmentMainLookerMusicfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainLookerMusicfileBinding.inflate(inflater, container, false)

        return binding.root
    }
}