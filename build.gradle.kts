import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.serialization") version "2.1.21"

    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"

    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "io.github.black_Kittys22"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")

    implementation("de.miraculixx", "kpaper", "1.1.2")
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:10.0.1")
    implementation("dev.jorel:commandapi-bukkit-kotlin:10.0.1")

}


tasks {
    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    runServer {
        minecraftVersion("1.21.5")
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}

tasks.shadowJar {
    archiveBaseName.set("Tutorial")
    archiveVersion.set(version.toString())
    archiveClassifier.set("")
    archiveFileName.set("Tutorial_v${version}.jar")

    manifest {
        attributes["Main-Class"] = "io.github.black_Kittys22.tutorial.Tutorial"
    }
}