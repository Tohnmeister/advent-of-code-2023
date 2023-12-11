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

    data class Pipe(val start: Boolean = false, val top: Boolean = false, val right: Boolean = false, val bottom: Boolean = false, val left: Boolean = false)

    val pipeMap = mapOf(
        '|' to Pipe(top = true, bottom = true),
        '-' to Pipe(left = true, right = true),
        'L' to Pipe(top = true, right = true),
        'J' to Pipe(top = true, left = true),
        '7' to Pipe(bottom = true, left = true),
        'F' to Pipe(bottom = true, right = true),
        '.' to Pipe(),
        'S' to Pipe(start = true),
    )

    fun part1(lines: List<String>): Int {
        fun isValid(x: Int, y: Int): Boolean {
            return x >= 0 && x < lines[0].length && y >= 0 && y < lines.size
        }

        // Find S
        // Then from S, find the loop, by
        // Going in each direction, in which there's a connecting pipe, while keeping track of all visited positions
            // If a position is out of the field, then his is NOT the loop
            // If a position is visited twice, which is not S, then this is NOT the loop
            // If a position is visited twice, and it is S, then this is the loop
                // Return length of loop / 2
        val pipes = lines.map { line -> line.map { pipeMap[it]!! }}
        val startPos = pipes.asSequence().withIndex().flatMap { (x, line) -> line.withIndex().map { (y, pipe) -> Pair(pipe, Pair(x, y)) }}.find { it.first.start }!!.second
        val found = false
        var nrSteps = 0
        var currentPos = startPos
        var comingFrom: Direction? = null
        while (!found) {
            val top = NextPos(Top, currentPos.first, currentPos.second - 1)
            val right = NextPos(Right, currentPos.first + 1, currentPos.second)
            val bottom = NextPos(Bottom, currentPos.first, currentPos.second + 1)
            val left = NextPos(Left, currentPos.first - 1, currentPos.second)

            val possibleNextPositions = when (comingFrom) {
                null -> listOf(top, right, bottom, left)
                Top -> listOf(right, bottom, left)
                Right -> listOf(top, bottom, left)
                Bottom -> listOf(top, right, left)
                Left -> listOf(top, right, bottom)
            }.filter { isValid(it.x, it.y) }

            possibleNextPositions.find { pipes[it.x][it.y] }



            ++nrSteps
        }

        return lines.size
    }

    val lines = readLines("Day10.txt")

    part1(lines).println()
}

