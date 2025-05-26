package io.github.black_Kittys22.tutorial.commands

import de.miraculixx.kpaper.extensions.bukkit.cmp
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument
import dev.jorel.commandapi.kotlindsl.textArgument
import io.github.black_Kittys22.tutorial.Timer
import net.kyori.adventure.text.format.NamedTextColor
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