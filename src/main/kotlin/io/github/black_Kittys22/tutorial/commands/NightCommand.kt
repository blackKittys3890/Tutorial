package io.github.black_Kittys22.tutorial.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.World
import org.bukkit.entity.Player

class NightCommand {
    fun register() {
        CommandAPICommand("night")
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                player.world.time = 13000 // Mitternacht
                player.sendMessage("§aEs ist jetzt Nacht!")
            })
            .register()
    }
}