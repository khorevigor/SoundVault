package com.dsphoenix.soundvault.ui.uploadscreen.forms.pickimage

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.databinding.PickImageCoverFragmentBinding
import com.dsphoenix.soundvault.ui.uploadscreen.CreateTrackViewModel
import com.dsphoenix.soundvault.utils.TAG
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment

class PickImageCoverFragment :
    ViewBindingFragment<PickImageCoverFragmentBinding>(PickImageCoverFragmentBinding::inflate) {
    private val fileMimeType = arrayOf("image/png", "image/jpeg")

    private val viewModel: CreateTrackViewModel by activityViewModels()

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                onImagePicked(uri)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.btnPickFile.setOnClickListener { onPickImageClick() }
        viewModel.imageForm.uri.observe(viewLifecycleOwner) {
            binding.btnPickFile.setImageURI(it)
            showImagePickedIcon()
        }
    }

    private fun onPickImageClick() {
        activityLauncher.launch(fileMimeType)
    }

    private fun onImagePicked(uri: Uri) {
        viewModel.imageForm.setUri(uri)
        showImagePickedIcon()
    }

    private fun showImagePickedIcon() {
        binding.ivDoneIcon.visibility = View.VISIBLE
    }
}