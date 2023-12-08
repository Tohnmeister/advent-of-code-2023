fun main() {
    fun part1(lines: List<String>): Long {
        val instructions = lines[0].toList()
        println(instructions.size)
        val nodes = lines.subList(2, lines.size).associate {
            val src = it.substring(0, 3)
            val left = it.substring(7, 10)
            val right = it.substring(12, 15)

            Pair(src, Pair(left, right))
        }

        var nrSteps: Long = 0
        var currentNode = "AAA"
        while (currentNode != "ZZZ") {
            val idx = nrSteps % instructions.size
            val instruction = instructions[idx.toInt()]

            currentNode = if (instruction == 'L') nodes[currentNode]!!.first else nodes[currentNode]!!.second

            nrSteps++
        }

        return nrSteps
    }

    val lines = readLines("Day08.txt")

    part1(lines).println()
}
