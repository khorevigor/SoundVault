package com.dsphoenix.soundvault.ui.uploadscreen.forms

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.PickGenresFragmentBinding
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import com.google.android.material.chip.Chip
import com.google.android.material.resources.MaterialResources.getColorStateList
import kotlin.random.Random

class PickGenresFragment :
    ViewBindingFragment<PickGenresFragmentBinding>(PickGenresFragmentBinding::inflate) {

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
        }
    }

    private fun onAddClicked() {
        val text = binding.autocompleteview.text
        if (!text.isNullOrEmpty()) {
            val chipColor = getRandomColor()
            val chip = layoutInflater.inflate(R.layout.chip, binding.chips, false) as Chip
            chip.apply {
                this.text = text
                chipStrokeColor = ColorStateList.valueOf(chipColor)
                setTextColor(chipColor)
                setOnClickListener {
                    binding.chips.removeView(it)
                }
            }
            binding.chips.addView(chip)
            binding.autocompleteview.text = null
        }
    }

    private fun getRandomColor(): Int = chipColors[Random.nextInt(chipColors.size)]
}