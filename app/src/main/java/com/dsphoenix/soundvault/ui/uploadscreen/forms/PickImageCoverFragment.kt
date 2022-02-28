package com.dsphoenix.soundvault.ui.uploadscreen.forms

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.databinding.PickImageCoverFragmentBinding
import com.dsphoenix.soundvault.ui.uploadscreen.UploadFileViewModel
import com.dsphoenix.soundvault.utils.TAG
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment

class PickImageCoverFragment :
    ViewBindingFragment<PickImageCoverFragmentBinding>(PickImageCoverFragmentBinding::inflate) {
    private val fileMimeType = arrayOf("image/png", "image/jpeg")

    private val viewModel: UploadFileViewModel by activityViewModels()

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                Log.d(TAG, "image picked $uri")
                onImagePicked()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.btnPickFile.setOnClickListener { onPickImageClick() }
    }

    private fun onPickImageClick() {
        activityLauncher.launch(fileMimeType)
    }

    private fun onImagePicked() {
        binding.ivDoneIcon.visibility = View.VISIBLE
    }
}