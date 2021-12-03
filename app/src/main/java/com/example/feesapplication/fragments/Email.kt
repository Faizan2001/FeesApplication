package com.example.feesapplication.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.FragmentEmailBinding
import com.example.feesapplication.list.utils.observeOnce

class Email : Fragment() {

    private val args by navArgs<EmailArgs>()
    private val studentViewModel: StudentViewModel by viewModels()

    private var _binding: FragmentEmailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentEmailBinding.inflate(layoutInflater, container, false)

        val batchNameReceived = args.batchNameSent

        val currentStudent = args.emailStudent


        if (args.emailStudent != null) {

            binding.mailField.editText?.setText(currentStudent?.studentEmail)

        } else if (args.emailStudent == null) {


            if (batchNameReceived != null) {
                studentViewModel.studentsInBatch(batchNameReceived)
                    .observeOnce(viewLifecycleOwner, { list ->

                        val allEmails = StringBuilder()

                        for (i in list.indices) {

                            allEmails.append("\n")
                            allEmails.append(list[i].studentEmail)


                        }

                        binding.mailField.editText?.setText(allEmails.toString())


                    })
            }


        }

        binding.sendButton.setOnClickListener {


            val email = binding.mailField.editText?.text.toString()
            val subject = binding.subjectField.editText?.text.toString()
            val message = binding.messageField.editText?.text.toString()

            val emailAddresses = email.split("\n".toRegex()).toTypedArray()

            val intent = Intent(Intent.ACTION_SENDTO).apply {

                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, emailAddresses)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)


            }

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireActivity(), "No Email Applications Found", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}