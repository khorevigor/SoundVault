package com.dsphoenix.soundvault.ui.uploadscreen.forms

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.PickFileFragmentBinding
import com.dsphoenix.soundvault.ui.uploadscreen.UploadFileViewModel
import com.dsphoenix.soundvault.utils.TAG
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickFileFragment :
    ViewBindingFragment<PickFileFragmentBinding>(PickFileFragmentBinding::inflate) {
    private val fileMimeType = "audio/mpeg"

    private val viewModel: UploadFileViewModel by activityViewModels()

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                onFilePicked(uri)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.btnPickFile.setOnClickListener { onPickFileClick() }
        if (!viewModel.filename.value.isNullOrEmpty()) {
            showFilePickedIcon()
        }
        viewModel.filename.observe(viewLifecycleOwner) { text ->
            val editText = binding.etFileName
            if (text != editText.text.toString())
                editText.setText(text)
        }
    }

    private fun onPickFileClick() {
        activityLauncher.launch(fileMimeType)
    }

    private fun onFilePicked(uri: Uri) {
        Log.d(TAG, "onFilePicked called with $uri")
        viewModel.uri.value = uri

        showFilePickedIcon()
    }

    private fun showFilePickedIcon() {
        binding.btnPickFile.background = getDrawable(resources, R.drawable.ic_folder, null)
        binding.ivDoneIcon.visibility = View.VISIBLE
    }
}