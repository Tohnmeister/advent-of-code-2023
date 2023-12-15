fun main() {
    fun part1(lines: List<String>): Int {
        return lines.size
    }

    val lines = readLines("Day13Test.txt")
    for (line in lines) {
        println(line)
    }

    part1(lines).println()
}
