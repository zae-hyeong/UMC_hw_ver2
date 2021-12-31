package com.example.FLO.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.FLO.Data.Album
import com.example.FLO.databinding.ItemHomeMusiclistAlbumBinding

class RVAlbumListAdapter(private val albumList : ArrayList<Album>) : RecyclerView.Adapter<RVAlbumListAdapter.ViewHolder>() {


    /*---------- 인터페이스 ----------*/


    //어댑터 외부에서 작업을 해주기 위한 인터페이스 정의
    interface MyItemClickListener{
        fun onItemClick(album : Album) //실행해줄 함수(데이터 랜더링을 위해 앨범 정보를 받아왔음!)
    }

    /*---------- 필드 ----------*/

    //리스너 객체를 저장할 변수 + 리스너 객체를 외부에서 받아오는 함수
    private lateinit var myItemClickListener : MyItemClickListener


    /*---------- 내부 클래스 ----------*/


    //내부 클래스로 뷰 홀더 만들어주기 -> 뷰 홀더 : 아이템 뷰를 잡아주기 위한 것
    //인자 값 : 리사이클러 뷰에 들어갈 아이템들
    inner class ViewHolder(val binding : ItemHomeMusiclistAlbumBinding) : RecyclerView.ViewHolder(binding.root){
        //인자로 들어온 아이템을 뷰 홀더에게 알려줌
        fun bind(album : Album) {
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlbumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }


    /*---------- 오버라이딩 함수 ----------*/


    // 뷰홀더를 생성할때 호출되는 함수 -> 아이템 뷰 객체를 만들어서 뷰 홀더에 넣어줌
    // 한번 만들고 나머지는 삭제하지 않고 재사용 함(몇번 많이 호출되지 않음)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        //뷰 홀더에서 바인딩을 받고 있기 때문에 inflate해서 인자를 전달해줘야 함!
        val binding : ItemHomeMusiclistAlbumBinding
            = ItemHomeMusiclistAlbumBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    //뷰 홀더에 데이터를 바인딩 해주는 함수(데이터가 로드 될때마다 호출됨(엄청 많이 호출))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        // holder.itemView는 아이템 전체를 나타냄. 근데 이런 작업을 어댑터 안에서만 해주면 너무 불편함!
        // 외부에서 bind 작업을 할 수는 없을까? -> 인터페이스로 정의
        holder.itemView.setOnClickListener{
            myItemClickListener.onItemClick(albumList[position])
        }
    }

    //리사이클 뷰에 사용하는 아이템 개수 리턴 -> 리사이클러 뷰의 마지막을 알 수 있음!
    override fun getItemCount(): Int = albumList.size


    /*---------- 추가 함수 ----------*/


    fun setMyItemClickListener(itemClickListener : MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

}