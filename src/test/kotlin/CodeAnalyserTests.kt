package org.example

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText

class CodeAnalyzerTests {
    private fun String.isCamelCase(): Boolean {
        return this.isNotEmpty() && this[0].isLowerCase() && this.all { it.isLetter() } && this.any { it.isUpperCase() }
    }

    private val codeAnalyzer = CodeAnalyzer()

    @Test
    fun testFileReading() {
        val tempFile = createTempFile(suffix = ".kt")
        try {
            tempFile.writeText("fun example() { if (true) { println(\"Hello World\") } }")

            val result = codeAnalyzer.analyzeDirectory(tempFile.parent.toString())
            assertTrue(result.isNotEmpty(), "Should read files and return non-empty result.")
        } finally {
            tempFile.deleteIfExists()
        }
    }

    @Test
    fun testComplexityAnalysis() {
        val content = """
            fun first() { if (true) {} }
            fun second() { for (i in 1..10) {} if (false) {} if (false) {} }
            fun third() { while (true) {} if (true) {} }
        """.trimIndent()

        val complexities = codeAnalyzer.getMethodComplexities(content)
        assertEquals(3, complexities.size, "Should analyze three methods.")
        assertEquals(
            1,
            complexities.find { it.name == "first" }?.complexity,
            "Wrong complexity for method 'first'."
        )
        assertEquals(
            3,
            complexities.find { it.name == "second" }?.complexity,
            "Wrong complexity for method 'second'."
        )
        assertEquals(
            2,
            complexities.find { it.name == "third" }?.complexity,
            "Wrong complexity for method 'third'."
        )
    }

    @Test
    fun testNamingConvention() {
        val content = """
            fun correctCamelCase() {}
            fun IncorrectCamelCase() {}
            fun another_incorrect_example() {}
        """.trimIndent()

        val names = codeAnalyzer.getMethodNames(content)
        for (name in names) {
            println(name)
            println(name.isCamelCase())
        }
        val nonCamelCase = names.count { !it.isCamelCase() }

        assertEquals(2, nonCamelCase, "Two method names should violate camelCase convention.")
    }

    @Test
    fun testOutputResults() {
        val methods = listOf(
            MethodComplexity("methodOne", 1),
            MethodComplexity("methodTwo", 5),
            MethodComplexity("methodThree", 3)
        )

        val sortedMethods = methods.sortedByDescending { it.complexity }.take(3)
        assertEquals(
            "methodTwo",
            sortedMethods[0].name,
            "Highest complexity method should be 'methodTwo'."
        )
        assertEquals(5, sortedMethods[0].complexity, "Highest complexity score should be 5.")
    }
}
