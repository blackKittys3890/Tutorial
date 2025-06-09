package io.github.black_Kittys22.modcraft

import dev.jorel.commandapi.kotlindsl.*
import dev.jorel.commandapi.arguments.GreedyStringArgument
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import de.miraculixx.kpaper.extensions.bukkit.*
import de.miraculixx.kpaper.extensions.console
import de.miraculixx.kpaper.extensions.onlinePlayers

class TeamChatSystem {

    fun registerCommands() {
        commandAPICommand("mcteamchat") {
            withPermission("modcraft.mcteamchat.use")
            withAliases("mctc", "mctchat", "mcteam", "serverteam", "st", "minecraftserverteamchat")

            // GreedyStringArgument direkt hinzufügen
            withArguments(GreedyStringArgument("message"))

            anyExecutor { sender, args ->
                if (sender !is Player) {
                    sender.sendMessage(Component.text("Nur Spieler können diesen Befehl verwenden!", NamedTextColor.RED))
                    return@anyExecutor
                }

                // Nachricht aus den Argumenten holen
                val message = args[0] as? String ?: run {
                    sender.sendMessage(Component.text("Verwendung: /st <Nachricht>", NamedTextColor.RED))
                    return@anyExecutor
                }

                if (message.isEmpty()) {
                    sender.sendMessage(Component.text("Verwendung: /st <Nachricht>", NamedTextColor.RED))
                    return@anyExecutor
                }

                sendTeamMessage(sender as Player, message)
            }
        }
    }

    private fun sendTeamMessage(player: Player, message: String) {
        if (!player.hasPermission("modcraft.mcteamchat.use")) {
            player.sendMessage(Component.text("Du hast keine Berechtigung für den Team-Chat!", NamedTextColor.RED))
            return
        }

        val componentMessage = Component.empty()
            .append(Component.text("[Server-Team] ", NamedTextColor.DARK_AQUA))
            .append(Component.text(player.name, NamedTextColor.AQUA))
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append(Component.text(message))

        onlinePlayers.filter { it.hasPermission("modcraft.mcteamchat.use") }
            .forEach { it.sendMessage(componentMessage) }

        console.sendMessage("[Team-Chat] ${player.name}: $message")
    }
}