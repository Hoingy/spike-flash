plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.1.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    compileOnly(buildString {
        append("io.papermc.paper:paper-api:26.3.build.+")
    })
    compileOnly("net.dmulloy2:ProtocolLib:5.4.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("26.3")
        jvmArgs("-Xms2G", "-Xmx2G")
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

tasks.jar {
    archiveFileName.set("SpikeFlash.jar")      // fixed name, no version suffix
}

tasks.register<Copy>("deploy") {
    dependsOn(tasks.jar)
    from(tasks.jar.flatMap { it.archiveFile })
    into("C:/Paper Server/plugins")
    outputs.upToDateWhen { false }   // always run
}
