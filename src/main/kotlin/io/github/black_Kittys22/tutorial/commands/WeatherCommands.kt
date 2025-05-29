package io.github.black_Kittys22.tutorial.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.World
import org.bukkit.entity.Player

class WeatherCommands {
    fun register() {
        // /rain - Regenwetter
        CommandAPICommand("rain")
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                player.world.setStorm(true)
                player.world.weatherDuration = 12000 // 10 Minuten Regen
                player.sendMessage("§aEs regnet jetzt!")
            })
            .register()

        // /sun (oder /clear) - Schönes Wetter
        CommandAPICommand("sun")
            .withAliases("clear") // Optional: /clear als Alias
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                player.world.setStorm(false)
                player.world.clearWeatherDuration = 12000 // 10 Minuten schön
                player.sendMessage("§aDie Sonne scheint wieder!")
            })
            .register()

        CommandAPICommand("thunder")
            .withAliases("storm") // Optionaler Alias
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                player.world.setStorm(true)
                player.world.setThundering(true) // Wichtig für Gewitter!
                player.world.weatherDuration = 20 * 60 * 10 // 10 Minuten (in Ticks)
                player.sendMessage("§6Ein Gewitter bricht los!")
            })
            .register()

    }
}