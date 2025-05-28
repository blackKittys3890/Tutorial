package io.github.black_Kittys22.tutorial.commands

import de.miraculixx.kpaper.extensions.bukkit.cmp
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.textArgument
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import kotlin.time.Duration

class TimerCommand {
    val timerCommand = commandTree("timer") {
        literalArgument("pause") {
            anyExecutor { _, _ ->
                Timer.paused = true
            }
        }

        literalArgument("resume") {
            anyExecutor { _, _ ->
                Timer.paused = false
            }
        }

        literalArgument("settime") {
            textArgument("time") {
                anyExecutor { sender, args ->
                    val timeString = args[0] as String

                    val time = try{
                        Duration.parse(timeString)
                    } catch (_: IllegalArgumentException) {
                        sender.sendMessage(cmp("Du hast keine gültige Zeit angegeben", NamedTextColor.RED))
                        return@anyExecutor
                    }

                    Timer.setTime(time)

                }
            }
        }

    }
}

class ColorUtils {
    fun convert(text: String): Component {
        return MiniMessage.miniMessage().deserialize(text)
    }

    fun convert(text: Component): String {
        return MiniMessage.miniMessage().serialize(text)
    }

    fun convertChatMessage(text: Component): Component {
        return convert(strip(text))
    }

    fun strip(text: String): String {
        return PlainTextComponentSerializer.plainText().serialize(convert(text))
    }

    fun strip(text: Component): String {
        return PlainTextComponentSerializer.plainText().serialize(text)
    }

    fun stripChatMessage(text: Component): String {
        return strip(strip(text))
    }

    fun handleLore(text: String): List<Component> {
        val lore = mutableListOf<Component>()
        val mm = MiniMessage.miniMessage()

        // Variable to store the last color or formatting tag detected
        var lastFormat: String? = null

        // List of known color and style tags from MiniMessage
        val knownTags = listOf(
            "<red>", "<green>", "<blue>", "<yellow>", "<gold>", "<white>", "<black>", "<gray>",
            "<aqua>", "<dark_red>", "<dark_green>", "<dark_blue>", "<dark_aqua>", "<dark_purple>",
            "<dark_gray>", "<light_purple>", "<bold>", "<italic>", "<underlined>",
            "<strikethrough>", "<obfuscated>"
        )

        val processLines: (String) -> Unit = { text ->
            val lines = text.split(Regex("<newline>|<br>|\\n"))
            for (line in lines) {
                if (line.trim().isEmpty()) continue

                val formattedLine = if (lastFormat != null && !knownTags.any { line.contains(it) }) {
                    "$lastFormat$line"
                } else {
                    line
                }

                val component = mm.deserialize(formattedLine)
                lore.add(component)

                knownTags.firstOrNull { tag -> formattedLine.contains(tag) }?.let {
                    lastFormat = it
                }
            }
        }

        processLines(text)
        return lore
    }


    fun handleLore(text: Component): List<Component> {
        return handleLore(convert(text))
    }
}