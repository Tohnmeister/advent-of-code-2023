package day20

import println
import readLines
import java.util.*

data class Module(val type: Char, val destinations: List<String>)
data class InputSignal(val source: String, var isHigh: Boolean)

class ModuleNetwork(private val moduleMap: Map<String, Module>, private val verbose: Boolean = true) {
    val signalQueue = LinkedList<() -> Unit>()
    val flipflopStates = moduleMap.filter { it.value.type == '%' }.mapValues { false }.toMutableMap()
    val conjunctionInputSignalMap = moduleMap.flatMap { (sourceName, module) -> module.destinations.map { destinationName -> Pair(destinationName, sourceName) }}.groupBy({ it.first }, { InputSignal(it.second, false) })
    var lowCount = 0
        private set
    var highCount = 0
        private set
    var rxWasSignalled = false

    private fun log(source: String, destination: String, isHigh: Boolean) {
        if (verbose) {
            val pulseStr = if (isHigh) "high" else "low"
            println("$source -${pulseStr}-> $destination")
        }
    }

    fun pushButton() {
        log("button", "broadcaster", false)
        ++lowCount

        for (destination in moduleMap["broadcaster"]!!.destinations) {
            sendSignal("broadcaster", destination, false)
        }
        while (!signalQueue.isEmpty()) {
            val signalQueueCopy = LinkedList(signalQueue)
            signalQueue.clear()
            signalQueueCopy.forEach { it() }
        }
    }

    private fun sendSignal(source: String, destination: String, isHigh: Boolean) {
        log(source, destination, isHigh)

        if (!isHigh && destination == "rx") {
            rxWasSignalled = true
        }

        if (isHigh) {
            ++highCount
        } else {
            ++lowCount
        }

        if (moduleMap.contains(destination)) {
            val destinationModule = moduleMap[destination]!!
            when (destinationModule.type) {
                '%' -> {
                    // FlipFlop
                    if (!isHigh) {
                        val newSignal = !flipflopStates[destination]!!
                        flipflopStates[destination] = newSignal
                        for (subDestination in destinationModule.destinations) {
                            signalQueue.add {
                                sendSignal(destination, subDestination, newSignal)
                            }
                        }
                    }
                }
                '&' -> {
                    // Conjunction
                    val inputSignals = conjunctionInputSignalMap[destination]!!
                    inputSignals.first { it.source == source }.isHigh = isHigh

                    val newSignal = !inputSignals.all { it.isHigh }
                    for (subDestination in destinationModule.destinations) {
                        signalQueue.add {
                            sendSignal(destination, subDestination, newSignal)
                        }
                    }
                }
            }
        }
    }
}

fun main() {
    fun part1(lines: List<String>): Int {
        val moduleMap: Map<String, Module> = lines.associate {
            val segments = it.split(" -> ")
            val source = segments[0]
            val (sourceType, sourceLabel) = if (source == "broadcaster") {
                Pair('b', source)
            } else {
                Pair(source[0], source.drop(1))
            }
            val destinations = segments[1].split(',').map(String::trim)
            Pair(sourceLabel, Module(sourceType, destinations))
        }

        val moduleNetwork = ModuleNetwork(moduleMap, false)
        repeat(1000) {
            moduleNetwork.pushButton()
        }

        return moduleNetwork.lowCount * moduleNetwork.highCount
    }

    fun part2(lines: List<String>): Int {
        val moduleMap: Map<String, Module> = lines.associate {
            val segments = it.split(" -> ")
            val source = segments[0]
            val (sourceType, sourceLabel) = if (source == "broadcaster") {
                Pair('b', source)
            } else {
                Pair(source[0], source.drop(1))
            }
            val destinations = segments[1].split(',').map(String::trim)
            Pair(sourceLabel, Module(sourceType, destinations))
        }

        val moduleNetwork = ModuleNetwork(moduleMap, false)
        var count = 0
        while (!moduleNetwork.rxWasSignalled) {
            moduleNetwork.pushButton()
            ++count
        }

        return count
    }

    val lines = readLines("Day20.txt")

    val part1 = part1(lines)
    part1.println()
    check(part1 == 791120136)

    val part2 = part2(lines)
    part2.println()
}
