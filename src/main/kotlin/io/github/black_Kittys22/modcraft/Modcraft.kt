package io.github.black_Kittys22.modcraft

import io.github.black_Kittys22.modcraft.StartCommandPlugin
import de.miraculixx.kpaper.main.KPaper
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import kotlinx.coroutines.SupervisorJob
import net.kyori.adventure.text.Component
import java.io.File

class Modcraft : KPaper() {
    private lateinit var friendSystem: FriendSystem
    private lateinit var hatSystem: HatSystem
    private lateinit var headDropSystem: HeadDropSystem  // Neue Instanz hinzugefügt
    private lateinit var teamChatSystem: TeamChatSystem
    private lateinit var sudoForcePlugin: SudoForcePlugin
    private lateinit var startCommandPlugin: StartCommandPlugin

    override fun load() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).silentLogs(true))
        server.consoleSender.sendMessage(Component.text("Modcraft Plugin loaded!"))
    }

    override fun startup() {
        CommandAPI.onEnable()

        // Register events and features
        server.pluginManager.registerEvents(CustomTablist(), this)
        server.pluginManager.registerEvents(HeadDropSystem(), this)


        // Initialize and enable systems
        friendSystem = FriendSystem().also { it.onEnable(this) }
        hatSystem = HatSystem().also { it.registerCommands() }
        headDropSystem = HeadDropSystem()  // HeadDropSystem initialisieren
        teamChatSystem = TeamChatSystem().also { it.registerCommands()}
        sudoForcePlugin = SudoForcePlugin().also { it.registerCommands()}
        startCommandPlugin = StartCommandPlugin(this).also { it.registerCommands() }

        // Create commands.txt
        val pluginFolder = this.dataFolder
        if (!pluginFolder.exists()) pluginFolder.mkdirs()

        val file = File(pluginFolder, "commands.txt")
        file.writeText("""
            # Modcraft Plugin Commands
            
            # Friend System:
            /friend add <player> - Send friend request
            /friend accept <player> - Accept request
            /friend deny <player> - Deny request
            /friend list - Show friends
            /friend remove <player> - Remove friend
            
            # Hat System:
            /hat - Set the block in your hand as a hat
            /hut - Alias for /hat (German)
            /helm - Alias for /hat
            
            # Head Drop Feature:
            - Wenn ein Spieler von einem anderen Spieler getötet wird, droppt sein Kopf
        """.trimIndent())

        server.consoleSender.sendMessage(Component.text("Plugin fully initialized!"))
    }

    override fun shutdown() {
        friendSystem.onDisable()
        CommandAPI.onDisable()
        server.consoleSender.sendMessage(Component.text("Plugin disabled"))
    }
}