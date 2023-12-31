package day10;

import day10.Direction.*
import println
import readLines

enum class Direction {
    Top, Right, Bottom, Left;

    fun opposite(): Direction {
        return when (this) {
            Top -> Bottom
            Right -> Left
            Bottom -> Top
            Left -> Right
        }
    }
}

class NextPos(val direction: Direction, val x: Int, val y: Int)

fun main() {

    class Pipe(vararg val directions: Direction, val start: Boolean = false) {
        fun connectsTo(direction: Direction): Boolean = directions.contains(direction)
    }

    val pipeMap = mapOf(
        '|' to Pipe(Top, Bottom),
        '-' to Pipe(Left, Right),
        'L' to Pipe(Top, Right),
        'J' to Pipe(Top, Left),
        '7' to Pipe(Bottom, Left),
        'F' to Pipe(Bottom, Right),
        '.' to Pipe(),
        'S' to Pipe(Top, Right, Bottom, Left, start = true)
    )

    fun calculateLoop(lines: List<String>): MutableList<Pair<Int, Int>> {
        val pipes = lines.map { line -> line.map { pipeMap[it]!! } }
        val startPos = pipes.asSequence().withIndex()
            .flatMap { (y, line) -> line.withIndex().map { (x, pipe) -> Pair(pipe, Pair(x, y)) } }
            .find { it.first.start }!!.second
        var found = false
        var currentPos = startPos
        var comingFrom: Direction? = null
        val loop = mutableListOf<Pair<Int, Int>>()
        while (!found) {
            val top = NextPos(Top, currentPos.first, currentPos.second - 1)
            val right = NextPos(Right, currentPos.first + 1, currentPos.second)
            val bottom = NextPos(Bottom, currentPos.first, currentPos.second + 1)
            val left = NextPos(Left, currentPos.first - 1, currentPos.second)
            val currentPipe = pipes[currentPos.second][currentPos.first]

            val nextPos = sequenceOf(top, right, bottom, left)
                .filter { nextPos -> nextPos.direction != comingFrom } // The pipe where we came from should not be allowed
                .filter { nextPos -> nextPos.x >= 0 && nextPos.x < lines[0].length && nextPos.y >= 0 && nextPos.y < lines.size } // The pipe outside the field should not be allowed
                .filter { currentPipe.connectsTo(it.direction) } // The current pipe should connect to the next pipe
                .find { nextPos -> pipes[nextPos.y][nextPos.x].connectsTo(nextPos.direction.opposite()) }!! // The next pipe should connect to the current pipe

            currentPos = Pair(nextPos.x, nextPos.y)
            comingFrom = nextPos.direction.opposite()

            loop.add(currentPos)

            if (pipes[currentPos.second][currentPos.first].start) {
                found = true
            }
        }
        return loop
    }

    fun part1(lines: List<String>): Int {
        val loop = calculateLoop(lines)

        return loop.size / 2
    }

    fun part2(lines: List<String>): Int {
        val loop = calculateLoop(lines)

        fun countHorizontalBreaks(x: Int, y: Int): Int {
            fun rulesOut(char1: Char?, char2: Char): Boolean {
                return char1 == 'F' && char2 == 'J' || char1 == '7' && char2 == 'L'
            }

            var count = 0
            var lastChar: Char? = null
            for (row in 0 until y) {
                val pos = Pair(x, row)
                val currentChar = lines[row][x]

                if (loop.contains(pos)) {
                    when (currentChar) {
                        '-' -> ++count
                        'F', '7' -> {
                            lastChar = currentChar
                        }
                        'J', 'L' -> {
                            if (rulesOut(lastChar, currentChar)) {
                                ++count
                            }
                            lastChar = null
                        }
                    }
                }
            }
            return count
        }

        fun countVerticalBreaks(x: Int, y: Int): Int {
            fun rulesOut(char1: Char?, char2: Char): Boolean {
                return char1 == 'L' && char2 == '7' || char1 == 'F' && char2 == 'J'
            }

            var count = 0
            var lastChar: Char? = null
            for (col in 0 until x) {
                val pos = Pair(col, y)
                val currentChar = lines[y][col]

                if (loop.contains(pos)) {
                    when (currentChar) {
                        '|' -> ++count
                        'L', 'F' -> {
                            lastChar = currentChar
                        }
                        '7', 'J' -> {
                            if (rulesOut(lastChar, currentChar)) {
                                ++count
                            }
                            lastChar = null
                        }
                    }
                }
            }
            return count
        }

        var insideLoopCount = 0

        // Count number of col breaks and row breaks
        // If both are even or both are uneven, we're inside the loop
        // If not, we're outside the loop
        val changedList = lines.map { it.toMutableList() }.toMutableList()
        for ((y, line) in lines.withIndex()) {
            for (x in lines[y].indices) {
                if (!loop.contains(Pair(x, y))) {
                    val nrOfHorizontalBreaks = countHorizontalBreaks(x, y)
                    val nrOfVerticalBreaks = countVerticalBreaks(x, y)

                    if (nrOfVerticalBreaks % 2 == 1 && nrOfHorizontalBreaks % 2 == 1) {
                        changedList[y][x] = 'I'
                        ++insideLoopCount
                    }
                }
            }
        }
        for (line in changedList) {
            println(line.joinToString(""))
        }

        return insideLoopCount
    }

    val lines = readLines("Day10.txt")

    val part1 = part1(lines)
    check(part1 == 7063)
    part1.println()
    part2(lines).println()
}
