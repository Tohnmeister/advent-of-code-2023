fun main() {
    data class RaceInfo(val time: Int, val recordDistance: Int) {
        fun nrWaysToBeatRecordDistance(): Int {
            return (1 until time).count {
                val timeLeft = time - it
                val speed = it
                val distance = timeLeft * speed
                distance > recordDistance
            }
        }
    }

    fun part1(lines: List<String>): Int {
        val times = lines[0].substring("Time:".length).split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
        val distances = lines[1].substring("Distance:".length).split(' ').filter { it.isNotEmpty() }.map { it.toInt() }
        check(times.size == distances.size)
        val timesAndDistances = times.zip(distances).map { RaceInfo(it.first, it.second) }
        return timesAndDistances.map { it.nrWaysToBeatRecordDistance() }.reduce { acc, n -> acc * n }
    }

    val lines = readLines("Day06.txt")


    part1(lines).println()
}
