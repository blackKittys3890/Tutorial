
package io.github.black_Kittys22.modcraft

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SudoForcePlugin {

    fun registerCommands() {
        CommandAPICommand("sudoforce")
            .withPermission("modcraft.sudoforce")
            .withAliases("forcecmd", "sudo")
            .withArguments(
                PlayerArgument("target"),
                GreedyStringArgument("command")
            )
            .executes(CommandExecutor { sender, args ->
                val target = args.get("target") as? Player ?: run {
                    sender.sendMessage(Component.text("Zielspieler nicht gefunden!", NamedTextColor.RED))
                    return@CommandExecutor
                }

                val rawCommand = args.get("command") as? String ?: run {
                    sender.sendMessage(Component.text("Kein Befehl angegeben!", NamedTextColor.RED))
                    return@CommandExecutor
                }

                val command = if (rawCommand.startsWith("/")) rawCommand.substring(1) else rawCommand
                executeCommand(sender, target, command)
                1
            })
            .register()
    }

    private fun executeCommand(sender: CommandSender, target: Player, command: String) {
        try {
            val result = Bukkit.dispatchCommand(target, command)
            val feedback = Component.text()
                .append(Component.text("[Modcraft-Sudo] ", NamedTextColor.DARK_RED))
                .append(
                    if (result) {
                        Component.text("Befehl '$command' wurde als ${target.name} ausgeführt", NamedTextColor.GRAY)
                    } else {
                        Component.text("Befehl konnte nicht ausgeführt werden!", NamedTextColor.RED)
                    }
                )
                .build()
            sender.sendMessage(feedback)
        } catch (e: Exception) {
            sender.sendMessage(
                Component.text("Fehler: ${e.message}", NamedTextColor.RED)
            )
        }
    }
}