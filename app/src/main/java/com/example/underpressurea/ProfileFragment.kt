package com.example.underpressurea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.underpressurea.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val userid = FirebaseAuth.getInstance().currentUser!!.uid

        val v:View= inflater.inflate(R.layout.fragment_profile, container, false)

        val binding: FragmentProfileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )
        val name = FirebaseAuth.getInstance().currentUser!!.displayName
        val email = FirebaseAuth.getInstance().currentUser!!.email

        //val name = v.findViewById(R.id.name) as TextView

        //name.setText("aaaaaaa")

        binding.name.text = name
        binding.email.text =email

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val name = view.findViewById(R.id.name) as TextView
        //name.setText("aaaaaaa")
        //Log.d("PROFILE_FRAGMENT","onViewCreated")

    }

}
