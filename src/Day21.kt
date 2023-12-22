fun main() {
    fun logMap(i: Int, prevLines: MutableList<MutableList<String>>) {
        val verbose = false
        if (verbose) {
            println("After ${i + 1} steps:")
            for (line in prevLines) {
                println(line.joinToString(""))
            }
            println()
        }
    }

    fun part1(lines: List<String>): Int {
        val startPoint = findCharPos(lines, 'S')!!

        val maxX = lines[0].length - 1
        val maxY = lines.size - 1

        var prevLines = lines.map { it.map { it.toString() }.toMutableList() }.toMutableList()
        prevLines[startPoint.y][startPoint.x] = "0"
        var newLines: MutableList<MutableList<String>>
        for (i in 0..63) {
            val iStr = i.toString()
            val newIStr = (i + 1).toString()
            newLines = prevLines.toMutableList()
            for ((y, line) in newLines.withIndex()) {
                for ((x, str) in line.withIndex()) {
                    if (str == iStr) {
                        // Replace all neighbours with i + 1
                        val neighbours = Point(x, y).getNeighbours(maxX, maxY)
                        for (neighbour in neighbours) {
                            if (newLines[neighbour.y][neighbour.x] != "#") {
                                newLines[neighbour.y][neighbour.x] = newIStr
                            }
                        }
                    }
                }
            }
            prevLines = newLines
            logMap(i, prevLines)
        }

        return prevLines.sumOf { it.count { it == "64" } }
    }

    fun part2(lines: List<String>):  Int {
        val charPoints = lines.toCharPoints()

        // Determine starting points
        var currentSpots = setOf(findCharPos(lines, 'S')!!)

        // Determine invalid points
        val invalidPoints = charPoints.asSequence().filter { it.second == '#' }.map { it.first }.toSet()


        val maxX = lines[0].length - 1
        val maxY = lines.size - 1

        for (i in 1..26501365) {
            if (i % 10000 == 0) {
                println(i)
            }
            currentSpots = currentSpots.flatMap { spot -> spot.getNeighbours(maxX, maxY).asSequence().filter { neighbour -> neighbour !in invalidPoints }}.toSet()
        }

        return currentSpots.size
    }

    val lines = readLines("Day21.txt")

    val part1 = part1(lines)
    part1.println()
    check(part1 == 3764)

    val part2 = part2(lines)
    part2.println()
}
