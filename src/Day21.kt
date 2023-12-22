fun main() {
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
            println("After ${i + 1} steps:")
            for (line in prevLines) {
                println(line.joinToString(""))
            }
            println()
        }

        return prevLines.sumOf { it.count { it == "64" } }
    }

    val lines = readLines("Day21.txt")

    part1(lines).println()
}
