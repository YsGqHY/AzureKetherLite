package kim.azure.kether.resolver

import io.rokuko.azureflow.features.action.ActionBindings
import io.rokuko.azureflow.features.action.AzureFlowActionableObject
import io.rokuko.azureflow.features.logger.log
import io.rokuko.azureflow.features.resolver.provided.PrefixSupportResolver
import kim.azure.kether.util.evalKether
import taboolib.common.platform.function.pluginId
import taboolib.common.platform.function.submitAsync
import taboolib.module.kether.runKether

object KetherResolver : PrefixSupportResolver("ke") {

    override fun doAction(
        args: String,
        actionBindings: ActionBindings,
        actionableObject: AzureFlowActionableObject
    ): Boolean {
        submitAsync {
            runKether(detailError = true) {
                args.evalKether(
                    actionableObject.player,
                    mapOf(
                        "item-amount" to actionableObject.itemStack?.amount
                    ),
                    listOf(
                        "@AzureItemStack" to actionableObject.itemStack,
                        "@AzureFlowItem" to actionableObject.item,
                        "@AzureActionBindings" to actionBindings,
                        "@AzureFlowActionableObject" to actionableObject
                    )
                )
            }
        }
        return true
    }

    override fun register() {
        super.register()
        log("成功注册附属 [ $pluginId ( &f&l$name&7 ) ]")
    }
}