import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.izzel.taboolib.gradle.*


plugins {
    id("io.izzel.taboolib") version "2.0.27"
    kotlin("jvm") version "2.1.0"
    java
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
        repoTabooLib = "https://repo.aeoliancloud.com/repository/releases"
    }
    description {
        name = "AzureKetherLite"
        desc("让 AzureFlow 与 Kether 相亲相爱")
        dependencies {
            name("AzureFlow")
            name("MythicMobs").optional(true)
        }
    }
    version { taboolib = "6.2.3-9e947e4" }
    relocate("ink.ptms.um","kim.azure.kether.um")
}

repositories {
    maven("https://repo.aeoliancloud.com/repository/releases") { isAllowInsecureProtocol = true }
    mavenCentral()
}

dependencies {
    taboo("ink.ptms:um:1.1.5")
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
