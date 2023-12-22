import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readLines(name: String) = Path("src/$name").readLines()

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

data class Point(val x: Int, val y: Int) {
    fun getNeighbours(maxX: Int, maxY: Int): List<Point> {
        return listOf(left, right, top, bottom).filter { it.x in 0..maxX && it.y in 0..maxY }
    }

    val left by lazy { Point(x - 1, y) }
    val right by lazy { Point(x + 1, y) }
    val top by lazy { Point(x, y - 1) }
    val bottom by lazy { Point(x, y + 1) }
}

fun findCharPos(lines: List<String>, char: Char): Point? {
    return lines.asSequence().withIndex()
        .flatMap { (y, line) -> line.withIndex().map { (x, char) -> Pair(char, Point(x, y)) } }
        .find { it.first == char }?.second
}

fun List<String>.toCharPoints(): List<Pair<Point, Char>> {
    return this
        .withIndex()
        .flatMap { (y, line) -> line
            .withIndex()
            .map { (x, char)  -> Pair(Point(x, y), char) }
        }
}
