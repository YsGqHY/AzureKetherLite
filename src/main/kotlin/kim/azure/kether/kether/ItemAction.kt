package kim.azure.kether.kether

import kim.azure.kether.getBukkitPlayer
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common5.cint
import taboolib.module.kether.KetherParser
import taboolib.module.kether.ScriptFrame
import taboolib.module.kether.combinationParser
import taboolib.platform.util.modifyMeta

/**
 * 扣除触发的物品数量
 * azure-item take 123
 *
 * 给予触发的物品数量
 * azure-item give 123
 *
 * 触发的物品重命名
 * azure-item rename "666 66 6"
 */
object ItemAction {
    /**
     * 获取物品原始 ItemStack
     */
    fun ScriptFrame.getItemStack(): ItemStack {
        return variables().get<Any?>("@AzureItemStack").orElse(null) as? ItemStack ?: error("No item selected.")
    }

    @KetherParser(["azure-item"], shared = true)
    fun put() = combinationParser {
        it.group(
            text(),
            text()
        ).apply(it) { oper, token ->
            now {
                when (oper) {
                    "take", "remove", "consume" -> {
                        getItemStack().amount -= token.cint
                    }

                    "give", "add" -> {
                        val item = getItemStack().clone().also { i -> i.amount = 1 }
                        val inv = getBukkitPlayer().inventory
                        repeat(token.cint) {
                            inv.addItem(item)
                        }
                    }

                    "rename" -> {
                        getItemStack().modifyMeta<ItemMeta> { setDisplayName(token) }
                    }

                    else -> {
                        null
                    }
                }
            }
        }
    }

}