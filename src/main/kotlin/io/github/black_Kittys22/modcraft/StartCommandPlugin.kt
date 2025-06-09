package io.github.black_Kittys22.modcraft

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class StartCommandPlugin(private val plugin: JavaPlugin) {

    private var startExecuted = false

    fun registerCommands() {
        CommandAPICommand("start")
            .withPermission("modcraft.admin.start")
            .executes(CommandExecutor { sender, _ ->
                if (startExecuted) {
                    sender.sendMessage(Component.text("Der Start-Befehl wurde bereits ausgeführt!", NamedTextColor.RED))
                    return@CommandExecutor
                }
                startExecuted = true

                // Spieler heilen und vorbereiten
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "deop @a")
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect give @a minecraft:darkness 1 255 true")
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect give @a minecraft:instant_health 1 255 true")
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "clear @a")
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give @a bread 5")
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players set @a Tode 0")
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect give @a minecraft:resistance 99999 255 true")

                // Titel: Starte den Server / Alle Spieler wurden geheilt.
                Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    """title @a title {"text":"Starte den Server","bold":true,"color":"gold"}"""
                )
                Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    """title @a subtitle {"text":"Alle Spieler wurden geheilt.","italic":true,"color":"green"}"""
                )
                Bukkit.dispatchCommand(
                    Bukkit.getConsoleSender(),
                    "title @a times 20 60 20"
                )

                // Nach 3 Sekunden: Modcraft-Titel
                object : BukkitRunnable() {
                    override fun run() {
                        Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            """title @a title {"text":"§l§6Modcraft","bold":true,"color":"gold"}"""
                        )
                        Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            """title @a subtitle {"text":"Das Abenteuer beginnt!","italic":true,"color":"gray"}"""
                        )
                        Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            "title @a times 20 80 20"
                        )

                        Bukkit.dispatchCommand(
                            Bukkit.getConsoleSender(),
                            "worldborder set 60000 25"
                        )
                    }
                }.runTaskLater(plugin, 60L)

                sender.sendMessage(Component.text("Start-Befehle wurden ausgeführt!", NamedTextColor.GREEN))
            })
            .register()
    }
}