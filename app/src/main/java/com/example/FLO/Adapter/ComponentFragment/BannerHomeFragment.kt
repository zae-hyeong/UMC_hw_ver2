package com.example.FLO.Adapter.ComponentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.databinding.FragmentMainHomeBannerTopViewpageBinding

class BannerHomeFragment(val bannerImg : Int) : Fragment() {

    lateinit var binding : FragmentMainHomeBannerTopViewpageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainHomeBannerTopViewpageBinding.inflate(inflater, container, false)

        binding.homeBackgroundIv.setImageResource(bannerImg)

        return binding.root
    }
}