package com.example.FLO.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.Adapter.LookerViewpagerAdapter
import com.example.FLO.Adapter.RVLookerStorageAdapter
import com.example.FLO.Data.Album
import com.example.FLO.R
import com.example.FLO.databinding.FragmentMainLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentMainLockerBinding

    //탭 레이아웃에 넣어줄 탭 목록
    val tabInfo = arrayListOf("저장된 곡", "음악파일")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainLockerBinding.inflate(inflater, container, false)

        val pageAdapter = LookerViewpagerAdapter(this)
        binding.lookerListVp.adapter = pageAdapter

        TabLayoutMediator(binding.lookerTablayout, binding.lookerListVp) {
            tab, position -> tab.text = tabInfo[position]
        }.attach()

        return binding.root
    }


}