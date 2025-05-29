package io.github.black_Kittys22.tutorial

import de.miraculixx.kpaper.extensions.broadcast
import de.miraculixx.kpaper.extensions.bukkit.cmp
import de.miraculixx.kpaper.extensions.bukkit.plus
import de.miraculixx.kpaper.extensions.onlinePlayers
import de.miraculixx.kpaper.runnables.task
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.entity.Player
import javax.naming.Name

class CustomTablist : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.player
        val header = Component.text()
            .append(Component.text(""))
            .append(Component.text("Willkommen auf diesem Server!\n").color(NamedTextColor.AQUA))
            .append(cmp("Serverstatus: ").color(NamedTextColor.RED) + cmp("Online\n").color(NamedTextColor.GREEN))
            .append(cmp("Online Player's: ").color(NamedTextColor.DARK_AQUA) + cmp("${onlinePlayers.size}\n").color(NamedTextColor.LIGHT_PURPLE))
        val footer = Component.text()
            .append(Component.text("Viel Spaß, ${player.name}!\n").color(NamedTextColor.GRAY))
            .append(Component.text("ᴘʟᴜɢɪɴ ʙʏ ʙʟᴀᴄᴋ_ᴋɪᴛᴛʏꜱ").color(NamedTextColor.DARK_GRAY))
            .append(Component.text(""))
        player.sendPlayerListHeaderAndFooter(header, footer)
    }
}
