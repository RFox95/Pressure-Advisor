package com.example.underpressurea.pressureok

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.underpressurea.R

class PressureOkFragment : Fragment() {

    companion object {
        fun newInstance() = PressureOkFragment()
    }

    private lateinit var viewModel: PressureOkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pressure_ok_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PressureOkViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
