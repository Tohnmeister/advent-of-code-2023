fun main() {
    data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
        var currentWorkflow = "in"

        fun getCriteria(char: Char): Int {
            return when (char) {
                'x' -> x
                'm' -> m
                'a' -> a
                's' -> s
                else -> throw IllegalArgumentException("Illegal criteria char $char")
            }
        }
    }

    fun createPart(partLine: String): Part {
        val sections = partLine.substring(1, partLine.length - 1).split(',')
        check(sections.size == 4)

        val x = sections[0].substring(2).toInt()
        val m = sections[1].substring(2).toInt()
        val a = sections[2].substring(2).toInt()
        val s = sections[3].substring(2).toInt()

        return Part(x, m, a, s)
    }

    data class Range(val min: Int, val max: Int) {
        val amount: Int
            get() = max - min + 1
    }

    class SplitResult<T>(val match: T?, val nonMatch: T?)

    data class PartRange(val x: Range, val m: Range, val a: Range, val s: Range, val currentWorkflow: String) {
        val amount: Long
            get() = x.amount.toLong() * m.amount * a.amount * s.amount

        fun getRange(char: Char): Range {
            return when (char) {
                'x' -> x
                'm' -> m
                'a' -> a
                's' -> s
                else -> throw IllegalArgumentException("Illegal criteria char $char")
            }
        }

        fun splitAbove(char: Char, value: Int, nextWorkflow: String): SplitResult<PartRange> {
            val range = getRange(char)

            if (range.max <= value) {
                // There is no above. Nothing matches.
                return SplitResult(null, this)
            }

            if (range.min > value) {
                // Everything matches.
                return SplitResult(this.copy(currentWorkflow = nextWorkflow), null)
            }

            return when (char) {
                'x' -> SplitResult(this.copy(x = x.copy(min = value + 1), currentWorkflow = nextWorkflow), this.copy(x = x.copy(max = value)))
                'm' -> SplitResult(this.copy(m = m.copy(min = value + 1), currentWorkflow = nextWorkflow), this.copy(m = m.copy(max = value)))
                'a' -> SplitResult(this.copy(a = a.copy(min = value + 1), currentWorkflow = nextWorkflow), this.copy(a = a.copy(max = value)))
                's' -> SplitResult(this.copy(s = s.copy(min = value + 1), currentWorkflow = nextWorkflow), this.copy(s = s.copy(max = value)))
                else -> throw IllegalArgumentException("Illegal criteria char $char")
            }
        }

        fun splitBelow(char: Char, value: Int, nextWorkflow: String): SplitResult<PartRange> {
            val range = getRange(char)

            if (range.min >= value) {
                // There is no below. Nothing matches.
                return SplitResult(null, this)
            }

            if (range.max < value) {
                // Everything matches.
                return SplitResult(this.copy(currentWorkflow = nextWorkflow), null)
            }

            return when (char) {
                'x' -> SplitResult(this.copy(x = x.copy(max = value - 1), currentWorkflow = nextWorkflow), this.copy(x = x.copy(min = value)))
                'm' -> SplitResult(this.copy(m = m.copy(max = value - 1), currentWorkflow = nextWorkflow), this.copy(m = m.copy(min = value)))
                'a' -> SplitResult(this.copy(a = a.copy(max = value - 1), currentWorkflow = nextWorkflow), this.copy(a = a.copy(min = value)))
                's' -> SplitResult(this.copy(s = s.copy(max = value - 1), currentWorkflow = nextWorkflow), this.copy(s = s.copy(min = value)))
                else -> throw IllegalArgumentException("Illegal criteria char $char")
            }
        }
    }

    class Rule(val text: String) {
        val nextWorkflow: String

        init {
            val colonIdx = text.indexOf(':')
            nextWorkflow = if (colonIdx >= 0) {
                text.substring(colonIdx + 1)
            } else {
                text
            }
        }

        fun matches(part: Part): Boolean {
            val colonIdx = text.indexOf(':')
            if (colonIdx >= 0) {
                val partCriteria = part.getCriteria(text[0])
                val conditionSymbol = text[1]
                val conditionValue = text.substring(2, colonIdx).toInt()

                check(conditionSymbol == '>' || conditionSymbol == '<')
                return if (conditionSymbol == '>') {
                    partCriteria > conditionValue
                } else {
                    partCriteria < conditionValue
                }
            }

            return true
        }

        fun splitRange(partRange: PartRange): SplitResult<PartRange> {
            val colonIdx = text.indexOf(':')
            return if (colonIdx >= 0) {
                val conditionSymbol = text[1]
                val conditionValue = text.substring(2, colonIdx).toInt()

                check(conditionSymbol == '>' || conditionSymbol == '<')

                if (conditionSymbol == '>') {
                    partRange.splitAbove(text[0], conditionValue, nextWorkflow)
                } else {
                    partRange.splitBelow(text[0], conditionValue, nextWorkflow)
                }
            } else {
                SplitResult(partRange.copy(currentWorkflow = nextWorkflow), null)
            }
        }
    }

    class Workflow(val label: String, val rules: List<Rule>) {
        fun getNextWorkflow(part: Part): String {
            val matchingRule = rules.find { it.matches(part) }
            return matchingRule?.nextWorkflow ?: label
        }

        fun handle(partRange: PartRange): List<PartRange> {
            val partRanges = mutableListOf<PartRange>()
            var currentPartRange: PartRange? = partRange
            for (rule in rules) {
                val splitResult = rule.splitRange(currentPartRange!!)
                if (splitResult.match != null) {
                    partRanges.add(splitResult.match)
                }
                currentPartRange = splitResult.nonMatch
                if (currentPartRange == null) {
                    break
                }
            }
            check(currentPartRange == null) // Last rule must always match
            return partRanges
        }
    }
    fun createWorkflow(workflowLine: String): Workflow {
        val rulesStartIdx = workflowLine.indexOf('{')
        val rulesEndIdx = workflowLine.indexOf('}')
        val label = workflowLine.substring(0, rulesStartIdx)
        val rules = workflowLine.substring(rulesStartIdx + 1, rulesEndIdx).split(',').map(::Rule)
        return Workflow(label, rules)
    }

    fun part1(lines: List<String>): Int {
        val emptyLineIdx = lines.indexOf("")

        val workflows = lines.take(emptyLineIdx).map(::createWorkflow).associateBy { it.label }
        val parts = lines.drop(emptyLineIdx + 1).map(::createPart)

        while (parts.any { it.currentWorkflow != "A" && it.currentWorkflow != "R" }) {
            for (part in parts.filter { it.currentWorkflow != "A" && it.currentWorkflow != "R" }) {
                val workflow = workflows[part.currentWorkflow]!!
                part.currentWorkflow = workflow.getNextWorkflow(part)
            }
        }

        return parts.filter { it.currentWorkflow == "A" }.sumOf { it.x + it.m + it.a + it.s }
    }

    fun part2(lines: List<String>): Long {
        val emptyLineIdx = lines.indexOf("")

        val workflows = lines.take(emptyLineIdx).map(::createWorkflow).associateBy { it.label }
        val partRange = PartRange(Range(1, 4000), Range(1, 4000), Range(1, 4000), Range(1, 4000), "in")

        var partRanges = listOf(partRange)

        while (partRanges.any { it.currentWorkflow != "A" && it.currentWorkflow != "R" }) {
            val (notDone, done) = partRanges.partition { it.currentWorkflow != "A" && it.currentWorkflow != "R" }
            val newPartRanges = mutableListOf<PartRange>()
            newPartRanges.addAll(done)
            for (partRange in notDone) {
                val workflow = workflows[partRange.currentWorkflow]!!
                newPartRanges.addAll(workflow.handle(partRange))
            }
            partRanges = newPartRanges
        }

        return partRanges.filter { it.currentWorkflow == "A" }.sumOf { it.amount }
    }


    val lines = readLines("Day19.txt")

    val part1 = part1(lines)
    part1.println()
    check (part1 == 346230)

    val part2 = part2(lines)
    part2.println()
    check(part2 == 124693661917133)
}
