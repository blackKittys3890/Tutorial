package io.github.black_Kittys22.tutorial.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.World
import org.bukkit.entity.Player

class DayCommand {
    fun register() {
        CommandAPICommand("day")
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                val world: World = player.world
                world.time = 1000 // Mittag (Tag)
                world.clearWeatherDuration = 12000 // Gutes Wetter für 10 Minuten
                player.sendMessage("§aEs ist jetzt Tag und das Wetter wurde schön!")
            })
            .register()
    }
}