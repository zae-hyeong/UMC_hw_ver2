package com.example.FLO.Adapter.ComponentFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.FLO.Adapter.RVLookerStorageAdapter
import com.example.FLO.Data.Album
import com.example.FLO.databinding.FragmentMainLookerStorageBinding
import com.example.FLO.Data.SongDatabase

class LookerStorageFragment : Fragment() {

    lateinit var binding : FragmentMainLookerStorageBinding

    private var albumDatas = ArrayList<Album>()

    lateinit var songDB: SongDatabase


    /*---------- 오버라이딩 함수 ----------*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentMainLookerStorageBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

//        albumDatas.apply {
//            add(Album("몬스터볼의 노래", "몬스터볼", R.drawable.img_album_pokemon_battel_exp))
//            add(Album("몬스터볼의 노래", "몬스터볼", R.drawable.img_album_pokemon_battel_exp))
//        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.lookerAlbumlistRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter = RVLookerStorageAdapter()
        //리스너 객체 생성 및 전달

        songRVAdapter.setItemClickListener(object : RVLookerStorageAdapter.LookerItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateIsLikeById(false, songId)
            }
        })
        binding.lookerAlbumlistRecyclerview.adapter = songRVAdapter

        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)

    }
}