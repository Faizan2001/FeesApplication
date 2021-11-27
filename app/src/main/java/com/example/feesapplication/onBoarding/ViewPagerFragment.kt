package com.example.feesapplication.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.feesapplication.R
import com.example.feesapplication.databinding.AddBatchFragmentBinding
import com.example.feesapplication.databinding.FragmentViewPagerBinding
import com.example.feesapplication.onBoarding.onboardingScreens.First
import com.example.feesapplication.onBoarding.onboardingScreens.Second
import com.example.feesapplication.onBoarding.onboardingScreens.Third


class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerBinding.inflate(layoutInflater, container, false)


        val fragmentList = arrayListOf<Fragment>(
            First(),
            Second(),
            Third()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}