package io.github.black_Kittys22.tutorial

import de.miraculixx.kpaper.main.KPaper
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.black_Kittys22.tutorial.commands.Circle
import io.github.black_Kittys22.tutorial.Timer
import io.github.black_Kittys22.tutorial.commands.FirstCommand
import io.github.black_Kittys22.tutorial.commands.TimerCommand
import net.kyori.adventure.text.Component
import java.io.File

class Tutorial : KPaper() {

    override fun load() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
        server.consoleSender.sendMessage(Component.text("Hey, I have been loaded!"))
    }

    override fun startup() {
        CommandAPI.onEnable()

        // Register commands and features
        DamageEvent
        Timer
        FirstCommand()
        TimerCommand()
        Circle()

        // Create commands.txt
        val pluginFolder = this.dataFolder
        if (!pluginFolder.exists()) pluginFolder.mkdirs()

        val file = File(pluginFolder, "commands.txt")
        val content = """
            # Action and More Plugin

            # This plugin includes:
            # – A timer in the colors of BastiGHG ZickZack V4
            # – A message when someone takes damage
            # – /test hey        → You receive a hey
            # – /test goldify    → Turns the selected block into gold
            # – /test select     → Choose between two options
            # – /test number     → You can enter a number
            # – /test text       → You can enter text
            # – /test selector   → You can use selectors like @a, @p
            # – /test types      → Spawns a specific object
            # – /circle          → Creates a circle with given radius and block
        """.trimIndent()

        file.writeText(content)

        server.consoleSender.sendMessage(Component.text("commands.txt has been created!"))
    }

    override fun shutdown() {
        CommandAPI.onDisable()
        server.consoleSender.sendMessage(Component.text("I have been disabled :("))
    }
}
