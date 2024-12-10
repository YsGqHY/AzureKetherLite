package kim.azure.kether

import io.rokuko.azureflow.features.addons.Addon
import io.rokuko.azureflow.features.addons.AddonService
import kim.azure.kether.resolver.KetherResolver
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info
import taboolib.common.platform.function.pluginId

object AzureKetherLite : Plugin(), Addon {

    override fun onLoad() {
        AddonService.register(this)
    }

    override fun onEnable() {
        info("Successfully running AzureKetherLite!")
    }

    override fun onDisable() {
        AddonService.unRegister(this)
    }

    override fun getName(): String {
        return pluginId
    }

    override fun register() {
        KetherResolver.register()
    }

    override fun unRegister() {
    }
}