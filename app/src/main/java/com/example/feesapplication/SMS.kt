package com.example.feesapplication

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.FragmentSMSBinding
import com.example.feesapplication.list.utils.observeOnce


class SMS : Fragment() {

    private val args by navArgs<SMSArgs>()
    private val studentViewModel: StudentViewModel by viewModels()


    private var _binding: FragmentSMSBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSMSBinding.inflate(layoutInflater, container, false)

        val batchName = args.smsBatchName



        if (args.currentSMSStudent != null) {
            val studentName = args.currentSMSStudent!!.studentName
            val studentNumber = args.currentSMSStudent!!.studentNumber

            binding.nameField.editText?.setText(studentName)
            binding.numbersField.editText?.setText(studentNumber)



            binding.sendButton.setOnClickListener {

                val hasPermission = ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED

                if (!hasPermission) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            Manifest.permission.SEND_SMS,
                        ), PackageManager.PERMISSION_GRANTED
                    )
                } else {

                    if (binding.messageField.editText?.text.isNullOrEmpty() || binding.numbersField.editText?.text.isNullOrEmpty()) {

                        Toast.makeText(
                            requireActivity(),
                            "Please fill out all fields",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        val typedMessage = binding.messageField.editText?.text
                        val numberSelected = binding.numbersField.editText?.text
                        try {
                            sendSMS(numberSelected.toString(), typedMessage.toString())

                            Toast.makeText(requireActivity(), "Sent!", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()
                        } catch (e: Exception) {
                            Toast.makeText(requireActivity(), "Error!", Toast.LENGTH_SHORT).show()
                        }


                    }
                }
            }

        } else if (args.currentSMSStudent == null) {


            if (batchName != null) {

                val studentNames = StringBuilder()
                val studentNumbers = StringBuilder()

                studentViewModel.studentsInBatch(batchName)
                    .observeOnce(viewLifecycleOwner, { list ->

                        for (i in list.indices) {

                            studentNames.append(list[i].studentName)
                            studentNames.append("\n")
                            studentNumbers.append(list[i].studentNumber)
                            studentNumbers.append("\n")

                        }

                        binding.nameField.editText?.setText(studentNames.toString())
                        binding.numbersField.editText?.setText(studentNumbers.toString())

                    })


                binding.sendButton.setOnClickListener {

                    val hasPermission = ContextCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED

                    if (!hasPermission) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(
                                Manifest.permission.SEND_SMS,
                            ), PackageManager.PERMISSION_GRANTED
                        )
                    } else {

                        if (binding.messageField.editText?.text.isNullOrEmpty() || binding.numbersField.editText?.text.isNullOrEmpty()) {

                            Toast.makeText(
                                requireActivity(),
                                "Please fill out all fields",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            val typedMessage = binding.messageField.editText?.text
                            val numbersSelected = binding.numbersField.editText?.text


                            val selectedNumbers =
                                numbersSelected?.split("\n".toRegex())?.toTypedArray()


                            try {
                                if (selectedNumbers != null) {
                                    for (i in selectedNumbers) {
                                        sendSMS(i, typedMessage.toString())
                                    }
                                }
                                Toast.makeText(requireActivity(), "Sent!", Toast.LENGTH_SHORT)
                                    .show()
                                findNavController().popBackStack()
                            } catch (e: Exception) {
                                Toast.makeText(requireActivity(), "Sent!", Toast.LENGTH_SHORT)
                                    .show()
                                findNavController().popBackStack()
                            }


                        }
                    }

                }

            }

        }



        return binding.root
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        val sentPI: PendingIntent =
            PendingIntent.getBroadcast(requireActivity(), 0, Intent("SMS_SENT"), 0)
        android.telephony.SmsManager.getDefault()
            .sendTextMessage(phoneNumber, null, message, sentPI, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}