package com.example.underpressurea.pressuretracker

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.underpressurea.R
import com.example.underpressurea.convertTimeToFormatted
import com.example.underpressurea.database.PressureValue
import com.example.underpressurea.databinding.PressureTrackerFragmentBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class PressureTrackerFragment : Fragment() {

    /**companion object {
    fun newInstance() = PressureTrackerFragment()
    }*/

    //private lateinit var viewModel: PressureTrackerViewModel

    private lateinit var dbReference: DatabaseReference
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    private var num = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PressureTrackerFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.pressure_tracker_fragment, container, false
        )

        binding.pressureList.layoutManager = LinearLayoutManager(this@PressureTrackerFragment.context)

        val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                binding.progressBar2.visibility = View.VISIBLE
            }

        var reference :DatabaseReference =  FirebaseDatabase.getInstance().getReference("data")

        if (user != null) {
            val userid = FirebaseAuth.getInstance().currentUser!!.uid

            //dbReference = FirebaseDatabase.getInstance().getReference("data")
            //dbReference = FirebaseDatabase.getInstance().reference.child("data").child(userid)
            reference =
                FirebaseDatabase.getInstance().getReference("users").child(userid)
        }

        //val uidRef = dbReference.child("data").child(userid)

        //dbReference.child(userid.toString()).setValue(user)

        //val pressures = dbReference.child("data")


        //return inflater.inflate(R.layout.pressure_tracker_fragment, container, false)




        binding.newBpButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_pressureTrackerFragment_to_insertPressureFragment)
        }


        /**binding.clearButton.setOnClickListener {
            deleteAll(reference)
        }*/

        val builder = AlertDialog.Builder(this@PressureTrackerFragment.context)
        builder.setTitle("Erase pressure history?")

        // Display a message on alert dialog
        builder.setMessage("You'll lose all pressure values saved until now!")

        // Display positive and negative button on alert dialog
        builder.setNegativeButton("Cancel", null)
        builder.setPositiveButton("Confirm"){
            dialog, which -> deleteAll(reference)
        }

        val dialog: AlertDialog = builder.create()

        binding.clearButton.setOnClickListener {
            dialog.show()
        }


        fun displayPost() {
            val options = FirebaseRecyclerOptions.Builder<PressureValue>()
                .setQuery(reference, PressureValue::class.java).build()
            val adapter =
                object : FirebaseRecyclerAdapter<PressureValue, PressureAdapter>(options) {
                    override fun onBindViewHolder(
                        @NonNull holder: PressureAdapter, position: Int, @NonNull model: PressureValue
                    ) {
                        //dbReference =

                        holder.min_pressure.setText("Min: "+model.min.toString())
                        holder.max_pressure.setText("Max: "+model.max.toString())
                        holder.date.setText(convertTimeToFormatted(model.startTimeMilli))
                        holder.qualityImage.setImageResource(
                            when (model.level) {
                                0 -> R.drawable.ic_pressure_bad
                                //0 -> R.drawable.ic_pressure_bad_black
                                else -> R.drawable.ic_pressure_ok
                               //else -> R.drawable.ic_pressure_ok_black
                            }
                        )

                        holder.btn_delete_item.setOnClickListener(object: View.OnClickListener {
                            override fun onClick(view:View) {
                                getRef(position).getKey()?.let {
                                    reference
                                        .child(it)
                                        .setValue(null)
                                        .addOnCompleteListener(object: OnCompleteListener<Void> {
                                            override fun onComplete(@NonNull task: Task<Void>) {
                                            }
                                        })
                                }
                            }
                        })

                        /**
                        holder.btn_delete_item.setOnClickListener(
                            holder.getRef(position).database
                        }*/





                    }

                    @NonNull
                    override fun onCreateViewHolder(@NonNull viewgroup: ViewGroup, i: Int): PressureAdapter {
                        val itemView = LayoutInflater.from(this@PressureTrackerFragment.context)
                            .inflate(R.layout.list_item_pressure, viewgroup, false)


                        return PressureAdapter(itemView)
                    }
                }


            adapter.startListening()
            binding.pressureList.adapter = adapter
            val saveListener = object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    num = dataSnapshot.getChildrenCount()
                    Log.d("onDataChange", "num: "+num)
                    binding.pressureList.smoothScrollToPosition(num.toInt())
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }
            }
            Log.d("num_counter", "num: "+num)
            reference.addValueEventListener(saveListener)





        }


        displayPost()

        binding.setLifecycleOwner(this)

        return binding.root
}

    private fun deleteAll(reference: DatabaseReference) {
        reference.removeValue()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //val viewModel = ViewModelProviders.of(this).get(PressureTrackerViewModel::class.java)
    }

}
