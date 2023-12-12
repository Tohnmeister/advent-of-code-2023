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

    fun part1(lines: List<String>): Int {

        val pipes = lines.map { line -> line.map { pipeMap[it]!! }}
        val startPos = pipes.asSequence().withIndex().flatMap { (y, line) -> line.withIndex().map { (x, pipe) -> Pair(pipe, Pair(x, y)) }}.find { it.first.start }!!.second
        var found = false
        var nrSteps = 0
        var currentPos = startPos
        var comingFrom: Direction? = null
        while (!found) {
            val top = NextPos(Top, currentPos.first, currentPos.second - 1)
            val right = NextPos(Right, currentPos.first + 1, currentPos.second)
            val bottom = NextPos(Bottom, currentPos.first, currentPos.second + 1)
            val left = NextPos(Left, currentPos.first - 1, currentPos.second)
            val currentPipe = pipes[currentPos.second][currentPos.first]

            val possibleNextPositions = sequenceOf(top, right, bottom, left)
                .filter { nextPos -> nextPos.direction != comingFrom } // The pipe where we came from should not be allowed
                .filter { nextPos -> nextPos.x >= 0 && nextPos.x < lines[0].length && nextPos.y >= 0 && nextPos.y < lines.size } // The pipe outside the field should not be allowed
                .filter { currentPipe.connectsTo(it.direction) } // The current pipe should connect to the next pipe
                .toList() // .find { nextPos -> pipes[nextPos.x][nextPos.y].connectsTo(nextPos.direction.opposite()) } // The next pipe should connect to the current pipe


            // From those pipes, find the pipe that has a connection to the current pipe
            val nextPos = possibleNextPositions.find {
                pipes[it.y][it.x].connectsTo(it.direction.opposite())
            }!!
            currentPos = Pair(nextPos.x, nextPos.y)
            comingFrom = nextPos.direction.opposite()

            ++nrSteps

            if (pipes[nextPos.y][nextPos.x].start) {
                found = true
            }
        }

        return nrSteps / 2
    }

    val lines = readLines("Day10.txt")

    part1(lines).println()
}
