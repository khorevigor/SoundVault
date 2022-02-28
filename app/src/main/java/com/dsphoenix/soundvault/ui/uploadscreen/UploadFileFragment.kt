package com.dsphoenix.soundvault.ui.uploadscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.UploadScreenFragmentBinding
import com.dsphoenix.soundvault.ui.uploadscreen.forms.DistributionPlanFragment
import com.dsphoenix.soundvault.ui.uploadscreen.forms.PickFileFragment
import com.dsphoenix.soundvault.ui.uploadscreen.forms.PickGenresFragment
import com.dsphoenix.soundvault.ui.uploadscreen.forms.PickImageCoverFragment
import com.dsphoenix.soundvault.utils.navigation.NavigationController
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UploadFileFragment: ViewBindingFragment<UploadScreenFragmentBinding>(UploadScreenFragmentBinding::inflate) {
    private val viewModel: UploadFileViewModel by activityViewModels()

    @Inject
    lateinit var navigationController: NavigationController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigationController.initialize(childFragmentManager, R.id.fragment_container_view)
        setupView()
    }

    private fun setupView() {
        showPickFileFragment()
        binding.bubblePicker.setOnActiveChangeListener { pos ->
            when (pos) {
                0 -> {
                    showPickFileFragment()
                    binding.tvTest.text = "Position 1"
                    true
                }
                1 -> {
                    showPickImageFragment()
                    binding.tvTest.text = "Position 2"
                    true
                }
                2 -> {
                    showPickGenresFragment()
                    binding.tvTest.text = "Position 3"
                    true
                }
                3 -> {
                    showDistributionPlanFragment()
                    binding.tvTest.text = "Position 4"
                    true
                }
                else -> false
            }
        }
    }

    private fun showPickFileFragment() {
        navigationController.replaceFragment(PickFileFragment(), addToBackStack = false)
    }

    private fun showPickImageFragment() {
        navigationController.replaceFragment(PickImageCoverFragment(), addToBackStack = false)
    }

    private fun showPickGenresFragment() {
        navigationController.replaceFragment(PickGenresFragment(), addToBackStack = false)
    }

    private fun showDistributionPlanFragment() {
        navigationController.replaceFragment(DistributionPlanFragment(), addToBackStack = false)
    }

    private fun onUploadButtonClick() {
        viewModel.uploadTrack()
    }
}