package io.github.black_Kittys22.modcraft

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.entity.Player


class CustomTablist : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player: Player = event.player
        val header = Component.text()
            .append(Component.text("\n"))
            .append(Component.text("\n"))
            .append(Component.text("\n"))
            .append(Component.text("\uE000\n").color(NamedTextColor.AQUA))
        val footer = Component.text()
            .append(Component.text("Viel Spaß, ${player.name}!\n").color(NamedTextColor.GRAY))
            .append(Component.text("ᴘʟᴜɢɪɴ ʙʏ ʙʟᴀᴄᴋ_ᴋɪᴛᴛʏꜱ").color(NamedTextColor.DARK_GRAY))
            .append(Component.text(""))
        player.sendPlayerListHeaderAndFooter(header, footer)
    }
}
