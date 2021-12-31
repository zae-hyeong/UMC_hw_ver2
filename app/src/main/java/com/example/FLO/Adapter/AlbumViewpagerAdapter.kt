package com.example.FLO.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.FLO.Adapter.ComponentFragment.AlbumDetailFragment
import com.example.FLO.Adapter.ComponentFragment.AlbumSonglistFragment
import com.example.FLO.Adapter.ComponentFragment.AlbumVideoFragment

class AlbumViewpagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    //세개 탭에 대해 만들어주는 것이기 때문에 그냥 3으로 둠
    override fun getItemCount(): Int = 3

    //세개의 프레그먼트와 연결(동적이 아닌 정적으로 연결해줌) -> 배너에서는 이미지를 사용하는 프레그먼트에서 동적으로 추가해줬지만 얜 아님
    override fun createFragment(position: Int): Fragment {
        //when = JAVA에서의 switch문
        return when(position) {
            0-> AlbumSonglistFragment()
            1-> AlbumDetailFragment()
            else-> AlbumVideoFragment()
        }
    }
}