package com.example.FLO.Adapter.ComponentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.databinding.FragmentMainHomeBanner2ViewpageBinding

class Banner2Fragment(val imgRes : Int) : Fragment() {

    lateinit var binding : FragmentMainHomeBanner2ViewpageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainHomeBanner2ViewpageBinding.inflate(inflater, container, false)

        binding.bannerImageIv.setImageResource(imgRes)

        return binding.root
    }
}