package kim.azure.kether.kether

import kim.azure.kether.data.KetherData.getAzureFlowActionableObject
import kim.azure.kether.data.KetherData.getAzureItem
import taboolib.module.kether.KetherParser
import taboolib.module.kether.combinationParser
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.jvm.optionals.getOrElse

/**
 * azure-emit 其他触发器名称
 */
@KetherParser(["azure-emit"], shared = true)
fun parseEmit() = combinationParser {
    it.group(
        text(),
    ).apply(it) {
        now {
            getAzureItem().emit(it, getAzureFlowActionableObject())
        }
    }
}

/**
 * 正则匹配文本
 * azure-regex "匹配文本" by "正则表达式" group 括号组(整数) def 匹配不到时的文本
 * azure-regex "需要 匹配的 文本" by ".*(匹配的).*" group 1 def 匹配不到
 */
@KetherParser(["azure-regex"], shared = true)
fun parseRegex() = combinationParser {
    it.group(
        text(),
        command("by", then = text()),
        command("group", then = int()),
        command("def", then = text()).optional()
    ).apply(it) { label, pattern, group, default ->
        now {
            val regex = Pattern.compile(pattern)

            val matcher: Matcher = regex.matcher(label)

            if (matcher.find()) {
                matcher.group(group)
            } else {
                default.getOrElse { label }
            }
        }
    }
}