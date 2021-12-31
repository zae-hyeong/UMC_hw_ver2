package com.example.FLO.Data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


//제목, 가수, 사진,재생시간,현재 재생시간, isplaying(재생 되고 있는지)
@Entity(tableName = "SongTable")
data class Song(
        var title : String = "",            //노래 제목
        var singer : String = "",           //노래 가수
        var albumImg : Int ?= null,              //노래 이미지
        var musicRes : String = "",          //음악파일 이름

        var curTime : Int =0,               //노래 진행 시간
        var playTime : Int = 0,             //노래 길이(총 재생시간)

        var isPlaying : Boolean = false,     //노래 진행여부
        var isLike: Boolean = false,        //like 여부

        var albumIdx: Int = 0 // 이 song이 어떤 앨범에 담겨 있는지 가리키는 변수 (foreign key 역할)
){
        @PrimaryKey(autoGenerate = true) var id: Int = 0
}
