package com.example.underpressurea

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.underpressurea.databinding.FragmentTutorialBinding


class TutorialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v:View= inflater.inflate(R.layout.fragment_tutorial, container, false)

        val binding: FragmentTutorialBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tutorial, container, false
        )

        binding.btnStartTutorial.setOnClickListener {
            val intent = Intent(activity, MyCustomAppIntro::class.java)
            startActivity(intent)
        }
        return binding.root
        // Inflate the layout for this fragment
        /**
        val intent = Intent(activity, MyCustomAppIntro::class.java)
        startActivity(intent)
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
        */
    }
}