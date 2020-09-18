package com.example.underpressurea.pressureinsert

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.underpressurea.R
import com.example.underpressurea.common.Common
import com.example.underpressurea.database.PressureValue
import com.example.underpressurea.databinding.InsertPressureFragmentBinding
import com.example.underpressurea.remote.FirebaseInterface
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InsertPressureFragment : Fragment() {

    private lateinit var dbReference: DatabaseReference
    //private lateinit var adapter: FirebaseRecyclerAdapter<PressureValue, PressureAdapter>
    lateinit var myFirebaseService: FirebaseInterface


    companion object {
        fun newInstance() = InsertPressureFragment()
    }

    private lateinit var viewModel: InsertPressureViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: InsertPressureFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.insert_pressure_fragment, container, false
        )

        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        dbReference = FirebaseDatabase.getInstance().getReference("users").child(userid)

        myFirebaseService = Common.firebaseInterfaceService

        fun postPressure() {

            binding.btnPost.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE


            val min = binding.minPressure.text.toString().toInt()
            val max = binding.maxPressure.text.toString().toInt()


            if (min > 90 || max > 140) {
                val item = PressureValue(min, max, 0)
                //dbReference.push().setValue(item)       //Use this method to create unique id of item
                myFirebaseService.addPressure(
                    "{\n\t\"id\":\"$userid\",\n\t\"level\":${item.level},\n\t" +
                            "\"min\": ${item.min},\n\t\"max\":${item.max},\n\t\"startTimeMilli\":${item.startTimeMilli}\n}"
                ).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {

                        if (response.isSuccessful) {
                            view?.findNavController()
                                ?.navigate(R.id.action_insertPressureFragment_to_mapsFragment)
                            Log.d("POST_REQUEST_DEBUG", "success ")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            this@InsertPressureFragment.context,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("POST_REQUEST_DEBUG", "failure" + t.message)
                    }
                })

            } else {
                val item = PressureValue(min, max, 1)
                //dbReference.push().setValue(item)       //Use this method to create unique id of item

                myFirebaseService.addPressure(
                    "{\n\t\"id\":\"$userid\",\n\t\"level\":${item.level},\n\t" +
                            "\"min\": ${item.min},\n\t\"max\":${item.max},\n\t\"startTimeMilli\":${item.startTimeMilli}\n}"
                ).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {

                        if (response.isSuccessful) {
                            view?.findNavController()
                                ?.navigate(R.id.action_insertPressureFragment_to_pressureOkFragment)
                            Log.d("POST_REQUEST_DEBUG", "success ")
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            this@InsertPressureFragment.context,
                            "" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("POST_REQUEST_DEBUG", "failure" + t.message)
                    }
                })

            }

            //adapter.notifyDataSetChanged()
        }

        binding.btnPost.setOnClickListener {
            if (binding.minPressure.text.isEmpty() || binding.maxPressure.text.isEmpty()) {
                val text = "Min and max must not be empty"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(this@InsertPressureFragment.context, text, duration)
                toast.show()
            } else if (binding.minPressure.text.toString().toInt() >= binding.maxPressure.text.toString().toInt()) {
                val text = "Max value must be greater than min value"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(this@InsertPressureFragment.context, text, duration)
                toast.show()
            } else if ((binding.minPressure.text.toString().toInt() >= 250 ||
                binding.maxPressure.text.toString().toInt() >= 300) || (binding.minPressure.text.toString().toInt() <= 45 ||
                binding.maxPressure.text.toString().toInt() <= 65)
            ) {
                val text = "Please, insert realistic values"
                val duration = Toast.LENGTH_SHORT

                val toast = Toast.makeText(this@InsertPressureFragment.context, text, duration)
                toast.show()
            } else {
                // Hide the keyboard.
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                postPressure()
            }
        }


        //return inflater.inflate(R.layout.insert_pressure_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(InsertPressureViewModel::class.java)
        // TODO: Use the ViewModel
    }


}
