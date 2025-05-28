package io.github.black_Kittys22.tutorial

import de.miraculixx.kpaper.main.KPaper
import org.bukkit.entity.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class CustomTablist {
    fun setCustomTablist(player: Player) {
        val header = Component.text("Wilkommen auf diesem Server!").color(NamedTextColor.AQUA)
        val footer = Component.text("Viel Spaß, ${player.name}!").color(NamedTextColor.GOLD)
        player.sendPlayerListHeaderAndFooter(header, footer)

    }
}