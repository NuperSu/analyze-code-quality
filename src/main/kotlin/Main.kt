package org.example

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class CodeAnalyzer {
    fun analyzeDirectory(path: String) {
        val files = File(path).walk()
            .filter { it.isFile && (it.extension == "java" || it.extension == "kt") }
            .toList()

        if (files.isEmpty()) {
            println("No Java or Kotlin files found in the directory.")
            return
        }

        val methodComplexities = mutableListOf<MethodComplexity>()
        val methodNames = mutableListOf<String>()

        files.forEach { file ->
            val content = Files.readString(Paths.get(file.toURI()))
            methodComplexities.addAll(getMethodComplexities(content))
            methodNames.addAll(getMethodNames(content))
        }

        val mostComplexMethods = methodComplexities.sortedByDescending { it.complexity }.take(3)
        val nonCamelCaseCount = methodNames.count { !it.isCamelCase() }

        println("Top 3 most complex methods:")
        mostComplexMethods.forEach {
            println("${it.name}: Complexity = ${it.complexity}")
        }

        if (methodNames.isNotEmpty()) {
            println("\nPercentage of methods not following camelCase: ${100.0 * nonCamelCaseCount / methodNames.size}%")
        }
    }

    fun getMethodComplexities(content: String): List<MethodComplexity> {
        val methodPattern = "fun\\s+(\\w+)\\s*\\([^)]*\\)\\s*\\{([^}]+)\\}".toRegex()
        return methodPattern.findAll(content).map {
            val methodName = it.groupValues[1]
            val methodBody = it.groupValues[2]
            val complexity =
                methodBody.count { ch -> ch in "?:ifsw".toList() } // Simplified complexity count
            MethodComplexity(methodName, complexity)
        }.toList()
    }

    private fun getMethodNames(content: String): List<String> {
        val methodPattern = "fun\\s+(\\w+)\\s*\\(".toRegex()
        return methodPattern.findAll(content).map { it.groupValues[1] }.toList()
    }

    private fun String.isCamelCase(): Boolean {
        return this[0].isLowerCase() && this.none { it.isWhitespace() }
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
