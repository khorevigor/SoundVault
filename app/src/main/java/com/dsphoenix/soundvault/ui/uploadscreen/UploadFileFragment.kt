package com.dsphoenix.soundvault.ui.uploadscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.UploadScreanFragmentLayoutBinding

private const val TAG = "UploadFileFragment"

class UploadFileFragment : Fragment() {
    private lateinit var viewModel: UploadFileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, UploadFileViewModelFactory()).get(UploadFileViewModel::class.java)

        val activityLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {uri ->
            viewModel.uri.value = uri
        }

        viewModel.openFileButtonClick = { activityLauncher.launch("audio/mpeg") }

        val binding = DataBindingUtil.inflate<UploadScreanFragmentLayoutBinding>(
            layoutInflater,
            R.layout.upload_screan_fragment_layout,
            container,
            false
        ).apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }
}