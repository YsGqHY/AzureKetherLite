package kim.azure.kether.util

import org.bukkit.entity.Player
import taboolib.common.platform.function.console
import taboolib.common5.Coerce
import taboolib.library.kether.ParsedAction
import taboolib.module.kether.*
import java.util.concurrent.CompletableFuture

fun ScriptFrame.getBukkitPlayer(name: ParsedAction<*>? = null): Player {
    val player = name?.let { org.bukkit.Bukkit.getPlayerExact(this.newFrame(name).run<String>().get()) }
        ?: script().sender?.castSafely<Player>()
        ?: error("No player selected.")
    return player
}

fun List<String>.parseKether(
    player: Player?,
    vars: Map<String, Any?> = mapOf(),
    sets: List<Pair<String, Any?>> = emptyList()
): List<String> {
    if (this.isEmpty()) {
        return listOf("")
    }
    return KetherFunction.parse(this, ScriptOptions.builder().apply {
        sender(player ?: console())
        vars(vars)
        sets.forEach {
            set(it.first, it.second)
        }
    }.build())
}

fun String?.parseKether(
    player: Player?,
    vars: Map<String, Any?> = mapOf(),
    sets: List<Pair<String, Any?>> = emptyList()
): String {
    if (this.isNullOrBlank()) {
        return ""
    }
    return KetherFunction.parse(this, ScriptOptions.builder().apply {
        sender(player ?: console())
        vars(vars)
        sets.forEach {
            set(it.first, it.second)
        }
    }.build())
}

fun List<String>.evalKether(
    player: Player?,
    vars: Map<String, Any?> = mapOf(),
    sets: List<Pair<String, Any?>> = emptyList()
): CompletableFuture<Any?> {
    if (isEmpty()) {
        val future = CompletableFuture<Any?>()
        future.complete(null)
        return future
    }
    if (size == 1) {
        return this[0].evalKether(player, vars, sets)
    }
    return KetherShell.eval(this, ScriptOptions.builder().apply {
        sender(player ?: console())
        vars(vars)
        sets.forEach {
            set(it.first, it.second)
        }
    }.build())
}

fun String?.evalKether(
    player: Player?,
    vars: Map<String, Any?> = mapOf(),
    sets: List<Pair<String, Any?>> = emptyList()
): CompletableFuture<Any?> {
    if (this.isNullOrBlank()) {
        val future = CompletableFuture<Any?>()
        future.complete(null)
        return future
    }
    return KetherShell.eval(this, ScriptOptions.builder().apply {
        sender(player ?: console())
        vars(vars)
        sets.forEach {
            set(it.first, it.second)
        }
    }.build())
}

fun String?.evalKetherValue(
    player: Player?,
    vars: Map<String, Any?> = mapOf(),
    sets: List<Pair<String, Any?>> = emptyList()
): Any? {
    if (this.isNullOrBlank()) {
        val future = CompletableFuture<Any?>()
        future.complete(null)
        return future
    }
    return KetherShell.eval(this, ScriptOptions.builder().apply {
        sender(player ?: console())
        vars(vars)
        sets.forEach {
            set(it.first, it.second)
        }
    }.build()).getNow("null")
}

fun String?.evalKetherBoolean(
    player: Player?,
    vars: Map<String, Any?> = mapOf(),
    sets: List<Pair<String, Any?>> = emptyList(),
    def: Boolean = true
): Boolean {
    if (this.isNullOrBlank()) {
        return def
    }

    /*
     * 预处理
     * 条件写 true / false 将直接绕过 Kether
     */
    if (this == "true") return true
    if (this == "false") return false

    return try {
        KetherShell.eval(this, ScriptOptions.builder().apply {
            sender(player ?: console())
            vars(vars)
            sets.forEach {
                set(it.first, it.second)
            }
        }.build()).thenApply {
            Coerce.toBoolean(it)
        }.get()
    } catch (_: Exception) {
        def
    }
}

/**
 * @param all 是否全部条件通过
 */
fun List<String>.evalKetherBoolean(
    player: Player?,
    vars: Map<String, Any?> = mapOf(),
    sets: List<Pair<String, Any?>> = emptyList(),
    def: Boolean = true,
    all: Boolean = true
): Boolean {
    if (this.isEmpty()) {
        return def
    }
    val condition = if (all) {
        if (this.all { it == "true" }) {
            return true
        }
        if (this.all { it == "false" }) {
            return false
        }
        "all"
    } else {
        if (this.any { it == "true" }) {
            return true
        }
        "any"
    } + "[ " + this.joinToString("\n") + " ]"
    return try {
        KetherShell.eval(condition, ScriptOptions.builder().apply {
            sender(player ?: console())
            vars(vars)
            sets.forEach {
                set(it.first, it.second)
            }
        }.build()).thenApply {
            Coerce.toBoolean(it)
        }.get()
    } catch (_: Exception) {
        def
    }
}
