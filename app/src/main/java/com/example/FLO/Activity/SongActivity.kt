package com.example.FLO.Activity

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.FLO.Data.Song
import com.example.FLO.databinding.ActivitySongBinding
import com.example.FLO.Data.SongDatabase

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding

    //메인 액티비티에서 넘어온 음악 정보를 사용하기 위해 데이터를 받을 Song 객체(여기서는 구조체처럼 쓰임)를 전역변수로 선언
    //private var song : Song = Song()
    //-> DB 사용 후 songs라는 song ArrayList에서 index(nowPos)로 접근하는 방식으로 변경
    private var songs = ArrayList<Song>()
    private var nowPos = 0

    //쓰레드 사용을 위한 쓰레드 변수 progress 선언
    private lateinit var sbProgress : SeekBarProgressThread
    //progress 쓰레드의 변경사항을 메인 쓰레드에전달해주기 위해 핸들러 선언
    //private val handler = Handler(Looper.getMainLooper())

    //반복재생 상태를 나타내는 변수
    private var repeatState = 0

    //음악 재생을 위한 media Player 선언. null값이 들어갈 수 있기 때문에 ?를 붙임
    private var mediaPlayer : MediaPlayer? = null;

    //데이터 저장을 위한 gson 객체 생성
    //private var gson : Gson = Gson()

    //song Database 생성
    private lateinit var songDB: SongDatabase


    /* ---------- 오버라이딩 된 함수의 영역 ---------- */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()

        /*intent로 title과 singer를 받았다면(있다면) 제목과 이름 변경
        if(intent.hasExtra("title") && intent.hasExtra("singer")) { //가지고 있다면 true
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")
        }       -> 아래 initSong으로 대체  */
        initSong()

        //쓰레드 객체 생성, 실행
        sbProgress = SeekBarProgressThread(songs[nowPos].playTime, songs[nowPos].isPlaying)
        sbProgress.start()        //무한으로 스레드가 돌아가는 중...

        initClickListener() //버튼 리스너 설정
    }

    //액티비티가 가려졌을때 실행되는 함수
    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()            //미디어 플레이어 중지
        sbProgress.isPlaying = false    //쓰레드 중지

        songs[nowPos].curTime = (binding.songPlayerSb.progress * songs[nowPos].playTime)/1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        //앱을 종료했다가 다시 돌아왔을때 진행정도를 유지하고 싶음 -> 내부 저장소에 저장 by sharedPreference
        //커다란 정보가 아니라 가벼운 설정값 등은 서버에서 가져오지 않고 휴대폰에 저장하는게 좋을 것임
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)  //song 정보 저장

        //그런데 sharedPreference는 데이터를 수정/삭제 등의 조작이 불가능함 -> 에디터 사용
        val editor = sharedPreferences.edit()

        // editor.putString("title", song.title) 원래는 이렇게 하나하나 보내줄 수도 있는데, 이제는 객채에 담아서 Json으로 변환한 뒤 한번에 보낼것임
        //Json 포맷으로 다른 객체에 데이터를 전송, 텍스트 포맷으로 구성('키' : "데이터")
        //근데 또 Json으로 바로 데이터를 바꿀 순 없음 -> Gson으로 변경 후 다시 Json으로 변경(객체 -> Gson -> Json / Json -> Gson -> 객체)
