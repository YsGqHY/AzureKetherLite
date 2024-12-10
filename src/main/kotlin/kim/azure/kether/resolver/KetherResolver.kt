package kim.azure.kether.resolver

import io.rokuko.azureflow.features.action.ActionBindings
import io.rokuko.azureflow.features.action.AzureFlowActionableObject
import io.rokuko.azureflow.features.resolver.provided.PrefixSupportResolver
import kim.azure.kether.evalKether
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object KetherResolver: PrefixSupportResolver("ke") {
    override fun doAction(
        args: String,
        actionBindings: ActionBindings,
        actionableObject: AzureFlowActionableObject
    ): Boolean {
        args.evalKether(actionableObject.player)
        return true
    }

    @Awake(LifeCycle.ENABLE)
    fun init() {
        register()
    }
}