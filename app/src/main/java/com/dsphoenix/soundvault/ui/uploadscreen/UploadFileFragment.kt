package com.dsphoenix.soundvault.ui.uploadscreen

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.UploadScreenFragmentBinding
import com.dsphoenix.soundvault.utils.constants.DistributionPlan
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadFileFragment: ViewBindingFragment<UploadScreenFragmentBinding>(UploadScreenFragmentBinding::inflate) {
    private val viewModel: UploadFileViewModel by viewModels()

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {uri ->
        viewModel.uri.value = uri
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.btnPickFile.setOnClickListener { onPickFileClick() }
        binding.btnUpload.setOnClickListener { onUploadButtonClick() }
        binding.btnUpload.isEnabled = false
        viewModel.filename.observe(viewLifecycleOwner) { text ->
            val editText = binding.etFileName
            if (text != editText.text.toString())
                editText.setText(text)
        }
        viewModel.isValid.observe(viewLifecycleOwner) {
            binding.btnUpload.isEnabled = it
        }
        binding.etFileName.doAfterTextChanged {
            viewModel.filename.value = it.toString()
        }
        binding.etFileDescription.doAfterTextChanged {
            viewModel.description.value = it.toString()
        }

        binding.rgDistributionPlan.setOnCheckedChangeListener { _, optionId ->
            when (optionId) {
                R.id.rb_free_for_all -> viewModel.distributionPlan.value = DistributionPlan.FREE_FOR_ALL
                R.id.rb_subscription -> viewModel.distributionPlan.value = DistributionPlan.SUBSCRIPTION
            }
        }
    }

    private fun onPickFileClick() {
        activityLauncher.launch("audio/mpeg")
    }

    private fun onUploadButtonClick() {
        viewModel.uploadTrack()
    }
}