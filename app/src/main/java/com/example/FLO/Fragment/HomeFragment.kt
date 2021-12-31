package com.example.FLO.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.FLO.Activity.MainActivity
import com.example.FLO.Adapter.*
import com.example.FLO.Adapter.ComponentFragment.Banner1Fragment
import com.example.FLO.Adapter.ComponentFragment.Banner2Fragment
import com.example.FLO.Adapter.ComponentFragment.BannerHomeFragment
import com.example.FLO.Data.Album
import com.example.FLO.R
import com.example.FLO.Adapter.RVAlbumListAdapter
import com.example.FLO.Data.SongDatabase
import com.example.FLO.databinding.FragmentMainHomeBinding
import com.google.gson.Gson


class HomeFragment : Fragment() {

    lateinit var binding: FragmentMainHomeBinding

    private var albums = ArrayList<Album>()

    private lateinit var songDB: SongDatabase

    //리사이클러 뷰에 들어갈 앨범들의 리스트
    private var albumDatas =ArrayList<Album>()
    private var albumDatas1 =ArrayList<Album>()
    private var albumDatas2 =ArrayList<Album>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {
        binding = FragmentMainHomeBinding.inflate(inflater, container, false)

        //ROOM_DB
        songDB = SongDatabase.getInstance(requireContext())!!
        albums.addAll(songDB.albumDao().getAlbums()) // songDB에서 album list를 가져옵니다.

        initBanner()

        //앨범 데이터 리스트(ArrayList)에 더미 데이터 생성
//        albumDatas.apply {
//            add(Album(0, "Why Don't You Know", "청하", R.drawable.img_album_chungha_whydontyouknow))
//            add(Album(1, "Undying Love", "베리굿", R.drawable.img_album_verygood_undyinglove))
//            add(Album(2, "첫눈", "에일리", R.drawable.img_album_aile_firstsnow))
//            add(Album(3, "안녕", "안녕", R.drawable.img_album_anyeong_kimjaehwan))
//        }
//        albumDatas1.apply {
//            add(Album(4, "추억을 떠올리며", "흔하지 않은 커플 ost", R.drawable.asdf))
//            add(Album(5, "물풍경시티 bgm", "포켓몬스터", R.drawable.img_watercity_album))
//            add(Album(6, "추억을 떠올리며", "흔하지 않은 커플 ost", R.drawable.asdf))
//            add(Album(7, "물풍경시티 bgm", "포켓몬스터", R.drawable.img_watercity_album))
//        }
//        albumDatas2.apply {
//            add(Album(8, "야생의 포켓몬이 나타났다!", "포켓몬스터", R.drawable.img_album_pokemon_watercity))
//            add(Album(9, "몬스터볼의 노래", "포켓몬스터", R.drawable.img_album_pokemon_battel_exp))
//            add(Album(10, "야생의 포켓몬이 나타났다!", "포켓몬스터", R.drawable.img_album_pokemon_watercity))
//            add(Album(11, "몬스터볼의 노래", "포켓몬스터", R.drawable.img_album_pokemon_battel_exp))
//        }

        //리사이클링 뷰 어댑터 설정 : 더미데이터랑 Adapter 연결
        val RVAlbumListAdapter = RVAlbumListAdapter(albumDatas)

        //만든 리사이클러 뷰를 xml과 연결
        binding.homeTodayAlbumListRecyclerView.adapter = RVAlbumListAdapter
        //레이아웃 매니저 설정 : 레이아웃 배치를 어떻게 할 것인가? (xml 파일에서도 설정 가능하긴 함!)
        binding.homeTodayAlbumListRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val RVAlbumListAdapter1 = RVAlbumListAdapter(albumDatas1)
        binding.homeAlbumList1RecyclerView.adapter = RVAlbumListAdapter1

        val RVAlbumListAdapter2 = RVAlbumListAdapter(albumDatas2)
        binding.homeAlbumList2RecyclerView.adapter = RVAlbumListAdapter2


       /*---------- 리스너 영역 ----------*/


        /*앨범 클릭시 프레그먼트 전환 -> 지금은 리사이클 뷰로 바꿔서 사용중*/
//        binding.homeMusicList1Album1Iv.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, HomeAlbumFragment()) //메인 액티비티의 메인 프레임을 앨범 프레그먼트로 전환
//                    .commitAllowingStateLoss()
//        }

        //Adapter 외부에서 리스너 객체를 전달하기 위한 코드
        RVAlbumListAdapter.setMyItemClickListener(object : RVAlbumListAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeToHomeAlbumFragment(album) //앨범 프레그먼트로 옮기는걸 함수로 넘겨버림 -> 가독성 UP
            }
        })

        return binding.root
    }


    /*---------- 함수 영역 ----------*/


    private fun initBanner() {
        //홈 배너
        val bannerAdapterHome = BannerViewpagerAdapter(this)
        bannerAdapterHome.addFragment(BannerHomeFragment(R.drawable.img_main_banner6))
        bannerAdapterHome.addFragment(BannerHomeFragment(R.drawable.img_main_banner5))
        bannerAdapterHome.addFragment(BannerHomeFragment(R.drawable.img_main_banner3))
        bannerAdapterHome.addFragment(BannerHomeFragment(R.drawable.img_main_banner4))
        binding.homeBannerTopVp.adapter = bannerAdapterHome
        binding.homeBannerTopVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //배터 어댑터라는 변수를 사용하기 위해 우리가 만든 BannerViewpagerAdapter 클래스로 연결
        val bannerAdapter = BannerViewpagerAdapter(this)
        //리스트에 프레그먼트 추가 -> 배너 이미지가 들어가는 BannerFragment를 넣어줌(각 프레그먼트에 들어가는 이미지는 BannerFragment 인자로 전달)
        bannerAdapter.addFragment(Banner1Fragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(Banner1Fragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(Banner1Fragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(Banner1Fragment(R.drawable.img_home_viewpager_exp))

        //fragment_home에 있는 뷰 페이저와 우리가 만든 배너 어댑터를 연결/설정(좌우 스크롤 모드)
        binding.homeBanner1Vp.adapter = bannerAdapter
        binding.homeBanner1Vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //두번째 배너
        val bannerAdapter2 = BannerViewpagerAdapter(this)
        bannerAdapter2.addFragment(Banner2Fragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter2.addFragment(Banner2Fragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter2.addFragment(Banner2Fragment(R.drawable.img_home_viewpager_exp2))
        bannerAdapter2.addFragment(Banner2Fragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBanner2Vp.adapter = bannerAdapter2
        binding.homeBanner2Vp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }

    /*HomeAlbumFragment로 전환하는 함수*/
    private fun changeToHomeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_frm,
                HomeAlbumFragment().apply { //HomeFragment -> HomeAlbumFragment로 데이터 전송을 위해 bundle 사용
                    //전달할 데이터를 bundle로 묶어 arguments에 넣음(번들에 들어가는 데이터는 Json으로 한번에 넣음)
                    arguments = Bundle().apply {
                        val gson = Gson()
                        val albumData = gson.toJson(album)
                        putString("album", albumData)
                    }
                }) //메인 액티비티의 메인 프레임을 앨범 프레그먼트로 전환
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }


}