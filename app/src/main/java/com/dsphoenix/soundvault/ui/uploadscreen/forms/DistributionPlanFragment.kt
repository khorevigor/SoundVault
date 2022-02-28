package com.dsphoenix.soundvault.ui.uploadscreen.forms

import android.os.Bundle
import android.view.View
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.DistributionPlanFragmentBinding
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment

class DistributionPlanFragment :
    ViewBindingFragment<DistributionPlanFragmentBinding>(DistributionPlanFragmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.apply {
            tvSingleSwitchLabel.setOnClickListener { switchSingle.toggle() }
            tvAlbumPartSwitchLabel.setOnClickListener { switchAlbumPart.toggle() }
            tvPlaylistPartSwitchLabel.setOnClickListener { switchPlaylistPart.toggle() }

            rgDistributionPlan.setOnCheckedChangeListener { _, optionId ->
                when (optionId) {
                    R.id.rb_free_for_all -> hideSwitchesLayout()
                    R.id.rb_subscription -> hideSwitchesLayout()
                    R.id.rb_one_time_purchase -> showSwitchesLayout()
                }
            }

            switchSingle.setOnCheckedChangeListener { _, checked ->
                etSinglePrice.visibility = if (checked) View.VISIBLE else View.GONE
                tvSinglePriceLabel.visibility = if (checked) View.VISIBLE else View.GONE
            }
        }
    }

    private fun showSwitchesLayout() {
        binding.switchesLayout.visibility = View.VISIBLE
    }

    private fun hideSwitchesLayout() {
        binding.switchesLayout.visibility = View.GONE
    }
}
