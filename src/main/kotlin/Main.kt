package org.example

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class CodeAnalyzer {
    fun analyzeDirectory(path: String): List<String> {
        val files = File(path).walk()
            .filter { it.isFile && (it.extension == "java" || it.extension == "kt") }
            .toList()

        val methodComplexities = mutableListOf<MethodComplexity>()
        val methodNames = mutableListOf<String>()
        val results = mutableListOf<String>()

        files.forEach { file ->
            val content = Files.readString(Paths.get(file.toURI()))
            methodComplexities.addAll(getMethodComplexities(content))
            methodNames.addAll(getMethodNames(content))
        }

        val mostComplexMethods = methodComplexities.sortedByDescending { it.complexity }.take(3)
        val nonCamelCaseCount = methodNames.count { !it.isCamelCase() }

        mostComplexMethods.forEach {
            results.add("${it.name}: Complexity = ${it.complexity}")
        }

        results.add("Percentage of methods not following camelCase: ${100.0 * nonCamelCaseCount / methodNames.size}%")
        return results
    }

    fun getMethodComplexities(content: String): List<MethodComplexity> {
        return getMethods(content).map { (name, body) ->
            val complexity = calculateComplexity(body)
            MethodComplexity(name, complexity)
        }
    }

    internal fun calculateComplexity(methodBody: String): Int {
        val keywords = listOf("if", "for", "while", "switch", "do", "try", "catch", "finally")
        return keywords.sumOf { keyword ->
            "\\b$keyword\\b".toRegex(setOf(RegexOption.MULTILINE)).findAll(methodBody).count()
        }
    }

    internal fun getMethodNames(content: String): List<String> {
        return getMethods(content).map { it.first }
    }

    private fun getMethods(content: String): List<Pair<String, String>> {
        val methodPattern = """fun\s+(\w+)\s*\([^)]*\)\s*\{([\s\S]*?)\}(?=\s*(fun|$))""".toRegex()
        return methodPattern.findAll(content).map {
            it.groupValues[1] to it.groupValues[2]
        }.toList()
    }

    private fun String.isCamelCase(): Boolean {
        return this.isNotEmpty() && this[0].isLowerCase() && this.all { it.isLetter() } && this.any { it.isUpperCase() }
    }
}

data class MethodComplexity(val name: String, val complexity: Int)

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please provide the directory path as a command line argument.")
        return
    }

    val directoryPath = args[0]
    val codeAnalyzer = CodeAnalyzer()
    codeAnalyzer.analyzeDirectory(directoryPath)
}
