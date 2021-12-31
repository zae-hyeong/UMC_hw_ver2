package com.example.FLO.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.FLO.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //handler 사용
        //역할 : 워커 스레드에서는 뷰 렌더링이 불가능하기 때문에 핸들러를 사용해서 메인 쓰레드에 메시지 전달
        //인자 : Looper. 자동으로 메시지를 꺼내서 처리/전달해주는 역할(Looper.getMainLooper : 메인 쓰레드에 루퍼 전달)
        Handler(Looper.getMainLooper()).postDelayed({
            //메인 액티비티로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 1500) //추가 함수 : 딜레이 주기(2000ms) -> 지금은 그냥 2초동안 기다리지만, 네트워크나 데이터 처리등을 할 수 있음
    }
}