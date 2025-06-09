package io.github.black_Kittys22.modcraft

import dev.jorel.commandapi.kotlindsl.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import java.util.logging.Level

class FriendSystem {
    private val friendRequests = mutableMapOf<UUID, MutableSet<UUID>>()
    private val friends = mutableMapOf<UUID, MutableSet<UUID>>()
    private lateinit var plugin: JavaPlugin

    // Extension function for easy messaging
    private fun Player.msg(message: String, color: NamedTextColor = NamedTextColor.GRAY) {
        this.sendMessage(Component.text(message).color(color))
    }

    fun onEnable(plugin: JavaPlugin) {
        this.plugin = plugin
        plugin.logger.info("FriendSystem activated!")
        ensureDataFolderExists()
        registerCommands()
        loadData()
    }

    fun onDisable() {
        saveData()
    }

    private fun ensureDataFolderExists() {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
        }
    }

    private fun registerCommands() {
        commandTree("friend") {
            withAliases("freund", "friends")
            withPermission("modcraft.friendsystem.use")

            literalArgument("add") {
                playerArgument("target") {
                    anyExecutor { sender, args ->
                        val target = args["target"] as Player
                        sendFriendRequest(sender as Player, target)
                    }
                }
            }

            literalArgument("accept") {
                playerArgument("target") {
                    anyExecutor { sender, args ->
                        val target = args["target"] as Player
                        acceptFriendRequest(sender as Player, target)
                    }
                }
            }

            literalArgument("deny") {
                playerArgument("target") {
                    anyExecutor { sender, args ->
                        val target = args["target"] as Player
                        denyFriendRequest(sender as Player, target)
                    }
                }
            }

            literalArgument("list") {
                anyExecutor { sender, _ ->
                    listFriends(sender as Player)
                }
            }

            literalArgument("remove") {
                playerArgument("target") {
                    anyExecutor { sender, args ->
                        val target = args["target"] as Player
                        removeFriend(sender as Player, target)
                    }
                }
            }
        }
    }

    private fun sendFriendRequest(sender: Player, target: Player) {
        if (sender.uniqueId == target.uniqueId) {
            sender.msg("Wie selbstverliebt bist du denn...", NamedTextColor.RED)
            return
        }

        if (areFriends(sender, target)) {
            sender.msg("Du bist schon mit ${target.name} befreundet", NamedTextColor.GREEN)
            return
        }

        val requests = friendRequests.getOrPut(target.uniqueId) { mutableSetOf() }
        if (requests.contains(sender.uniqueId)) {
            sender.msg("Du hast ${target.name} schon dein Freundebuch gegeben!", NamedTextColor.RED)
            return
        }

        requests.add(sender.uniqueId)
        sender.msg("Du hast ${target.name} dein Freundebuch gegeben!", NamedTextColor.GREEN)
        target.msg("${sender.name} hat dir dein Freundebuch gegeben", NamedTextColor.GOLD)
        target.msg("/friend accept ${sender.name} - Reinschreiben", NamedTextColor.GREEN)
        target.msg("/friend deny ${sender.name} - Zurückgeben", NamedTextColor.RED)
    }

    private fun acceptFriendRequest(accepter: Player, sender: Player) {
        val requests = friendRequests[accepter.uniqueId] ?: run {
            accepter.msg("${sender.name} hat dir nicht sein Freundebuch gegeben!", NamedTextColor.RED)
            return
        }

        if (!requests.contains(sender.uniqueId)) {
            accepter.msg("${sender.name} hat dir nicht sein Freundebuch gegeben!", NamedTextColor.RED)
            return
        }

        requests.remove(sender.uniqueId)
        addFriends(accepter, sender)
        accepter.msg("Du hast in das Freundebuch ${sender.name} geschrieben!", NamedTextColor.GREEN)
        sender.msg("${accepter.name} hat in dein Freundebuch geschrieben!", NamedTextColor.GREEN)
    }

    private fun denyFriendRequest(denier: Player, sender: Player) {
        val requests = friendRequests[denier.uniqueId] ?: run {
            denier.msg("${sender.name} hat dir nicht sein Freundebuch gegeben!", NamedTextColor.RED)
            return
        }

        if (!requests.contains(sender.uniqueId)) {
            denier.msg("${sender.name} hat dir nicht sein Freundebuch gegeben!", NamedTextColor.RED)
            return
        }

        requests.remove(sender.uniqueId)
        denier.msg("Du hast ${sender.name} sein Freundebuch zurückgegeben!", NamedTextColor.GREEN)
        sender.msg("${denier.name} hat nicht in dein Freundebuch geschrieben!", NamedTextColor.RED)
    }

    private fun addFriends(player1: Player, player2: Player) {
        val friends1 = friends.getOrPut(player1.uniqueId) { mutableSetOf() }
        val friends2 = friends.getOrPut(player2.uniqueId) { mutableSetOf() }
        friends1.add(player2.uniqueId)
        friends2.add(player1.uniqueId)
    }

    private fun areFriends(player1: Player, player2: Player): Boolean {
        return friends[player1.uniqueId]?.contains(player2.uniqueId) ?: false
    }

    private fun listFriends(player: Player) {
        val friendList = friends[player.uniqueId] ?: run {
            player.msg("Du hast keine Freunde :( Such dir welche", NamedTextColor.RED)
            return
        }

        val online = friendList.filter { Bukkit.getPlayer(it)?.isOnline == true }
        val offline = friendList - online.toSet()

        player.msg("=== Freunde ===", NamedTextColor.GOLD)
        player.msg("Online (${online.size}):", NamedTextColor.GREEN)
        online.forEach {
            player.msg("- ${Bukkit.getPlayer(it)?.name}", NamedTextColor.GREEN)
        }
        player.msg("Offline (${offline.size}):", NamedTextColor.GRAY)
        offline.forEach {
            player.msg("- ${Bukkit.getOfflinePlayer(it).name}", NamedTextColor.GRAY)
        }
    }

    private fun removeFriend(player: Player, target: Player) {
        val playerFriends = friends[player.uniqueId] ?: run {
            player.msg("${target.name} steht nicht in deinem Freundebuch!", NamedTextColor.RED)
            return
        }

        if (!playerFriends.contains(target.uniqueId)) {
            player.msg("${target.name} steht nicht in deinem Freundebuch!", NamedTextColor.RED)
            return
        }

        playerFriends.remove(target.uniqueId)
        friends[target.uniqueId]?.remove(player.uniqueId)
        player.msg("${target.name} wurde aus deinem Freundebuch rausgelöscht!", NamedTextColor.GREEN)
        if (target.isOnline) {
            target.msg("${player.name} hat deine Seite aus dem Freudenbuch rausgerissen!", NamedTextColor.RED)
        }
    }

    private fun loadData() {
        val dataFile = File(plugin.dataFolder, "friends.yml")
        if (!dataFile.exists()) return

        try {
            val yaml = YamlConfiguration.loadConfiguration(dataFile)

            // Load friends
            yaml.getConfigurationSection("friends")?.let { section ->
                section.getKeys(false).forEach { playerId ->
                    val uuid = UUID.fromString(playerId)
                    val friendIds = section.getStringList(playerId).map { UUID.fromString(it) }.toMutableSet()
                    friends[uuid] = friendIds
                }
            }

            // Load friend requests
            yaml.getConfigurationSection("requests")?.let { section ->
                section.getKeys(false).forEach { playerId ->
                    val uuid = UUID.fromString(playerId)
                    val requestIds = section.getStringList(playerId).map { UUID.fromString(it) }.toMutableSet()
                    friendRequests[uuid] = requestIds
                }
            }

            plugin.logger.info("Friend data loaded successfully!")
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Failed to load friend data", e)
        }
    }

    private fun saveData() {
        val dataFile = File(plugin.dataFolder, "friends.yml")
        val yaml = YamlConfiguration()

        try {
            // Save friends
            friends.forEach { (player, friendList) ->
                yaml.set("friends.$player", friendList.map { it.toString() })
            }

            // Save friend requests
            friendRequests.forEach { (player, requestList) ->
                yaml.set("requests.$player", requestList.map { it.toString() })
            }

            yaml.save(dataFile)
            plugin.logger.info("Friend data saved successfully!")
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Failed to save friend data", e)
        }
    }
}