package com.example.FLO.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.FLO.databinding.FragmentMainLookBinding


class LookFragment : Fragment() {

    lateinit var binding: FragmentMainLookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainLookBinding.inflate(inflater, container, false)
        return binding.root
    }

}