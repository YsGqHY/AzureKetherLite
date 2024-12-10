package kim.azure.kether.resolver

import io.rokuko.azureflow.AzureFlow
import io.rokuko.azureflow.features.action.ActionBindings
import io.rokuko.azureflow.features.action.AzureFlowActionableObject
import io.rokuko.azureflow.features.resolver.provided.PrefixSupportResolver
import kim.azure.kether.evalKether
import taboolib.common.platform.function.pluginId

object KetherResolver : PrefixSupportResolver("ke") {

    val actionName: String
        get() = "ke"

    override fun doAction(
        args: String,
        actionBindings: ActionBindings,
        actionableObject: AzureFlowActionableObject
    ): Boolean {
        args.evalKether(actionableObject.player, mapOf("item-amount" to actionableObject.itemStack?.amount))
        return true
    }

    override fun register() {
        AzureFlow.print("正在加载注册附属动作 [ $pluginId ( &f&l$actionName&7 ) ] 中..")
        super.register()
        AzureFlow.print("成功注册附属 [ $pluginId ( &f&l$actionName&7 ) ]")
    }
}