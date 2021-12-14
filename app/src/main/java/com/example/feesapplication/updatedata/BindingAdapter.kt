package com.example.feesapplication.updatedata


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.feesapplication.R
import com.example.feesapplication.data.database.FeeStatus
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.fragments.DashboardFragmentDirections
import com.example.feesapplication.fragments.StudentListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

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

        @BindingAdapter("android:notEmptyDatabase")
        @JvmStatic
        fun notEmptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.INVISIBLE
                false -> view.visibility = View.VISIBLE
            }
        }


        @BindingAdapter("android:feesStatusColor")
        @JvmStatic
        fun feesStatusColor(cardView: CardView, feeStatus: FeeStatus) {
            when (feeStatus) {
                //  FeeStatus.NONE -> {cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.greyColor))}
                //FeeStatus.PAID ->  {cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.green))}
                //  FeeStatus.UNPAID ->  {cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.context, R.color.red))}

                FeeStatus.None -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.grayColor))

                    } else {
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                cardView.context,
                                R.color.grayColor
                            )
                        )
                    }
                }
                FeeStatus.Paid -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))

                    } else {
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                cardView.context,
                                R.color.green
                            )
                        )
                    }
                }
                FeeStatus.Unpaid -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))

                    } else {
                        cardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                cardView.context,
                                R.color.red
                            )
                        )
                    }
                }


            }
        }


        @BindingAdapter("android:onOptionsMenu")
        @JvmStatic
        fun onOptionsCardView(view: View, currentStudent: Student) {


            view.setOnClickListener {
                val popUp = PopupMenu(view.context, it.findViewById(R.id.fees_status))
                popUp.menuInflater.inflate(R.menu.student_options_menu, popUp.menu)
                popUp.setOnMenuItemClickListener { menuItem: MenuItem ->

                    when (menuItem.itemId) {
                        R.id.update_student -> {
                            val action =
                                StudentListFragmentDirections.actionStudentListFragmentToUpdateFragment(
                                    currentStudent
                                )
                            view.findNavController().navigate(action)
                        }

                        R.id.call_student -> {
                            val callIntent =
                                Intent(
                                    Intent.ACTION_DIAL,
                                    Uri.parse("tel:${currentStudent.studentNumber}")
                                )

                            view.context.startActivity(callIntent)

                        }
                        R.id.SMS -> {

                            val numberAction =
                                StudentListFragmentDirections.actionStudentListFragmentToSMS(
                                    currentStudent,
                                    null
                                )
                            view.findNavController().navigate(numberAction)

                        }
                        R.id.mail_Student -> {

                            val emailAction =
                                StudentListFragmentDirections.actionStudentListFragmentToEmail(
                                    currentStudent,
                                    null
                                )
                            view.findNavController().navigate(emailAction)

                        }

                    }


                    return@setOnMenuItemClickListener true
                }
                try {
                    val fieldPopUp = PopupMenu::class.java.getDeclaredField("mPopUp")
                    fieldPopUp.isAccessible = true
                    val mPopUp = fieldPopUp.get(popUp)
                    mPopUp.javaClass
                        .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(mPopUp, true)
                } catch (e: Exception) {
                    Log.d("Main", "Did not work")
                } finally {
                    popUp.show()
                }

            }


        }


    }


}
