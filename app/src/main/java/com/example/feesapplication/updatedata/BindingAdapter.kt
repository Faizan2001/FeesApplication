package com.example.feesapplication.updatedata

import android.os.Build
import android.view.View
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.feesapplication.R
import com.example.feesapplication.data.FeeStatus
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.fragments.DashboardFragmentDirections
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
                val action =
                    DashboardFragmentDirections.actionDashboardFragmentToStudentListFragment(
                        currentBatch
                    )
                view.findNavController().navigate(action)
            }
        }

        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }


        @BindingAdapter("android:feesStatusColor")
        @JvmStatic
        fun feesStatusColor(cardView: CardView, feeStatus: FeeStatus) {
            when (feeStatus) {
                //  FeeStatus.NONE -> {cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.greyColor))}
                //FeeStatus.PAID ->  {cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.green))}
                //  FeeStatus.UNPAID ->  {cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.red))}

                FeeStatus.NONE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                      cardView.setCardBackgroundColor(cardView.context.getColor(R.color.greyColor))

                    } else {
                        cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.greyColor))
                    }
                }
                FeeStatus.PAID -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))

                    } else {
                        cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.green))
                    }
                }
                FeeStatus.UNPAID -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))

                    } else {
                         cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.red))
                    }
                }


            }
        }

    }


}

//  FeeStatus.PAID ->  {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))}
//                FeeStatus.UNPAID ->  {cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))}