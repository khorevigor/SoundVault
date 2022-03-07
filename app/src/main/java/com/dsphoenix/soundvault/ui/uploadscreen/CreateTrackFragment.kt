package com.dsphoenix.soundvault.ui.uploadscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.CreateTrackFragmentBinding
import com.dsphoenix.soundvault.ui.uploadscreen.forms.distributionplan.DistributionPlanFragment
import com.dsphoenix.soundvault.ui.uploadscreen.forms.pickfile.PickFileFragment
import com.dsphoenix.soundvault.ui.uploadscreen.forms.pickgenres.PickGenresFragment
import com.dsphoenix.soundvault.ui.uploadscreen.forms.pickimage.PickImageCoverFragment
import com.dsphoenix.soundvault.utils.navigation.NavigationController
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateTrackFragment: ViewBindingFragment<CreateTrackFragmentBinding>(CreateTrackFragmentBinding::inflate) {
    private val viewModel: CreateTrackViewModel by activityViewModels()

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
                    true
                }
                1 -> {
                    showPickImageFragment()
                    true
                }
                2 -> {
                    showPickGenresFragment()
                    true
                }
                3 -> {
                    showDistributionPlanFragment()
                    true
                }
                else -> false
            }
        }
        binding.btnUpload.setOnClickListener { onUploadButtonClick() }
        viewModel.isValid.observe(viewLifecycleOwner) { valid ->
            binding.btnUpload.visibility = if (valid) View.VISIBLE else View.GONE
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