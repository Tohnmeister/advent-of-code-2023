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
