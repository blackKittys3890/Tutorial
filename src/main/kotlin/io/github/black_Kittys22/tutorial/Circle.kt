package io.github.black_Kittys22.tutorial

import de.miraculixx.kpaper.extensions.bukkit.cmp
import dev.jorel.commandapi.arguments.LocationType
import dev.jorel.commandapi.kotlindsl.blockStateArgument
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.locationArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import org.bukkit.Location

class Circle {
    val command = commandTree("circle") {
        locationArgument("location", LocationType.BLOCK_POSITION, true) {
            integerArgument("radius") {
                blockStateArgument("material") {
                    playerExecutor { player, args ->
                        val argCount = args.args.size
                        val hasLocation = argCount == 3

                        val loc = if (hasLocation) args[0] as Location else player.location
                        val radius = if (hasLocation) args[1] as Int else args[0] as Int
                        val blockData = if (hasLocation) args[2] as org.bukkit.block.data.BlockData else args[1] as org.bukkit.block.data.BlockData
                        val material = blockData.material

                        val world = loc.world ?: run {
                            player.sendMessage(cmp("Ungültige Welt!"))
                            return@playerExecutor
                        }

                        for (x in -radius..radius) {
                            for (y in -radius..radius) {
                                for (z in -radius..radius) {
                                    val currentLoc = loc.clone().add(x.toDouble(), y.toDouble(), z.toDouble())
                                    if (currentLoc.distance(loc) <= radius) {
                                        world.getBlockAt(currentLoc).type = material
                                    }
                                }
                            }
                        }

                        player.sendMessage(cmp("Alle Blöcke im Radius von $radius wurden zu $material geändert!"))
                    }
                }

            }
        }
    }
}
