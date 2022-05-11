package com.dsphoenix.soundvault.ui.uploadscreen.forms.pickfile

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.PickFileFragmentBinding
import com.dsphoenix.soundvault.ui.uploadscreen.CreateTrackViewModel
import com.dsphoenix.soundvault.utils.collectLatestLifeCycleFlow
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PickFileFragment :
    ViewBindingFragment<PickFileFragmentBinding>(PickFileFragmentBinding::inflate) {
    private val fileMimeType = "audio/mpeg"

    private val viewModel: CreateTrackViewModel by activityViewModels()

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
        binding.apply {
            viewModel.fileForm.apply {
                collectLatestLifeCycleFlow(filename) { filename ->
                    if (filename != etFileName.text.toString()) {
                        etFileName.setText(filename)
                    }
                }

                collectLatestLifeCycleFlow(authorName) { author ->
                    if (author != etAuthorName.text.toString()) {
                        etAuthorName.setText(author)
                    }
                }

                collectLatestLifeCycleFlow(uri) {
                    if (it != null) {
                        viewModel.fileForm.setFileName(getFileName(it))
                        showFilePickedIcon()
                    }
                }

                btnPickFile.setOnClickListener { onPickFileClick() }

                etFileName.doAfterTextChanged { text ->
                    setFileName(text.toString())
                }
                etAuthorName.doAfterTextChanged { text ->
                    setAuthorName(text.toString())
                }
            }
        }
    }

    private fun onPickFileClick() {
        activityLauncher.launch(fileMimeType)
    }

    private fun onFilePicked(uri: Uri) {
        viewModel.fileForm.setUri(uri)
        showFilePickedIcon()
    }

    private fun showFilePickedIcon() {
        binding.btnPickFile.background = getDrawable(resources, R.drawable.ic_folder, null)
        binding.ivDoneIcon.visibility = View.VISIBLE
    }

    private fun getFileName(uri: Uri) = File(uri.path!!).nameWithoutExtension
}