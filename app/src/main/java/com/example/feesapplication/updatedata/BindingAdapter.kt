package com.example.feesapplication.updatedata

import android.renderscript.RenderScript
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.feesapplication.R
import com.example.feesapplication.data.FeeStatus
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.fragments.DashboardFragmentDirections
import com.example.feesapplication.fragments.StudentListFragment
import com.example.feesapplication.fragments.StudentListFragmentDirections
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

        @BindingAdapter("android:sendBatchToStudentListFragment")
        @JvmStatic
        fun sendBatchToStudentListFragment(view: ConstraintLayout, currentBatch: Batch) {

            view.setOnClickListener {
                val action = DashboardFragmentDirections.actionDashboardFragmentToStudentListFragment(currentBatch)
                view.findNavController().navigate(action)
            }
        }






      /*  @BindingAdapter("android:feesStatusColor")
        @JvmStatic
        fun feesStatusColor(cardView: CardView, feeStatus: FeeStatus) {
            when(feeStatus){
                FeeStatus.NONE -> {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.greyColor))}
                FeeStatus.PAID ->  {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))}
                FeeStatus.UNPAID ->  {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))}
            }
        } */
    }


}