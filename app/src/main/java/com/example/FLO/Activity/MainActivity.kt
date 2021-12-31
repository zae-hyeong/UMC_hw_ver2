package com.example.FLO.Activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.FLO.Data.Album
import com.example.FLO.Data.Song
import com.example.FLO.Fragment.HomeFragment
import com.example.FLO.Fragment.LockerFragment
import com.example.FLO.Fragment.LookFragment
import com.example.FLO.Fragment.SearchFragment
import com.example.FLO.R
import com.example.FLO.databinding.ActivityMainBinding
import com.example.FLO.Data.SongDatabase


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    //Gson 객체 초기화
    //private var gson : Gson = Gson()

    //song 객체 초기화
    private var song : Song = Song()

    // 미디어 플레이어
    private var mediaPlayer: MediaPlayer? = null


    /* ---------- 오버라이딩 한 함수 ---------- */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()  //처음 Navigator 설정 및 세팅

        //노래 추가(더미데이터)
        //var song1 = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString())
        //val song1 = Song("물풍경시티 bgm", "포켓몬스터 ost", 0, 90, R.drawable.img_watercity_album, false,"watercity")
        // -> 이제 DB에 더미데이터 추가
        inputDummySongs()  //DB에 더미데이터 추가
        inputDummyAlbums()

        initClickListner() //버튼 리스너 설정


    }

    //액티비티가 실행되면 실행되는 함수(초기화)
    override fun onStart() {
        super.onStart()

        //핸드폰에 저장되어있던 sharedPreferences의 정보를 가져옴
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) //PRIVATE 모드 : 다른 앱에서 접근하지 못하도록 함
        val songId = sharedPreferences.getInt("song", 0) //sharedPreferences에 저장되어있던 데이터를 가져옴

