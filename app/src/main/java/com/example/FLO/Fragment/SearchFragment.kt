package com.example.FLO.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.databinding.FragmentMainSearchBinding


class SearchFragment : Fragment() {
    lateinit var binding: FragmentMainSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
}