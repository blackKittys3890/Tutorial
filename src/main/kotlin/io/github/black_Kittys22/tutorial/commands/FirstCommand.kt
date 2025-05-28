package io.github.black_Kittys22.tutorial.commands

import de.miraculixx.kpaper.extensions.bukkit.cmp
import dev.jorel.commandapi.StringTooltip
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.LocationType
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.Location
import org.bukkit.Material
import kotlin.jvm.optionals.getOrNull
import org.bukkit.entity.Player


class FirstCommand {

    private val someList = listOf("a", "b", "c") // Weil Listen immer nice sind 😎

    val command = commandTree("test") {

        // Goldify - Verwandle deinen Block in pures Gold, Midas approved ✨
        literalArgument("goldify") {
            locationArgument("block", LocationType.BLOCK_POSITION, true) {
                playerExecutor { player, args ->
                    val loc = args.getOptional(0).getOrNull() as? Location
                    if (loc == null) {
                        player.sendMessage(cmp("🚫 Du hast keine Position angegeben! Komm schon, sag mir wo!"))
                        return@playerExecutor
                    }
                    loc.block.type = Material.GOLD_BLOCK
                    player.sendMessage(cmp("✨ Boom! Dein Block ist jetzt Gold, König!"))
                }
            }
        }

        // Hey Command - Simpel, freundlich, direkt: Hey! 👋
        literalArgument("hey") {
            playerExecutor { player, _ ->
                player.sendMessage(cmp("Hey, was geht ab?"))
            }
        }

        // Select - Wähle deine Option, aber mach’s schlau! 🎯
        literalArgument("select") {
            stringArgument("selection") {
                replaceSuggestions(
                    ArgumentSuggestions.stringsWithTooltips(
                        StringTooltip.ofString("test", "Hey, ich bin ein cooler Tooltip!"),
                        StringTooltip.ofString("test2", "Der zweite Test, noch cooler!")
                    )
                )
                playerExecutor { player, args ->
                    val selection = args.getUnchecked<String>(0)
                    player.sendMessage(cmp("Du hast '$selection' ausgewählt! Gute Wahl! 🚀"))
                }
            }
        }

        // Numbers - Zahlenakrobatik deluxe 🤹‍♂️
        literalArgument("numbers") {

            literalArgument("int") {
                integerArgument("int", 0, 10) {
                    anyExecutor { sender, args ->
                        val num = args.getUnchecked<Int>(0)
                        sender.sendMessage(cmp("Integer erkannt: $num - Nice!"))
                    }
                }
            }

            literalArgument("double") {
                doubleArgument("double", -1.0, 5.5) {
                    anyExecutor { sender, args ->
                        val num = args.getUnchecked<Double>(0)
                        sender.sendMessage(cmp("Double-Precision-Zahl erhalten: $num. Cool, oder?"))
                    }
                }
            }
        }

        // Text - Schreib’s, sag’s, mach’s! 📝
        literalArgument("text") {

            literalArgument("string") {
                stringArgument("simple-text") {
                    anyExecutor { sender, args ->
                        val text = args.getUnchecked<String>(0)
                        sender.sendMessage(cmp("Du hast eingegeben: $text"))
                    }
                }
            }

            literalArgument("text") {
                textArgument("special-text") {
                    anyExecutor { sender, args ->
                        val text = args.getUnchecked<String>(0)
                        sender.sendMessage(cmp("Special Text: $text"))
                    }
                }
            }

            literalArgument("greedy") {
                greedyStringArgument("infinit-text") {
                    anyExecutor { sender, args ->
                        val text = args.getUnchecked<String>(0)
                        sender.sendMessage(cmp("Unendlich viel Text empfangen: $text"))
                    }
                }
            }
        }

        // Selector - Wähle Spieler oder Entities wie ein Boss! 🎮
        literalArgument("selector") {

            literalArgument("manyEntities") {
                entitySelectorArgumentManyEntities("many-entities") {
                    anyExecutor { sender, args ->
                        sender.sendMessage(cmp("Viele Entities ausgewählt, Respekt!"))
                    }
                }
            }

            literalArgument("onePlayer") {
                entitySelectorArgumentOnePlayer("one-player") {
                    anyExecutor { sender, args ->
                        val player = args.getUnchecked<org.bukkit.entity.Player>(0) // Spieler-Argument holen
                        if (sender is org.bukkit.entity.Player) {
                            sender.sendMessage(cmp("Spieler ausgewählt: ${player}"))
                        } else {
                            sender.sendMessage("Spieler ausgewählt: ${player}") // cmp ist für Player, für Konsole einfach so
                        }
                    }
                }
            }


            // Types - Spawn mal schnell coole Items, Sounds und Entities! 🎉
            literalArgument("types") {

                literalArgument("itemStack") {
                    itemStackArgument("item") {
                        anyExecutor { sender, args ->
                            sender.sendMessage(cmp("Item Stack empfangen und bereit zum Spawn!"))
                        }
                    }
                }

                literalArgument("entityType") {
                    entityTypeArgument("entity") {
                        anyExecutor { sender, args ->
                            sender.sendMessage(cmp("Entity Type wurde gesetzt. Mal schauen, was passiert!"))
                        }
                    }
                }

                literalArgument("sound") {
                    soundArgument("sound") {
                        anyExecutor { sender, args ->
                            sender.sendMessage(cmp("Sound gewählt - Lass die Party starten! 🔊"))
                        }
                    }
                }
            }
        }
    }
}
