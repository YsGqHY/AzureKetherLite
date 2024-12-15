package kim.azure.kether.kether

import kim.azure.kether.data.KetherData.getItemStack
import kim.azure.kether.util.getBukkitPlayer
import org.bukkit.inventory.meta.ItemMeta
import taboolib.common5.cint
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import taboolib.module.nms.getName
import taboolib.platform.util.modifyMeta
import kotlin.jvm.optionals.getOrNull

/**
 * 扣除触发的物品数量
 * azure-item take 123
 *
 * 给予触发的物品数量
 * azure-item give 123
 *
 * 触发的物品重命名
 * azure-item rename "666 66 6"
 *
 * 获取原始物品名
 * azure-item name
 *
 * 获取原始物品 Lore, 列表类型, 可用 element 0 of azure-item lore 获取第一行 lore
 * azure-item lore
 */
@KetherParser(["azure-item"], shared = true)
fun parseItem() = combinationParser {
    it.group(
        text(),
        text().optional()
    ).apply(it) { oper, token ->
        now {
            when (oper) {
                "take", "remove", "consume" -> {
                    getItemStack().amount -= token.getOrNull().cint
                }

                "give", "add" -> {
                    val item = getItemStack().clone().also { i -> i.amount = 1 }
                    val inv = getBukkitPlayer().inventory
                    repeat(token.getOrNull().cint) {
                        inv.addItem(item)
                    }
                }

                "rename" -> {
                    getItemStack().modifyMeta<ItemMeta> { setDisplayName(token.getOrNull() ?: "null") }
                }

                "name" -> {
                    getItemStack().getName(getBukkitPlayer())
                }

                "lore" -> {
                    getItemStack().itemMeta?.lore ?: emptyList<String>()
                }

                else -> {
                    null
                }
            }
        }
    }
}
