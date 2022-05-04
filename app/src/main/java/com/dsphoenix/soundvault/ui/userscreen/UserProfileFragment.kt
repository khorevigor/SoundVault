package com.dsphoenix.soundvault.ui.userscreen

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.UserProfileFragmentBinding
import com.dsphoenix.soundvault.ui.homescreen.TracksAdapter
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment: ViewBindingFragment<UserProfileFragmentBinding>(UserProfileFragmentBinding::inflate) {

    private val viewModel: UserProfileViewModel by activityViewModels()

    private val fileMimeType = arrayOf("image/png", "image/jpeg")
    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                onImagePicked(uri)
            }
        }

    private var initialEditTextBackground: Drawable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        disableEditMode()
        binding.apply {
            rvTracks.adapter = TracksAdapter()
            val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            decorator.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider, null)!!)
            rvTracks.addItemDecoration(decorator)

            etUserName.doAfterTextChanged { text ->
                viewModel.setUserName(text.toString())
            }

            viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
                (rvTracks.adapter as TracksAdapter).setData(tracks)
            }

            viewModel.user.observe(viewLifecycleOwner) { user ->
                if (user.name != etUserName.text.toString()) {
                    etUserName.setText(user.name ?: "Placeholder name")
                }

                Glide.with(requireContext()).load(user.avatarUri ?: user.avatarPath).placeholder(R.drawable.hemsworth_icon).into(ivUserAvatar)
            }

            editButton.setOnClickListener { enableEditMode() }
            saveButton.setOnClickListener {
                viewModel.saveChanges()
                disableEditMode() }
            fabUploadAvatar.setOnClickListener { pickFileToUpload() }
        }
    }

    private fun enableEditMode() {
        binding.apply {
            editButton.visibility = View.GONE
            saveButton.visibility = View.VISIBLE
            fabUploadAvatar.visibility = View.VISIBLE

            enableNameEdit()
        }
    }

    private fun disableEditMode() {
        binding.apply {
            editButton.visibility = View.VISIBLE
            saveButton.visibility = View.GONE
            fabUploadAvatar.visibility = View.GONE

            disableNameEdit()
        }
    }

    private fun disableNameEdit() {
        binding.etUserName.apply {
            initialEditTextBackground = background
            background = null
            isEnabled = false
        }
    }

    private fun enableNameEdit() {
        binding.etUserName.apply {
            background = initialEditTextBackground
            isEnabled = true
        }
    }

    private fun pickFileToUpload() {
        activityLauncher.launch(fileMimeType)
    }

    private fun onImagePicked(uri: Uri) {
        viewModel.setAvatarUri(uri)
    }
}