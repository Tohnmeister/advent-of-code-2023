data class Grab(val red: Int, val green: Int, val blue: Int) {
    fun isPossible(maxGrab: Grab): Boolean {
        return red <= maxGrab.red && green <= maxGrab.green && blue <= maxGrab.blue
    }
}

data class Game(val id: Int, val grabs: List<Grab>) {
     fun isPossible(maxGrab: Grab): Boolean {
         return grabs.all { it.isPossible(maxGrab) }
     }
}

fun createGames(lines: List<String>): List<Game> {
    return lines.map {
        val spacePos = it.indexOf(' ')
        val idPos = spacePos + 1
        val colonPos = it.indexOf(':')
        val id = it.substring(idPos, colonPos).toInt()
        val grabsString = it.substring(colonPos + 1)
        val grabStrings = grabsString.split(';')
        val grabs = createGrabs(grabStrings)
        Game(id, grabs)
    }
}

val RedRegex = """(\d+) red""".toRegex()
val GreenRegex = """(\d+) green""".toRegex()
val BlueRegex = """(\d+) blue""".toRegex()

fun createGrabs(grabStrings: List<String>): List<Grab> {
    return grabStrings.map {
        val redMatch = RedRegex.find(it)
        val greenMatch = GreenRegex.find(it)
        val blueMatch = BlueRegex.find(it)
        val red = redMatch?.groupValues?.get(1)?.toInt() ?: 0
        val green = greenMatch?.groupValues?.get(1)?.toInt() ?: 0
        val blue = blueMatch?.groupValues?.get(1)?.toInt() ?: 0
        Grab(red, green, blue)
    }
}

fun main() {
    fun part1(games: List<Game>): Int {
        val maxGrab = Grab(12, 13, 14)
        return games.asSequence().filter { it.isPossible(maxGrab) }.sumOf { it.id }
    }

    val lines = readLines("Day02.txt")
    val games = createGames(lines)

    part1(games).println()
}
