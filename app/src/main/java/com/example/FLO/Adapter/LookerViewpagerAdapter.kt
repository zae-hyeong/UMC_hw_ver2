package com.example.FLO.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.FLO.Adapter.ComponentFragment.LookerMusicfileFragment
import com.example.FLO.Adapter.ComponentFragment.LookerStorageFragment

class LookerViewpagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LookerStorageFragment()
            else -> LookerMusicfileFragment()
        }
    }
}