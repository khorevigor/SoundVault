package com.dsphoenix.soundvault.ui.uploadscreen.forms.distributionplan

import com.dsphoenix.soundvault.utils.ValidatedForm
import com.dsphoenix.soundvault.utils.constants.DistributionBundle
import com.dsphoenix.soundvault.utils.constants.DistributionPlan
import kotlinx.coroutines.flow.*

class DistributionPlanForm : ValidatedForm {

    private val _distributionPlan = MutableStateFlow<DistributionPlan?>(null)
    val distributionPlan = _distributionPlan.asStateFlow()

    private val bundles = mutableSetOf<DistributionBundle>()
    private val _distributionBundles = MutableStateFlow(setOf<DistributionBundle>())
    val distributionBundles = _distributionBundles.asStateFlow()

    private val _singlePrice = MutableStateFlow("")
    val singlePrice = _singlePrice.asStateFlow()

    override val isValid: Flow<Boolean>

    init {
        isValid = combine(
            _distributionPlan,
            _distributionBundles,
            _singlePrice
        ) { _, _, _ ->
            validateFlows()
        }

    }

    private fun validateFlows(): Boolean {
        val isDistributionPlanValid = _distributionPlan.value != null
        if (_distributionPlan.value != DistributionPlan.ONE_TIME_PURCHASE) return isDistributionPlanValid

        val isBundleValid = bundles.isNotEmpty()
        if (!bundles.contains(DistributionBundle.SINGLE)) return isDistributionPlanValid && isBundleValid

        val isPriceValid = _singlePrice.value != "" && _singlePrice.value.toInt() > 0

        return isDistributionPlanValid && isBundleValid && isPriceValid
    }

    fun setDistributionPlan(plan: DistributionPlan) {
        _distributionPlan.value = plan
    }

    fun addDistributionBundle(bundle: DistributionBundle) {
        bundles.add(bundle)
        _distributionBundles.value = bundles
    }

    fun removeDistributionBundle(bundle: DistributionBundle) {
        bundles.remove(bundle)
        _distributionBundles.value = bundles
    }

    fun setSinglePrice(price: String) {
        _singlePrice.value = price
    }

    fun removeSinglePrice() {
        setSinglePrice("")
    }
}