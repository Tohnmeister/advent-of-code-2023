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

    class Rule(val text: String) {
        val nextWorkflow: String

        init {
            val colonIdx = text.indexOf(':')
            if (colonIdx >= 0) {
                nextWorkflow = text.substring(colonIdx + 1)
            } else {
                nextWorkflow = text
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
    }
    class Workflow(val label: String, val rules: List<Rule>) {
        fun getNextWorkflow(part: Part): String {
            val matchingRule = rules.find { it.matches(part) }
            return matchingRule?.nextWorkflow ?: label
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

    val lines = readLines("Day19.txt")

    part1(lines).println()
}