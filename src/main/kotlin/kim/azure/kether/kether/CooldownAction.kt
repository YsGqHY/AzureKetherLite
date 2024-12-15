package kim.azure.kether.kether

import io.rokuko.azureflow.features.item.AzureFlowItem
import kim.azure.kether.data.KetherData.AZURE_COOLDOWN_PREFIX
import kim.azure.kether.data.KetherData.getAzureItem
import kim.azure.kether.data.KetherData.getItemStack
import kim.azure.kether.util.getBukkitPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common5.clong
import taboolib.expansion.getDataContainer
import taboolib.library.kether.*
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import java.time.Duration
import kotlin.jvm.optionals.getOrNull

/**
 * 判断是否在冷却
 * @param byPlayer 是否绑定到玩家
 */
fun AzureFlowItem.isItemInCooldown(player: Player, byPlayer: Boolean): Boolean {
    return getItemInCooldown(player, byPlayer) > 0
}

/**
 * 设置冷却, 只是写入内存，并不会写入数据库中
 * @param byPlayer 是否绑定到玩家
 */
fun AzureFlowItem.setItemInCooldown(gameTick: Long, player: Player, byPlayer: Boolean, itemStack: ItemStack) {
    val nextTime = System.currentTimeMillis() + gameTick
    if (byPlayer) {
        player.getDataContainer()["$AZURE_COOLDOWN_PREFIX.$uuid"] = nextTime
    } else {
        set(AZURE_COOLDOWN_PREFIX, nextTime)
        update(player, itemStack)
    }
}

/**
 * 获取冷却剩余时间
 * @param byPlayer 是否绑定到玩家
 */
fun AzureFlowItem.getItemInCooldown(player: Player, byPlayer: Boolean): Long {
    val time = if (byPlayer) {
        player.getDataContainer()["$AZURE_COOLDOWN_PREFIX.$uuid"].clong
    } else {
        get<Long>(AZURE_COOLDOWN_PREFIX) ?: 0
    }
    return time - System.currentTimeMillis()
}

@Throws(LocalizedException::class)
fun read(reader: String): Duration {
    var s = reader.uppercase()
    if (!s.contains("T")) {
        if (s.contains("D")) {
            if (s.contains("H") || s.contains("M") || s.contains("S")) {
                s = s.replace("D", "DT")
            }
        } else if (s.startsWith("P")) {
            s = "PT" + s.substring(1)
        } else {
            s = "T$s"
        }
    }

    if (!s.startsWith("P")) {
        s = "P$s"
    }

    try {
        return Duration.parse(s)
    } catch (var4: Exception) {
        throw LoadError.NOT_DURATION.create(*arrayOf<Any>(s))
    }
}

/**
 * 判断物品/玩家是否冷却
 * azure-cd check for [item/player]
 *
 * 获取物品/玩家的冷却剩余时间
 * azure-cd time for [item/player]
 *
 * 获取物品/玩家的冷却 (单位Ticks)
 * azure-cd set 20s for [item/player]
 *
 * 默认检测物品是否在冷却
 * azure-cd check
 */
@KetherParser(["azure-cooldown", "azure-cd"], shared = true)
fun parseCooldown() = combinationParser {
    it.group(
        symbol(),
        text().optional(),
        command("for", "by", then = symbol()).optional()
    ).apply(it) { oper, time, target ->
        now {
            val byPlayer = target.getOrNull().equals("player", ignoreCase = true)
            val player = getBukkitPlayer()
            when (oper) {
                "check" -> {
                    getAzureItem().isItemInCooldown(player, byPlayer)
                }

                "time" -> {
                    getAzureItem().getItemInCooldown(player, byPlayer)
                }

                "set" -> {

                    getAzureItem().setItemInCooldown(
                        read(time.getOrNull() ?: return@now null).toMillis(),
                        player,
                        byPlayer,
                        getItemStack()
                    )
                }

                else -> null
            }
        }
    }
}

