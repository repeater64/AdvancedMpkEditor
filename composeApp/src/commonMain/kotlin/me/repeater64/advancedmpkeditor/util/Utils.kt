package me.repeater64.advancedmpkeditor.util

fun prettyPrintDataClass(input: Any, indentStep: String = "    ") {
    val sb = StringBuilder()
    var level = 0

    val trimInput = input.toString().trim()
    var i = 0
    while (i < trimInput.length) {
        val char = trimInput[i]

        when (char) {
            '(', '[', '{' -> {
                level++
                sb.append(char).append("\n").append(indentStep.repeat(level))
            }
            ')', ']', '}' -> {
                level--
                sb.append("\n").append(indentStep.repeat(level)).append(char)
            }
            ',' -> {
                sb.append(char).append("\n").append(indentStep.repeat(level))
                // Skip the space that usually follows a comma in toString()
                if (i + 1 < trimInput.length && trimInput[i + 1] == ' ') i++
            }
            else -> sb.append(char)
        }
        i++
    }
    println(sb.toString())
}

fun mapIntToAlpha(n: Int): String {
    if (n < 0) throw IllegalArgumentException("Input must be non-negative")

    val result = StringBuilder()
    var num = n

    do {
        // Get the remainder to find the current character (0=a, 1=b...)
        val remainder = num % 26
        result.append('a' + remainder)

        // Move to the next "digit"
        // We subtract 1 for subsequent digits because 'aa' represents 26,
        // effectively shifting the logic for multi-character strings.
        num = (num / 26) - 1
    } while (num >= 0)

    return result.reverse().toString()
}