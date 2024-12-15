package kim.azure.kether.kether

import io.rokuko.azureflow.features.item.AzureFlowItem
import kim.azure.kether.data.KetherData.getAzureItem
import kim.azure.kether.data.KetherData.getItemStack
import kim.azure.kether.util.Calculator
import kim.azure.kether.util.getBukkitPlayer
import taboolib.module.kether.KetherParser
import taboolib.module.kether.actionNow
import taboolib.module.kether.combinationParser
import taboolib.module.kether.scriptParser
import kotlin.jvm.optionals.getOrNull

fun AzureFlowItem.setData(data: String, value: Any?): Any? {
    set(data, value)
    return value
}

fun AzureFlowItem.getData(data: String, def: Any?): Any? {
    return get(data) ?: def
}

/**
 * 获取 key 的 data 值, 若无返回 123
 * azure-data get key def 123
 *
 * 设置 key 的 data 值
 * azure-data set key to value
 *
 * 获取值计算并设置 key 的 data 值
 * azure-data calc key to inline "+ {{ azure-data get key def 123 }}+ 2 - 3" def 1
 */
@KetherParser(["azure-data"], shared = true)
fun parseData() = combinationParser {
    it.group(
        symbol(),
        text(),
        command("to", then = text()).optional(),
        command("def", then = text()).optional()
    ).apply(it) { oper, key, value, default ->
        now {
            when (oper) {
                "get" -> {
                    getAzureItem().getData(key, default.getOrNull())
                }

                "set" -> {
                    getAzureItem().setData(key, value.getOrNull())
                }

                "calc", "math" -> {
                    val def = getAzureItem().getData(key, default.getOrNull())
                    val calcValue = Calculator.getResult("$def${value.getOrNull()}")
                    getAzureItem().setData(key, calcValue)
                }

                else -> null
            }
        }
    }
}

/**
 * 设置完 data 值后更新操作的物品
 * azure-update
 */
@KetherParser(["azure-update"], shared = true)
fun parseUpdate() = scriptParser {
    actionNow {
        getAzureItem().update(getBukkitPlayer(), getItemStack())
    }
}
