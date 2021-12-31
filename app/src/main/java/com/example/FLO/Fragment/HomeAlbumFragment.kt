package com.example.FLO.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.Activity.MainActivity
import com.example.FLO.Adapter.AlbumViewpagerAdapter
import com.example.FLO.Data.Album
import com.example.FLO.R
import com.example.FLO.databinding.FragmentMainHomeAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class HomeAlbumFragment : Fragment() {

    lateinit var binding : FragmentMainHomeAlbumBinding

    private val gson = Gson()

    //탭 레이아웃에 들어갈 선택 목록 이름 list(arrayList)
    val information = arrayListOf("수록곡", "상세정보", "영상")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //프레그먼트에서 바인딩 초기화
        binding = FragmentMainHomeAlbumBinding.inflate(inflater, container, false)

        //HomeFragment에서 보낸 앨범 정보 받아오는 코드
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)
        renderingAlbumData(album)

        val albumAdapter = AlbumViewpagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter

        //탭 레이아웃을 뷰 페이저와 연결하는 중재자 역할(뷰 페이저 스크롤과 탭 선택을 동기화해줌)
        TabLayoutMediator(binding.albumContentTabLayout, binding.albumContentVp) {
            //코틀린 문법(람다식) : tab, position에 따라 다음을 수행 -> position에 따른 tab의 제목(text)를 information[position]으로 정함
                tab, position -> tab.text = information[position]
        }.attach()


        /*---------- 리스너 영역 ----------*/


        /* 뒤로가기 버튼을 누르면 HomeFragment로 전환 */
        binding.albumBackIb.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment()) //홈 프레그먼트로 변경
                .commitAllowingStateLoss()
        }

        return binding.root
    }


    /*---------- 함수 영역 ----------*/


    private fun renderingAlbumData(album: Album) {
        binding.songTitleTv.text = album.title
        binding.songSingerTv.text = album.singer
        binding.songAlbumcoverIv.setImageResource(album.coverImg!!)
    }
}