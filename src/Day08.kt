fun main() {
    fun calculateNrSteps(instructions: List<Char>, nodes: Map<String, Pair<String, String>>, startingNode: String, endingNodePredicate: (String) -> Boolean): Long {
        var nrSteps: Long = 0
        var currentNode = startingNode
        while (!endingNodePredicate(currentNode)) {
            val idx = nrSteps % instructions.size
            val instruction = instructions[idx.toInt()]

            currentNode = if (instruction == 'L') nodes[currentNode]!!.first else nodes[currentNode]!!.second

            ++nrSteps
        }

        return nrSteps
    }

    fun part1(lines: List<String>): Long {
        val instructions = lines[0].toList()
        val nodes = lines.subList(2, lines.size).associate {
            val src = it.substring(0, 3)
            val left = it.substring(7, 10)
            val right = it.substring(12, 15)

            Pair(src, Pair(left, right))
        }

        return calculateNrSteps(instructions, nodes, "AAA") { it == "ZZZ" }
    }

    fun part2(lines: List<String>): Long {
        val instructions = lines[0].toList()
        val nodes = lines.subList(2, lines.size).associate {
            val src = it.substring(0, 3)
            val left = it.substring(7, 10)
            val right = it.substring(12, 15)

            Pair(src, Pair(left, right))
        }

        val startingNodes = nodes.keys.filter { it[2] == 'A' }.toMutableList()
        val allNrSteps = startingNodes.map { calculateNrSteps(instructions, nodes, it) { it.endsWith('Z') } }
        val max = allNrSteps.max()
        var lcm = max
        var foundLcm = false
        while (!foundLcm) {
            if (allNrSteps.all { lcm % it == 0L }) {
                foundLcm = true
            } else {
                lcm += max
            }
        }

        return lcm
    }

    val lines = readLines("Day08.txt")

    val part1 = part1(lines)
    check(part1 == 12643L)
    part1.println()
    part2(lines).println()
}
