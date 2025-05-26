package io.github.black_Kittys22.tutorial

import de.miraculixx.kpaper.event.listen
import de.miraculixx.kpaper.extensions.broadcast
import de.miraculixx.kpaper.extensions.bukkit.cmp
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent

object DamageEvent {
    val onDamage = listen<EntityDamageEvent> {
        val player = it.entity
        if (player !is Player) return@listen

        val damageHearts = it.finalDamage / 2.0

        broadcast(cmp("Jemand hat Schaden bekommen... (%{it.damageHearts})", NamedTextColor.GOLD)

        )

    }
}