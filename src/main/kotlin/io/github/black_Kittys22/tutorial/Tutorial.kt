package io.github.black_Kittys22.tutorial

import de.miraculixx.kpaper.main.KPaper
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import io.github.black_Kittys22.tutorial.commands.Timer
import io.github.black_Kittys22.tutorial.commands.FirstCommand
import io.github.black_Kittys22.tutorial.commands.MobsToBlocks
import io.github.black_Kittys22.tutorial.commands.TimerCommand
import net.kyori.adventure.text.Component

class Tutorial : KPaper() {

    override fun load() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
        server.consoleSender.sendMessage(Component.text("Hey, ich wurde geladen!"))
    }

    override fun startup() {
        CommandAPI.onEnable()

        DamageEvent
        Timer
        FirstCommand()
        TimerCommand()
        MobsToBlocks()
    }


    }

    override fun shutdown() {
        CommandAPI.onDisable()
        server.consoleSender.sendMessage(Component.text("Ich wurde deaktiviert :("))
    }
}