//        val json = gson.toJson(song) //gson을 통해 객체를 Json 데이터로 변환 -> 저장된 객체는 String 한줄로 직렬화 됨
//        editor.putString("song", json) //"키", 데이터(Json)자바 객체를 한번에 전달

        //-> 이제 song data 정보를 메인 액티비티로 넘기려는데 노래 id 값만 보내줌
        editor.putInt("songId", songs[nowPos].id)

        //이제 데이터 저장을 실제로 적용하려면
        editor.apply()
    }

    //액티비티가 종료될때 실행되는 함수
    override fun onDestroy() {
        super.onDestroy()
        sbProgress.interrupt()    //액티비티가 꺼지면 실행중이었던 쓰레드 종료

        mediaPlayer?.release() //리소스 해제 -> 미디어 플레이어가 갖고있는 리소스(노래mp3 파일 연결된거) 해제
        mediaPlayer = null //미디어 플레이어 해제
    }



    /* ---------- 내부 클래스의 영역 ---------- */


    /*SeekBar가 진행되도록 하는 쓰레드 만들기*/
    //자바에서는 클래스 안에 클래스르 넣으면 내부 함수로 사용되어 전역변수를 사용할 수 있었지만,
    //코틀린에서는 내부가 아닌 중첩 클래스로 취부됨 -> inner로 내부 클래스로 만들어줘야함
    inner class SeekBarProgressThread(val playTime : Int, var isPlaying : Boolean) : Thread() {
        var second = 0
        private var mil10sec =0 //쓰레드가 빠르게 멈추기 위헤 10milsecond마다 한번씩 확인
        //쓰레드.start()를 하면 실행되는 함수 -> 이 run() 안에 코드가 끝나면 쓰레드는 종료함
        override fun run() {
            try {
                do { //반복재생이 설정되어 있으면 다시 재생
                    second = 0
                    while (true) {
                        if (second >= playTime) {
                            break //노래가 끝나면 break, 쓰레드를 종료함
                        }
                        //isPlaying이 true면, 매 초마다 second++
                        if (isPlaying) {
                            sleep(10) //쓰레드에서 제공하는 함수
                            mil10sec++
                            if (mil10sec % 100 == 0) { //mil10sec이 100번 됨 -> 1초
                                second++
                                //메인 쓰레드에 메시지(뷰 렌더링을 위한 데이터) 보내기
                                runOnUiThread { // ==  handler.post { : UI 스레드에서 다음 코드를 실행하도록 해라 -> handler로 요청 전송하는 것과 같음
                                    //자바에서는 내부 클래스로 쳐주지만 여기선 그냥 별도의 중첩 클래스가 되기 때문에 별도의 선언이 필요함(inner)
                                    binding.songPlayerSb.progress = second * 1000 / playTime        //진행 정도를 퍼센트로 전달
                                    binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
                                }
                            }
                        }
                    }
                }while (repeatState != 0) //반복재생 설정이 켜져있다면
            }
            //쓰레드는 액티비티가 끝난다고 끝나지 않음 -> 종료시키기 위해 인터럽트 사용
            //인터럽트로 exception이 일어나면, try 구문에서 여기로 넘어오고 while문을 빠져나와 run() 함수가 종료됨
            catch (e : InterruptedException) {
                //Log.d("interrupt", "쓰레드가 종료되었습니다")
            }
        }
    }


    /* ---------- 추가적인 함수의 영역 ---------- */


    /*버튼 리스너 설정 */
    private fun initClickListener() {
        //액티비티 종료
        binding.songDownIb.setOnClickListener { //binding된 레이아웃에서 song_down_ib(Camel로 자동 변환)를 찾아 ClickListener setting
            finish()
        }

        //노래 재생 세팅 변경
        binding.songPlayIv.setOnClickListener {
            songs[nowPos].isPlaying = true
            sbProgress.isPlaying = true //내부 클래스의 필드에 접근 : song의 isPlaying만 바꾼다고 스레드에 적용되지 않음
            mediaPlayer?.start()
            setPlayerStatus(true)
        }
        binding.songPauseIv.setOnClickListener {
            songs[nowPos].isPlaying = false
            sbProgress.isPlaying = false
            mediaPlayer?.pause()
            setPlayerStatus(false)
        }

        //반복재생 세팅 변경
        binding.songRepeatInactiveIv.setOnClickListener {
            repeatState = 1
            setRepeatStatus(1)
        }
        binding.songRepeatActiveIv.setOnClickListener {
            repeatState = 2
            setRepeatStatus(2)
        }
        binding.songRepeatOneIv.setOnClickListener {
            repeatState = 0
            setRepeatStatus(0)
        }

        //랜덤 재생 세팅 변경
        binding.songRandomActiveIv.setOnClickListener {
            setRandomStatus(false)
        }
        binding.songRandomInactiveIv.setOnClickListener {
            setRandomStatus(true)
        }

        //Like 세팅 변경
        binding.songLikeActiveIv.setOnClickListener{
            setLikeStatus(false)
        }
        binding.songLikeInactiveIv.setOnClickListener {
            setLikeStatus(true)
        }

        //다음/이전 노래로 이동
        binding.songSkipPreIv.setOnClickListener{
            moveSong(1)
        }
        binding.songSkipNextIv.setOnClickListener{
            moveSong(-1)
        }

        /*SeekBar 움직임 리스너*/
        binding.songPlayerSb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            private var second = 0

            //값 변경 중 : 인자 progress = 설정 된 값 정보, fromUser = 유저의 터치 정보를 가져오면 true, 코드로 바뀌면 false
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                second = progress * sbProgress.playTime / 1000
                binding.songStartTimeTv.text = String.format("%02d:%02d", second / 60, second % 60)
            }

            //값 변경 시작 : 유저가 값 변경을 위해 터치한 경우
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            //값 변경 종료 : 유저가 값을 변경 후 손을 뗀 경우
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                sbProgress.second = second
            }
        })
    }

    //DB에서 데이터를 가져와 songs로 넣어주는 함수(관리를 위해)
    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!   //DB에서 Instance를 가져와서
        songs.addAll(songDB.songDao().getSongs())           //가져온 데이터를 모두 songs에 넣음
    }

    //노래 데이터를 받아와서 초기화하는 함수
    private fun initSong() {
        //액티비티에 아래와 같은 키를 가진 intent가 있다면 -> 뷰 랜더링
//        if(intent.hasExtra("title") && intent.hasExtra("singer") && intent.hasExtra("playTime")
//            &&intent.hasExtra("albumImg")&&intent.hasExtra("isPlaying") &&intent.hasExtra("curTime")&&intent.hasExtra("musicRes")) {
//            song.title = intent.getStringExtra("title") !!
//            song.singer = intent.getStringExtra("singer") !!
//            song.curTime = intent.getIntExtra("curTime", 0)
//
//            //음악 파일의 이름을 가지고 실제 음악파일(리소스)를 찾아내는 함수
//            val musicRes = resources.getIdentifier(song.musicRes, "raw", this.packageName)
//
//            binding.songStartTimeTv.text = String.format("%02d:%02d", 0, 0)
//            binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
//            binding.songTitleTv.text = song.title
//            binding.songSingerTv.text = song.singer
//            binding.songAlbumIv.setImageResource(song.albumImg)
//            setPlayerStatus(song.isPlaying)

        //현재 miniPlayer에 저장된 노래 ID를 가져옴
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        //songs엔 index로 저장되어있고, 전달받은 노래 정보는 Id로 받아왔음
        // -> 받아온 노래를 찾아야함! (songId == songs[index].id)
        nowPos = getPlayingSongPosition(songId)

        //Log.d("now Song ID",songs[nowPos].id.toString())

        //노래 재생을 위한 미디어 플레이어 생성, 찾아온 음악 파일을 넣어줌
        //mediaPlayer = MediaPlayer.create(this, musicRes)
        setPlayer(songs[nowPos])
    }

    //songId에 해당하는 노래의 index(songs에서의)를 리턴
    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if (songs[i].id == songId){
                return i  //찾은 노래의 index를 리턴
            }
        }
        return 0
    }

    //player 세팅
    private fun setPlayer(song: Song) {
        val music = resources.getIdentifier(song.musicRes, "raw", this.packageName)

        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.curTime / 60, song.curTime % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songAlbumIv.setImageResource(song.albumImg!!)
        binding.songPlayerSb.progress = (song.curTime * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)

        if (song.isLike) {
            setLikeStatus(true)
        } else {
            setLikeStatus(false)
        }

        //플레이어 세팅 할때마다 mediaplayer 생성
        mediaPlayer = MediaPlayer.create(this, music)
    }

    //노래 재생 여부 변경 함수
    private fun setPlayerStatus(isPlaying : Boolean) {
        if(isPlaying) {
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
        else {
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }

    /*노래 반복 재생 설정 아이콘 변경 함수*/
    private fun setRepeatStatus(state : Int) {
        when (state) {
            0 -> {
                binding.songRepeatInactiveIv.visibility = View.VISIBLE
                binding.songRepeatActiveIv.visibility = View.GONE
                binding.songRepeatOneIv.visibility = View.GONE
            }
            1 -> {
                binding.songRepeatInactiveIv.visibility = View.GONE
                binding.songRepeatActiveIv.visibility = View.VISIBLE
                binding.songRepeatOneIv.visibility = View.GONE
            }
            2 -> {
                binding.songRepeatInactiveIv.visibility = View.GONE
                binding.songRepeatActiveIv.visibility = View.GONE
                binding.songRepeatOneIv.visibility = View.VISIBLE
            }
        }
    }

    //랜덤 재생 여부 아이콘 변경
    private fun setRandomStatus(randomStatus : Boolean) {
        if(randomStatus) {
            binding.songRandomActiveIv.visibility = View.VISIBLE
            binding.songRandomInactiveIv.visibility = View.GONE
        }
        else {
            binding.songRandomActiveIv.visibility = View.GONE
            binding.songRandomInactiveIv.visibility = View.VISIBLE
        }
    }

    //좋아요 선택 여부 변경 함수
    private fun setLikeStatus(isLike : Boolean) {
        if(isLike) {
            songs[nowPos].isLike = false
            binding.songLikeActiveIv.visibility = View.VISIBLE
            binding.songLikeInactiveIv.visibility = View.GONE
        }
        else{
            songs[nowPos].isLike = true
            binding.songLikeActiveIv.visibility = View.GONE
            binding.songLikeInactiveIv.visibility = View.VISIBLE
        }

        songDB.songDao().updateIsLikeById(isLike, songs[nowPos].id) //DB의 Like값 업데이트
    }

    //스레드 재실행
    private fun startThread() {
        sbProgress = SeekBarProgressThread(songs[nowPos].playTime, songs[nowPos].isPlaying)
        sbProgress.start()
    }

    //노래 이동하는 함수 : songs의 nowPos 값 조정
    private fun moveSong(direction: Int){
        if (nowPos + direction < 0){  //index가 마이너스가 되지 않도록
            Toast.makeText(this,"이전 음악이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direction >= songs.size) {  //index가 음악 개수를 넘어가지 않도록
            Toast.makeText(this,"다음 음악이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direction

        sbProgress.interrupt()      //재생중인 노래 스레드 종료
        startThread()               //새로운 노래 재생(다음 노래)

        mediaPlayer?.release()      // 미디어플레이어가 가지고 있던 리소스를 해방
        mediaPlayer = null          //미디어 플레이어가 가지고 있던 리소스 해제

        setPlayer(songs[nowPos])    //데이터 랜더링
    }
}