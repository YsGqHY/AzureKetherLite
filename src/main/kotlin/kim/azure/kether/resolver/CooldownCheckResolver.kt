package kim.azure.kether.resolver

import io.rokuko.azureflow.features.action.ActionBindings
import io.rokuko.azureflow.features.action.AzureFlowActionableObject
import io.rokuko.azureflow.features.item.AzureFlowItem
import io.rokuko.azureflow.features.logger.log
import io.rokuko.azureflow.features.resolver.provided.PrefixSupportResolver
import kim.azure.kether.kether.isItemInCooldown
import taboolib.common.platform.function.pluginId

/**
 * 检测玩家冷却
 * akl-check-cd: player
 *
 * 检测物品冷却
 * akl-check-cd: item
 * akl-check-cd
 */
object CooldownCheckResolver : PrefixSupportResolver("akl-check-cd") {
    override fun doAction(
        args: String,
        actionBindings: ActionBindings,
        actionableObject: AzureFlowActionableObject
    ): Boolean {
        val byPlayer = args.trim().equals("player", true)
        val item = actionableObject.item as? AzureFlowItem ?: return true
        val player = actionableObject.player ?: return true
        throw NotImplementedError("功能未实现")
//        if (item.isItemInCooldown(player, byPlayer)) {
//        } else {
//        }
//
//        return true
    }


    override fun register() {
        super.register()
        log("成功注册附属 [ $pluginId ( &f&l$name&7 ) ]")

    }

}