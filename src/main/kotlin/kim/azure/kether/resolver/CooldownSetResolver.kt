package kim.azure.kether.resolver

import io.rokuko.azureflow.features.action.ActionBindings
import io.rokuko.azureflow.features.action.AzureFlowActionableObject
import io.rokuko.azureflow.features.item.AzureFlowItem
import io.rokuko.azureflow.features.logger.log
import io.rokuko.azureflow.features.resolver.provided.PrefixSupportResolver
import kim.azure.kether.kether.setItemInCooldown
import taboolib.common.platform.function.pluginId
import taboolib.common5.clong

/**
 * 设置玩家冷却 1000 毫秒
 * akl-set-cd: 1000 player
 *
 * 设置物品冷却 1000 毫秒
 * akl-set-cd: 1000
 */
object CooldownSetResolver : PrefixSupportResolver("akl-set-cd") {
    override fun doAction(
        args: String,
        actionBindings: ActionBindings,
        actionableObject: AzureFlowActionableObject
    ): Boolean {
        val player = actionableObject.player ?: return true
        val item = actionableObject.item as? AzureFlowItem ?: return true
        val itemStack = actionableObject.itemStack ?: return true
        val split = args.trim().split(" ")
        val time = split[0].clong
        val byPlayer = split.getOrElse(1) { "item" }.equals("player", true)
        item.setItemInCooldown(time, player, byPlayer, itemStack)
        return true
    }

    override fun register() {
        super.register()
        log("成功注册附属 [ $pluginId ( &f&l$name&7 ) ]")
    }
}