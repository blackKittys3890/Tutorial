package io.github.black_Kittys22.tutorial.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.GreedyStringArgument
import org.bukkit.GameMode
import org.bukkit.entity.Player
import dev.jorel.commandapi.executors.PlayerCommandExecutor

class GamemodeCommand {

    fun register() {
        CommandAPICommand("gm")
            .withArguments(GreedyStringArgument("mode"))
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val mode = when ((args[0] as String).lowercase()) {
                    "0", "s", "survival" -> GameMode.SURVIVAL
                    "1", "c", "creative" -> GameMode.CREATIVE
                    "2", "a", "adventure" -> GameMode.ADVENTURE
                    "3", "sp", "spectator" -> GameMode.SPECTATOR
                    else -> null
                }

                if (mode == null) {
                    player.sendMessage("§cUngültiger Gamemode! Nutze: 0/s/survival, 1/c/creative, 2/a/adventure, 3/sp/spectator")
                    return@PlayerCommandExecutor
                }

                player.gameMode = mode
                player.sendMessage("§aDein Gamemode wurde auf ${mode.name.lowercase().replaceFirstChar { it.uppercase() }} gesetzt.")
            })
            .register()
    }
}