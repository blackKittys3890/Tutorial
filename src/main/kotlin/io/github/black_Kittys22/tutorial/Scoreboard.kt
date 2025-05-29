package io.github.black_Kittys22.tutorial

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard

class Scoreboard : Listener {

    companion object {
        fun clearScoreboard(player: Player) {
            player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        setScoreboard(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        updateAllScoreboards()
    }

    @EventHandler
    fun onWorldChange(event: PlayerChangedWorldEvent) {
        setScoreboard(event.player)
    }

    fun updateAllScoreboards() {
        Bukkit.getOnlinePlayers().forEach { setScoreboard(it) }
    }

    fun setScoreboard(player: Player) {
        val scoreboard = Bukkit.getScoreboardManager().newScoreboard
        val objective = scoreboard.registerNewObjective("main", "dummy", "§6§l✧ Black_Kittys22 ✧")
        objective.displaySlot = DisplaySlot.SIDEBAR

        val entries = listOf(
            "§7▶ Spieler: §b${Bukkit.getOnlinePlayers().size}" to 3,
            "§7 Welt: §a${player.world.name}" to 2,
            "§8---------------------" to 1,
            "§eᴘʟᴜɢɪɴ ʙʏ ʙʟᴀᴄᴋ_ᴋɪᴛᴛʏꜱ" to 0
        )

        entries.forEach { (text, score) ->
            objective.getScore(text).score = score
        }

        player.scoreboard = scoreboard
    }
}