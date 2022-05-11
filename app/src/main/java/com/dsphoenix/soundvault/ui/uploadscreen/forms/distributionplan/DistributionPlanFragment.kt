package com.dsphoenix.soundvault.ui.uploadscreen.forms.distributionplan

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.dsphoenix.soundvault.R
import com.dsphoenix.soundvault.databinding.DistributionPlanFragmentBinding
import com.dsphoenix.soundvault.ui.uploadscreen.CreateTrackViewModel
import com.dsphoenix.soundvault.utils.TAG
import com.dsphoenix.soundvault.utils.collectLatestLifeCycleFlow
import com.dsphoenix.soundvault.utils.constants.DistributionBundle
import com.dsphoenix.soundvault.utils.constants.DistributionPlan
import com.dsphoenix.soundvault.utils.viewbinding.ViewBindingFragment
import java.lang.IllegalArgumentException

class DistributionPlanFragment :
    ViewBindingFragment<DistributionPlanFragmentBinding>(DistributionPlanFragmentBinding::inflate) {

    private val viewModel: CreateTrackViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
    }

    private fun setupView() {
        binding.apply {

            viewModel.distributionPlanForm.apply {
                collectLatestLifeCycleFlow(distributionPlan) { plan ->
                    plan?.let { rgDistributionPlan.check(it.getRadioButtonId()) }
                }

                collectLatestLifeCycleFlow(distributionBundles) { bundles ->
                    bundles.map { it.getSwitch().isChecked = true }
                }

                collectLatestLifeCycleFlow(singlePrice) { price ->
                    if (price != etSinglePrice.text.toString()) {
                        etSinglePrice.setText(price)
                    }
                }

                rgDistributionPlan.setOnCheckedChangeListener { _, optionId ->
                    when (optionId) {
                        R.id.rb_free_for_all -> {
                            setDistributionPlan(DistributionPlan.FREE_FOR_ALL)
                            hideSwitchesLayout()
                        }
                        R.id.rb_subscription -> {
                            setDistributionPlan(DistributionPlan.SUBSCRIPTION)
                            hideSwitchesLayout()
                        }
                        R.id.rb_one_time_purchase -> {
                            setDistributionPlan(DistributionPlan.ONE_TIME_PURCHASE)
                            showSwitchesLayout()
                        }
                    }
                }
            }

            tvSingleSwitchLabel.setOnClickListener { switchSingle.toggle() }
            tvAlbumPartSwitchLabel.setOnClickListener { switchAlbumPart.toggle() }
            tvPlaylistPartSwitchLabel.setOnClickListener { switchPlaylistPart.toggle() }

            switchPlaylistPart.setOnCheckedChangeListener { _, checked ->
                handleBundleSwitchCheckChanged(checked, DistributionBundle.PLAYLIST)
            }

            switchAlbumPart.setOnCheckedChangeListener { _, checked ->
                handleBundleSwitchCheckChanged(checked, DistributionBundle.ALBUM)
            }

            switchSingle.setOnCheckedChangeListener { _, checked ->
                handleBundleSwitchCheckChanged(checked, DistributionBundle.SINGLE)
                if (!checked) {
                    viewModel.distributionPlanForm.removeSinglePrice()
                    etSinglePrice.text.clear()
                }
                etSinglePrice.visibility = if (checked) View.VISIBLE else View.GONE
                tvSinglePriceLabel.visibility = if (checked) View.VISIBLE else View.GONE
            }

            etSinglePrice.doAfterTextChanged { text ->
                viewModel.distributionPlanForm.setSinglePrice(text.toString())
            }
        }
    }

    private fun handleBundleSwitchCheckChanged(checked: Boolean, bundle: DistributionBundle) {
        viewModel.distributionPlanForm.apply {
            if (checked) {
                addDistributionBundle(bundle)
            }
            else {
                removeDistributionBundle(bundle)
            }
        }
    }

    private fun showSwitchesLayout() {
        binding.switchesLayout.visibility = View.VISIBLE
    }

    private fun hideSwitchesLayout() {
        binding.switchesLayout.visibility = View.GONE
    }

    private fun DistributionPlan.getRadioButtonId() = when (this) {
        DistributionPlan.FREE_FOR_ALL -> R.id.rb_free_for_all
        DistributionPlan.SUBSCRIPTION -> R.id.rb_subscription
        DistributionPlan.ONE_TIME_PURCHASE -> R.id.rb_one_time_purchase
    }

    private fun DistributionBundle.getSwitch() = when (this) {
        DistributionBundle.ALBUM -> binding.switchAlbumPart
        DistributionBundle.PLAYLIST -> binding.switchPlaylistPart
        DistributionBundle.SINGLE -> binding.switchSingle
    }
}
