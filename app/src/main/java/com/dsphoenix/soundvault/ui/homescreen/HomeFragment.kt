package com.dsphoenix.soundvault.ui.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.HomeScreenFragmentLayoutBinding

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelFactory = HomeViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)

        val binding = DataBindingUtil.inflate<HomeScreenFragmentLayoutBinding>(
            layoutInflater,
            R.layout.home_screen_fragment_layout,
            container,
            false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    companion object {
        const val navigationTag = "HomeFragment"
    }
}