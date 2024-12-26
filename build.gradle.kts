import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.izzel.taboolib.gradle.*
import io.izzel.taboolib.gradle.Basic


plugins {
    java
    id("io.izzel.taboolib") version "2.0.22"
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

taboolib {
    env {
        install(
            Bukkit,
            Kether,
            Basic,
            BukkitHook,
            BukkitNMS,
            BukkitNMSUtil,
            BukkitUtil,
            DatabasePlayer,
            I18n,
            JavaScript,
            Jexl,
            MinecraftChat,
            MinecraftEffect
        )
    }
    description {
        name = "AzureKetherLite"
        desc("让 AzureFlow 与 Kether 相亲相爱")
        dependencies {
            name("AzureFlow")
            name("MythicMobs").optional(true)
        }
    }
    version { taboolib = "6.2.0" }
    relocate("ink.ptms.um","kim.azure.kether.um")
}

repositories {
    mavenCentral()
}

dependencies {
    taboo("ink.ptms:um:1.1.2")
    compileOnly("ink.ptms.core:v12004:12004:mapped")
    compileOnly("ink.ptms.core:v12004:12004:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