//        song = if(jsonSong == null) { //앱을 처음 시작해서 sharedPreference 정보가 없다면, 이걸로 초기화
//            Song("물풍경시티 bgm", "포켓몬스터 ost", 0, 90, R.drawable.img_watercity_album, false,"watercity")
//        }else { //아니면 sharedPreference에서 가져온 데이터(Json)를 객체로 만들기 by Gson
//            gson.fromJson(jsonSong, Song::class.java)
//        }

        //songDB 인스턴스 생성 후 Dao로 노래 하나를 가져옴
        val songDB = SongDatabase.getInstance(this)!!
        song = if (songId == 0) {  //저장한 값이 없으면, 1번 노래를 가져옴(디폴트)
            songDB.songDao().getSong(1)
        } else {  //아니면, 위에서 가져온 노래를 사용함!
            songDB.songDao().getSong(songId)
        }

        //가져온 노래로 하단 미니 플레이어 세팅!
        setMiniPlayer(song)
    }


    /* ---------- 추가적인 함수의 영역 ---------- */


    //리스너 설정
    private fun initClickListner() {
        /*하단 플레이어바 클릭시 song 액티비티 실행*/
        binding.mainPlayerLayout.setOnClickListener {
            //startActivity(Intent(this, SongActivity::class.java))
            //Intent로 song 정보 전달(위에서 만든 song 더미데이터
//            val intent = Intent(this, SongActivity::class.java)
//            intent.putExtra("title", song1.title)
//            intent.putExtra("singer", song1.singer)
//            intent.putExtra("curTime", song1.curTime)
//            intent.putExtra("playTime", song1.playTime)
//            intent.putExtra("isPlaying", song1.isPlaying)
//            intent.putExtra("albumImg", song1.albumImg)
//            intent.putExtra("musicRes", song1.musicRes)
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this@MainActivity, SongActivity::class.java)

            startActivity(intent)
        }

        binding.mainMiniplayerPlayBtn.setOnClickListener {
            setPlayStatus(true)
        }

        binding.mainMiniplayerPauseBtn.setOnClickListener {
            setPlayStatus(false)
        }
    }

    //Navigation 바 초기화
    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.popBackStack("homeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .addToBackStack("homeFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.popBackStack("lookFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .addToBackStack("lookFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.popBackStack("searchFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .addToBackStack("searchFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.popBackStack("lockerFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .addToBackStack("lockerFragment")
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }
    }

    //음악 재생/멈춤 설정
    private fun setPlayStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.mainMiniplayerPauseBtn.visibility = View.VISIBLE
            binding.mainMiniplayerPlayBtn.visibility = View.GONE
        }
        else {
            binding.mainMiniplayerPauseBtn.visibility = View.GONE
            binding.mainMiniplayerPlayBtn.visibility = View.VISIBLE
        }
        song.isPlaying = isPlaying
    }

    //하단 미니 플레이어 바 뷰 랜더링
    private fun setMiniPlayer(song : Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerSb.progress = (song.curTime*1000)/song.playTime

        val music = resources.getIdentifier(song.musicRes, "raw", this.packageName)

        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isPlaying) {
            binding.mainMiniplayerPlayBtn.visibility = View.VISIBLE
            binding.mainMiniplayerPauseBtn.visibility = View.GONE
        }
        else {
            binding.mainMiniplayerPlayBtn.visibility = View.GONE
            binding.mainMiniplayerPauseBtn.visibility = View.VISIBLE
        }
    }

    //ROOM_DB : song dummy data 삽입(실제로는 서버에서 가져오는거임)
    private fun inputDummySongs() {
        val songDB = SongDatabase.getInstance(this)!!   //songDatabase의 instance를 가져옴 -> songDao를 사용할 수 있게 됨!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return  //이미 데이터베이스에 데이터가 들어갔으면 바로 리턴!

        songDB.songDao().insert(
            Song(
                "Lilac",
                "아이유 (IU)",
                R.drawable.img_album_exp2,
                "music_lilac",
                0,
                200,
                false,
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Flu",
                "아이유 (IU)",
                R.drawable.img_album_exp2,
                "music_lilac",
                0,
                200,
                false,
                false,
                1
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp,
                "music_lilac",
                0,
                190,
                false,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter (Hotter Remix)",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp,
                "music_lilac",
                0,
                190,
                false,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Butter (Sweeter Remix)",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp,
                "music_lilac",
                0,
                190,
                false,
                false,
                2
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                R.drawable.img_album_exp3,
                "music_lilac",
                0,
                210,
                false,
                false,
                3
            )
        )

        songDB.songDao().insert(
            Song(
                "Next Level (IMLAY Remix)",
                "에스파 (AESPA)",
                R.drawable.img_album_exp3,
                "music_lilac",
                0,
                210,
                false,
                false,
                3
            )
        )

        songDB.songDao().insert(
            Song(
                "Boy with Luv",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp4,
                "music_lilac",
                0,
                230,
                false,
                false,
                4
            )
        )

        songDB.songDao().insert(
            Song(
                "소우주 (Mikrokosmos)",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp4,
                "music_lilac",
                0,
                230,
                false,
                false,
                4
            )
        )

        songDB.songDao().insert(
            Song(
                "Make It Right",
                "방탄소년단 (BTS)",
                R.drawable.img_album_exp4,
                "music_lilac",
                0,
                230,
                false,
                false,
                4
            )
        )

        songDB.songDao().insert(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                R.drawable.img_album_exp5,
                "music_lilac",
                0,
                240,
                false,
                false,
                5
            )
        )

        songDB.songDao().insert(
            Song(
                "궁금해",
                "모모랜드 (MOMOLAND)",
                R.drawable.img_album_exp5,
                "music_lilac",
                0,
                240,
                false,
                false,
                5
            )
        )
        //val _songs = songDB.songDao().getSongs()  //더미데이터가 잘 들어왔는지 확인하기 위해 들어간 데이터를 가져와서 로그를 띄움
        //Log.d("DB DATA", _songs.toString())

    }

    //ROOM_DB
    private fun inputDummyAlbums() {
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) return

        songDB.albumDao().insert(
                Album(
                        1,
                        "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.img_album_exp2
                )
        )

        songDB.albumDao().insert(
                Album(
                        2,
                        "Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp
                )
        )

        songDB.albumDao().insert(
                Album(
                        3,
                        "iScreaM Vol.10 : Next Level Remixes", "에스파 (AESPA)", R.drawable.img_album_exp3
                )
        )

        songDB.albumDao().insert(
                Album(
                        4,
                        "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)", R.drawable.img_album_exp4
                )
        )

        songDB.albumDao().insert(
                Album(
                        5,
                        "GREAT!", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5
                )
        )

    }


}

