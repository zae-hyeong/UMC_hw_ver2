package com.example.FLO.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

//프레그먼트(배너)를 스크롤할 수 있는 어댑터기 때문에 인자로 프레그먼트를 받음
//그리고 이 프레그먼트를 관리하는 FragmentStateAdapter 상속(프레그먼트를 인자로 받음)
class BannerViewpagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {

    //뷰페이저에 출력될 프레그먼트들을 리스트로 만들어서 관리/출력함
    //프레그먼트 리스트를 관리하는 ArrayList 상속, elements는 Fragment, ArrayList는 초기화 필수
    //보안성/관리 등의 문제로 이 페이지에서만 사용 -> private 로 선언
    private val fragmentList : ArrayList<Fragment> = ArrayList()

    //FragmentStateAdapter의 추상 메소드 1 -> 필수적으로 오버라이드해서 구현
    //뷰페이저의 아이템 개수를 알려주는 함수, 리턴값 : Int -> 'return fragmentList.size'라 써도 됨
    override fun getItemCount(): Int = fragmentList.size

    //FragmentStateAdapter의 추상 메소드 2 -> 필수적으로 오버라이드해서 구현
    //프레그먼트 리스트 안에 들어갈 elements(item)을 생성하는 함수, 리턴값 : 출력할 프레그먼트 리스트의 프레그먼트(fragmentList[position])
    override fun createFragment(position: Int): Fragment = fragmentList[position]

    //프레그먼트 추가(배너 추가)가 동적으로 이뤄짐(배너를 사용하는 곳에서 추가함)
    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)

        notifyItemInserted(fragmentList.size -1) //뷰페이저에 아이템을 들어왔음을 알려주는 코드
        // 인자로 마지막 리스트 인덱스를 넣음 : index 0부터 시작 -> 마지막 item은 전체 사이즈에서 1 빼야함
    }
}