package com.example.FLO.Adapter.ComponentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.databinding.FragmentMainHomeBanner1ViewpageBinding
import com.google.android.material.tabs.TabLayoutMediator

//여러개의 이미지를 프레그먼트에 넣기 위해 이미지 소스를 인자로 받음
//이미지 소스의 타입은 Int(R.drawable로 연결되기 때문)
class Banner1Fragment(val imgRes : Int) : Fragment() {
    lateinit var binding : FragmentMainHomeBanner1ViewpageBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainHomeBanner1ViewpageBinding.inflate(inflater, container, false)

        //인자로 받은 이미지를 이미지로 설정해서 보임임
       binding.bannerImageIv.setImageResource(imgRes)

        return binding.root
    }
}