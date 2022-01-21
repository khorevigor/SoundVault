package com.dsphoenix.soundvault.ui.uploadscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dsphoenix.soundvault.databinding.UploadScreenFragmentLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "UploadFileFragment"

@AndroidEntryPoint
class UploadFileFragment : Fragment() {
    private var _binding: UploadScreenFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UploadFileViewModel by viewModels()

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {uri ->
        viewModel.uri.value = uri
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UploadScreenFragmentLayoutBinding.inflate(inflater, container, false)

        setupView()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
    }

    private fun onPickFileClick() {
        activityLauncher.launch("audio/mpeg")
    }

    private fun onUploadButtonClick() {
        viewModel.uploadTrack()
    }
}