package com.tutorials.hngx2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tutorials.hngx2.databinding.FragmentEditResumeBinding
import com.tutorials.hngx2.databinding.FragmentResumeBinding

class EditResume : Fragment() {
    private lateinit var binding: FragmentEditResumeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditResumeBinding.inflate(inflater, container, false)
        return binding.root
    }
}