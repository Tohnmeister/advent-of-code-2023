fun main() {
    class Lens(val label: String, val focalLength: Int)

    class Box {
        val lenses = mutableListOf<Lens>()

        fun addLens(lens: Lens) {
            val existingIdx = lenses.indexOfFirst { it.label == lens.label }

            if (existingIdx >= 0) {
                lenses[existingIdx] = lens
            } else {
                lenses.add(lens)
            }
        }

        fun removeLens(label: String) {
            val idx = lenses.indexOfFirst { it.label == label }
            if (idx >= 0) {
                lenses.removeAt(idx)
            }
        }

        fun totalFocusingPower(boxNumber: Int): Int {
            return lenses.asSequence().withIndex().sumOf { (idx, lens) -> (boxNumber + 1) * (idx + 1) * lens.focalLength}
        }
    }

    fun calculateHash(step: String): Int = step.fold(0) { currentValue, char -> ((currentValue + char.code) * 17) % 256 }

    fun part1(lines: List<String>): Int {
        val steps = lines[0].split(',')

        return steps.sumOf { calculateHash(it) }
    }

    fun part2(lines: List<String>): Int {
        val steps = lines[0].split(',')

        val boxes = List(256) { Box() }

        for (step in steps) {
            if (step.contains('-')) {
                // Remove at end of box
                val lensLabel = step.substring(0, step.indexOf('-'))
                val boxIdx = calculateHash(lensLabel)
                boxes[boxIdx].removeLens(lensLabel)
            } else {
                val lensLabel = step.substring(0, step.indexOf('='))
                val boxIdx = calculateHash(lensLabel)
                val focalLength = step.drop(step.indexOf('=') + 1).toInt()
                boxes[boxIdx].addLens(Lens(lensLabel, focalLength))
            }
        }

        return boxes.asSequence().withIndex().sumOf { (idx, box) -> box.totalFocusingPower(idx) }
    }

    val lines = readLines("Day15.txt")
//    val lines = readLines("Day15Test.txt")

    val part1 = part1(lines)
    check(part1 == 511215)
    part1.println()
    val part2 = part2(lines)
    part2.println()
}
