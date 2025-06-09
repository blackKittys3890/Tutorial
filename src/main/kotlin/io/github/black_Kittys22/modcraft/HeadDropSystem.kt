package io.github.black_Kittys22.modcraft

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import java.text.SimpleDateFormat
import java.util.*

class HeadDropSystem : Listener {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        val killer = player.killer

        // Nur wenn der Spieler von einem anderen Spieler getötet wurde
        if (killer != null) {
            val head = createPlayerHead(player, killer)
            player.world.dropItemNaturally(player.location, head)

            killer.sendMessage(
                Component.text("Du hast den Kopf von ${player.name} erhalten!", NamedTextColor.GOLD)
            )
        }
    }

    private fun createPlayerHead(player: Player, killer: Player): ItemStack {
        val head = ItemStack(Material.PLAYER_HEAD)
        val meta = head.itemMeta as SkullMeta
        val currentTime = dateFormat.format(Date())

        meta.displayName(
            Component.text("Kopf von ${player.name}", NamedTextColor.GOLD)
        )

        // Erstelle die Lore mit Killer-Info und Zeitstempel
        val lore = listOf(
            Component.text("Getötet von: ${killer.name}", NamedTextColor.RED),
            Component.text("Datum: $currentTime", NamedTextColor.GRAY),
            Component.text(""),
            Component.text("Ein trauriges Ende...", NamedTextColor.DARK_GRAY)
        )

        meta.lore(lore)
        meta.playerProfile = player.playerProfile

        head.itemMeta = meta
        return head
    }
}