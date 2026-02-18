package me.repeater64.advancedmpkeditor.backend.commands

import kotlin.math.max
import kotlin.math.min

interface CommandCondition {
    fun getOrListOfAnds() : List<List<CommandObjectiveRange>> // The inner list is a list of CommandObjectiveRanges that must ALL be met at the same time. If any of these inner lists are met, the overall condition is met.

    infix fun and(other: CommandCondition): CommandCondition {
        return CommandConditionAnd(this, other)
    }

    infix fun or(other: CommandCondition): CommandCondition {
        return CommandConditionOr(this, other)
    }

    operator fun not(): CommandCondition {
        return CommandConditionNot(this)
    }

    companion object {
        fun print(condition: CommandCondition) {
            println("------")
            val dnf = condition.getOrListOfAnds()

            if (dnf.isEmpty()) {
                println("FALSE (Impossible)")
            } else {
                dnf.forEachIndexed { index, chain ->
                    val chainStr = if (chain.isEmpty()) "TRUE" else chain.joinToString(" AND ")
                    println("Option ${index + 1}: $chainStr")
                }
            }
            println()
        }
    }
}

data class CommandObjectiveRange(
    val objective: String,
    val range: IntRange,
    val totalWeight: Int
) : CommandCondition {

    override fun getOrListOfAnds(): List<List<CommandObjectiveRange>> {
        return listOf(listOf(this))
    }

    override fun toString(): String = "$objective:${range}"

    fun negate(): List<CommandObjectiveRange> {
        if (range.isEmpty()) return listOf(this.copy(range = 0 until totalWeight))

        val result = mutableListOf<CommandObjectiveRange>()

        if (range.first > 0) {
            result.add(this.copy(range = 0 until range.first))
        }
        if (range.last < totalWeight - 1) {
            result.add(this.copy(range = (range.last + 1) until totalWeight))
        }
        return result
    }

    fun intersect(other: CommandObjectiveRange): CommandObjectiveRange? {
        // Only merge ranges for the same objective
        if (this.objective != other.objective) return null

        val newStart = max(this.range.first, other.range.first)
        val newEnd = min(this.range.last, other.range.last)

        // If start > end, there is no overlap
        if (newStart > newEnd) return null

        return this.copy(range = newStart..newEnd)
    }

    fun getCommandIfString(): String {
        if (range.first == range.last) {
            return "if score @p $objective matches ${range.first}"
        }
        return "if score @p $objective matches ${range.first}..${range.last}"
    }
}

class CommandConditionAnd(val a: CommandCondition, val b: CommandCondition) : CommandCondition {
    override fun getOrListOfAnds(): List<List<CommandObjectiveRange>> {
        val leftOptions = a.getOrListOfAnds()
        val rightOptions = b.getOrListOfAnds()

        return leftOptions.flatMap { leftChain ->
            // Use mapNotNull. If simplifyChain returns null (impossible),
            // this path is automatically removed from the list.
            rightOptions.mapNotNull { rightChain ->
                simplifyChain(leftChain + rightChain)
            }
        }
    }
}

class CommandConditionOr(val a: CommandCondition, val b: CommandCondition) : CommandCondition {
    override fun getOrListOfAnds(): List<List<CommandObjectiveRange>> {
        return a.getOrListOfAnds() + b.getOrListOfAnds()
    }
}

class CommandConditionNot(val condition: CommandCondition) : CommandCondition {
    override fun getOrListOfAnds(): List<List<CommandObjectiveRange>> {
        val originalOrs = condition.getOrListOfAnds()

        if (originalOrs.isEmpty()) return listOf(emptyList())
        if (originalOrs.firstOrNull()?.isEmpty() == true) return emptyList()

        val negatedGroups = originalOrs.map { andChain ->
            andChain.flatMap { atom ->
                atom.negate().map { negatedAtom -> listOf(negatedAtom) }
            }
        }

        if (negatedGroups.isEmpty()) return emptyList()

        return negatedGroups.reduce { accumulated, nextGroup ->
            accumulated.flatMap { leftChain ->
                // IMPORTANT: Call simplifyChain here too
                nextGroup.mapNotNull { rightChain ->
                    simplifyChain(leftChain + rightChain)
                }
            }
        }
    }
}

fun simplifyChain(chain: List<CommandObjectiveRange>): List<CommandObjectiveRange>? {
    // 1. Group by objective (e.g. put all "Score" items together)
    val grouped = chain.groupBy { it.objective }
    val result = mutableListOf<CommandObjectiveRange>()

    for ((_, ranges) in grouped) {
        // Start with the first range (e.g. Score:11..99)
        var currentStart = ranges.first().range.first
        var currentEnd = ranges.first().range.last
        val weight = ranges.first().totalWeight
        val objName = ranges.first().objective

        // 2. Intersect it with every other range in the group
        for (i in 1 until ranges.size) {
            val nextRange = ranges[i].range

            // Calculate overlap
            val newStart = maxOf(currentStart, nextRange.first)
            val newEnd = minOf(currentEnd, nextRange.last)

            // If start > end, there is no overlap. The condition is impossible.
            if (newStart > newEnd) return null

            currentStart = newStart
            currentEnd = newEnd
        }
        result.add(CommandObjectiveRange(objName, currentStart..currentEnd, weight))
    }
    return result
}

fun main() {
    val total = 100

    // Helper to create ranges quickly
    fun range(name: String, start: Int, end: Int) =
        CommandObjectiveRange(name, start..end, total)

    // Helper to visualize the complex output
    fun printResult(label: String, condition: CommandCondition) {
        println("--- $label ---")
        val dnf = condition.getOrListOfAnds()

        if (dnf.isEmpty()) {
            println("FALSE (Impossible)")
        } else {
            dnf.forEachIndexed { index, chain ->
                val chainStr = if (chain.isEmpty()) "TRUE" else chain.joinToString(" AND ")
                println("Option ${index + 1}: $chainStr")
            }
        }
        println()
    }

    // --- TEST SCENARIOS ---

    val condA = range("Score", 0, 10)
    val condB = range("Score", 40, 60)
    val condC = range("Health", 80, 90)

    // 1. Direct Usage (No Wrapper)
    printResult("1. Single Range (Score 0-10)", condA)

    // 2. AND Logic
    // Must satisfy Score 0-10 AND Health 80-90
    val andCond = CommandConditionAnd(condA, condC)
    printResult("2. AND (Score 0-10 && Health 80-90)", andCond)

    // 3. OR Logic
    // Can satisfy Score 0-10 OR Score 40-60
    val orCond = CommandConditionOr(condA, condB)
    printResult("3. OR (Score 0-10 || Score 40-60)", orCond)

    // 4. Negation of OR (De Morgan's)
    // NOT(Score 0-10 || Score 40-60)
    // Should result in ranges that are NOT 0-10 AND NOT 40-60
    // Expected: 11-39 AND 61-99
    val notOr = CommandConditionNot(orCond)
    printResult("4. NOT (A || B) -> Expect gaps between A and B", notOr)

    // 5. Complex Nesting
    // (A OR B) AND NOT(C)
    val complex = CommandConditionAnd(
        orCond,
        CommandConditionNot(condC)
    )
    printResult("5. (A || B) && !C", complex)
}