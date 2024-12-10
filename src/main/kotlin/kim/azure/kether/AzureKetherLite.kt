package kim.azure.kether

import kim.azure.kether.resolver.KetherResolver
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object AzureKetherLite : Plugin() {

    override fun onEnable() {
        info("Successfully running AzureKetherLite!")
    }
}