package io.github.black_Kittys22.modcraft


import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import dev.jorel.commandapi.kotlindsl.*

class HatSystem {

    fun registerCommands() {
        commandTree("hat") {
            withPermission("modcraft.hatsystem.use")
            withAliases("hut", "helm")

            anyExecutor { sender, _ ->
                if (sender !is Player) {
                    sender.sendMessage(Component.text("Nur Spieler können diesen Befehl verwenden!", NamedTextColor.RED))
                    return@anyExecutor
                }

                val itemInHand = sender.inventory.itemInMainHand

                if (itemInHand.type == Material.AIR) {
                    sender.sendMessage(Component.text("Du musst einen Block in der Hand halten!", NamedTextColor.RED))
                    return@anyExecutor
                }

                setHat(sender, itemInHand)
                sender.sendMessage(Component.text("Du hast jetzt einen coolen Hut auf!", NamedTextColor.GREEN))
            }
        }
    }

    private fun setHat(player: Player, hatItem: ItemStack) {
        val inventory = player.inventory
        val helmet = inventory.helmet

        // Speichere den aktuellen Helm, falls vorhanden
        if (helmet != null && helmet.type != Material.AIR) {
            // Versuche, den Helm ins Inventar zu legen
            if (inventory.firstEmpty() != -1) {
                inventory.addItem(helmet)
            } else {
                // Kein Platz im Inventar, wirft den Helm auf den Boden
                player.world.dropItem(player.location, helmet)
            }
        }

        // Setze den neuen Hut
        inventory.helmet = hatItem.clone().apply { amount = 1 }

        // Reduziere den Item-Stack in der Hand um 1
        if (hatItem.amount > 1) {
            hatItem.amount = hatItem.amount - 1
        } else {
            player.inventory.setItemInMainHand(ItemStack(Material.AIR))
        }
    }
}