package com.example.feesapplication.onBoarding.onboardingScreens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.feesapplication.R
import com.example.feesapplication.databinding.FragmentThirdBinding


// TODO: Rename parameter arguments, choose names that match

class Third : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentThirdBinding.inflate(layoutInflater, container, false)



        binding.nextButtonThird.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_dashboardFragment)
            finishedOnBoarding()
        }


        return binding.root
    }

    private fun finishedOnBoarding() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}