package com.dsphoenix.soundvault.ui.uploadscreen.forms.distributionplan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.dsphoenix.soundvault.utils.ValidatedForm
import com.dsphoenix.soundvault.utils.constants.DistributionBundle
import com.dsphoenix.soundvault.utils.constants.DistributionPlan

class DistributionPlanForm: ValidatedForm {
    private val _distributionPlan = MutableLiveData<DistributionPlan>()
    val distributionPlan: LiveData<DistributionPlan> = _distributionPlan

    private val bundles = mutableSetOf<DistributionBundle>()
    private val _distributionBundles = MutableLiveData<Set<DistributionBundle>>()
    val distributionBundle: LiveData<Set<DistributionBundle>> = _distributionBundles

    private val _singlePrice = MutableLiveData<String>()
    val singlePrice: LiveData<String> = _singlePrice

    override val isValid = MediatorLiveData<Boolean>()

    init {
        isValid.apply {
            addSource(_distributionPlan) { isValid.value = validate() }
            addSource(_distributionBundles) { isValid.value = validate() }
            addSource(_singlePrice) { isValid.value = validate() }
        }
    }

    private fun validate(): Boolean {
        val isDistributionPlanValid = _distributionPlan.value != null
        if (_distributionPlan.value != DistributionPlan.ONE_TIME_PURCHASE) return isDistributionPlanValid

        val isBundleValid = bundles.isNotEmpty()
        if (!bundles.contains(DistributionBundle.SINGLE)) return isDistributionPlanValid && isBundleValid

        val isPriceValid = !_singlePrice.value.isNullOrEmpty()

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