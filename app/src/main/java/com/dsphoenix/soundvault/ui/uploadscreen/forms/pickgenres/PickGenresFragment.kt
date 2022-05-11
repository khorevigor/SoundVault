package com.dsphoenix.soundvault.ui.uploadscreen.forms.pickgenres

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.PickGenresFragmentBinding
import com.dsphoenix.soundvault.ui.uploadscreen.CreateTrackViewModel
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import com.google.android.material.chip.Chip
import kotlin.random.Random

class PickGenresFragment :
    ViewBindingFragment<PickGenresFragmentBinding>(PickGenresFragmentBinding::inflate) {

    private val viewModel: CreateTrackViewModel by activityViewModels()

    private lateinit var chipColors: IntArray

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chipColors = resources.getIntArray(R.array.chipsColors)
        setupView()
    }

    private fun setupView() {
        val genres = AutocompleteDictionaryStub.strings
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, genres)
        binding.apply {
            autocompleteview.setAdapter(adapter)
            btnAdd.setOnClickListener { onAddClicked() }
            viewModel.genresForm.genres.value.map {
                val (text, color) = it
                addChip(text, color)
            }
        }
    }

    private fun onAddClicked() {
        val text = binding.autocompleteview.text.toString()
        val color = getRandomColor()
        addGenre(text, color)
        addChip(text, color)
    }

    private fun addGenre(text: String, color: Int) {
        viewModel.genresForm.addGenre(text, color)
    }

    private fun removeGenre(text: String) {
        viewModel.genresForm.removeGenre(text)
    }

    private fun addChip(text: String, color: Int) {
        if (text.isNotEmpty()) {
            val chip = layoutInflater.inflate(R.layout.chip, binding.chips, false) as Chip
            chip.apply {
                this.text = text
                chipStrokeColor = ColorStateList.valueOf(color)
                setTextColor(color)
                setOnClickListener {
                    binding.chips.removeView(it)
                    removeGenre(text)
                }
            }
            binding.chips.addView(chip)
            binding.autocompleteview.text = null
        }
    }

    private fun getRandomColor() = chipColors[Random.nextInt(chipColors.size)]
}