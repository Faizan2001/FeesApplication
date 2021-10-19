package com.example.feesapplication.updatedata

import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.feesapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapter {

    companion object {

        @BindingAdapter("android:navigateToAddBatchFragment")
        @JvmStatic
        fun navigateToAddBatchFragment(view: FloatingActionButton, navigate: Boolean) {
            view.setOnClickListener {
                if (navigate) {
                    view.findNavController()
                        .navigate(R.id.action_dashboardFragment_to_addBatchFragment)
                }

            }

        }

    }

}