package com.dsphoenix.soundvault.ui.uploadscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dsphoenix.soundvault.R

private const val TAG = "UploadFileFragment"

class UploadFileFragment : Fragment(R.layout.upload_screan_fragment_layout) {
    private lateinit var viewModelFactory: UploadFileViewModelFactory
    private lateinit var viewModel: UploadFileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModelFactory = UploadFileViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(UploadFileViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}