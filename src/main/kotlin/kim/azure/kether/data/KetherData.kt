package kim.azure.kether.data

import io.rokuko.azureflow.features.action.ActionBindings
import io.rokuko.azureflow.features.action.AzureFlowActionableObject
import io.rokuko.azureflow.features.item.AzureFlowItem
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.LifeCycle
import taboolib.common.io.newFile
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.getDataFolder
import taboolib.expansion.releaseDataContainer
import taboolib.expansion.setupDataContainer
import taboolib.expansion.setupPlayerDatabase
import taboolib.module.kether.ScriptFrame

object KetherData {

    const val AZURE_COOLDOWN_PREFIX = "afk_cooldown"

    /**
     * 获取物品原始 ItemStack
     */
    fun ScriptFrame.getItemStack(): ItemStack {
        return variables().get<Any?>("@AzureItemStack").orElse(null) as? ItemStack ?: error("No item selected.")
    }

    fun ScriptFrame.getAzureItem(): AzureFlowItem {
        return variables().get<Any?>("@AzureFlowItem").orElse(null) as? AzureFlowItem ?: error("No item selected.")
    }


    fun ScriptFrame.getAzureActionBindings(): ActionBindings {
        return variables().get<Any?>("@AzureActionBindings").orElse(null) as? ActionBindings
            ?: error("No bindings selected.")
    }

    fun ScriptFrame.getAzureFlowActionableObject(): AzureFlowActionableObject {
        return variables().get<Any?>("@AzureFlowActionableObject").orElse(null) as? AzureFlowActionableObject
            ?: error("No object selected.")
    }

    @Awake(LifeCycle.ENABLE)
    private fun onEnable() {
        setupPlayerDatabase(newFile(getDataFolder(), "data.db"))
    }

    @SubscribeEvent
    private fun onJoin(e: PlayerJoinEvent) {
        e.player.setupDataContainer()
    }

    @SubscribeEvent
    private fun onQuit(e: PlayerQuitEvent) {
        e.player.releaseDataContainer()
    }

}