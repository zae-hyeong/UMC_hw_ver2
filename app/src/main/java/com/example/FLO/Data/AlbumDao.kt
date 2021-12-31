package com.example.FLO.Data

import androidx.room.*
import com.example.FLO.Data.Album


@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable") // 테이블의 모든 값을 가져와라
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    //앨범에 좋아요를 눌렀으니깐
    @Insert
    fun likeAlbum(like : Like)

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun islikeAlbum(userId : Int, albumId : Int) : Int? //좋아요를 눌렀을때 DB에 앨범 아이디 추가

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikeAlbum(userId : Int, albumId : Int) //좋아요를 취소했을때 DB에서 앨범 아이디 제거

    //userId 값의 유저를 가져와서, albumId와 id가 같은것으로 왼쪽(LikeTable) 기준 JOIN
    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId =:userId")
    fun getLikedAlbums(userId: Int) : List<Album> //유저가 좋아요를 표시한 모든 앨범 리스트를 가져옴
}