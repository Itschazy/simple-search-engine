package search

import java.io.File

const val MENU = "\n=== Menu ===\n" +
        "1. Find a person\n" +
        "2. Print all people\n" +
        "0. Exit"
const val LIST_OF_PEOPLE = "=== List of people ==="

var inputData: List<String> = emptyList()
var inputMap = mutableMapOf<String, MutableList<Int>>()
val result: MutableList<String> = mutableListOf()

class SearchingEngine {

    fun runEngine() {
        fillMap(inputData)
        showMenu(inputData)
    }

    fun fillMap(inputData: List<String>) {

        for (line in inputData) {
            for (item in line.split(" ")) {
                val innerList = inputMap.getOrDefault(item.lowercase(), mutableListOf())
                innerList.add(inputData.indexOf(line))
                inputMap[item.lowercase()] = innerList
            }
        }
    }

    fun showMenu(inputData: List<String>) {
        var appStatus = true
        while (appStatus) {
            println(MENU)
            when (readln().toInt()) {
                1 -> doSearch(inputData)
                2 -> printAllPeople(inputData)
                0 -> {
                    println("\nBye!")
                    appStatus = false
                }
                else -> println("\nIncorrect option! Try again.")
            }
        }
    }

    fun printAllPeople(inputData: List<String>) {
        println(LIST_OF_PEOPLE)
        inputData.forEach { println(it) }
    }

    fun readSearchingStrategy(): String {
        println("\nSelect a matching strategy: ALL, ANY, NONE")
        return readln()
    }

    fun anySearch(inputData: List<String>, data: List<String>) {
        val indexesOfLines: MutableList<MutableList<Int>> = mutableListOf()
        data.filter { it in inputMap.keys }.forEach { indexesOfLines.add(inputMap.getValue(it)) }

        for (i in indexesOfLines.flatten()) {
            result.add(inputData[i])
        }
    }

    fun allSearch(inputData: List<String>, data: List<String>) {
        val indexesOfLines: MutableList<MutableList<Int>> = mutableListOf()
        data.filter { it in inputMap.keys }.forEach { indexesOfLines.add(inputMap.getValue(it)) }
        val index = indexesOfLines.flatten().groupingBy { it }.eachCount().filter { it.value == indexesOfLines.size }
        for (i in index.keys) {
            result.add(inputData[i])
        }
    }

    fun noneSearch(inputData: List<String>, data: List<String>) {
        val indexesOfLines: MutableList<MutableList<Int>> = mutableListOf()
        data.filter { it in inputMap.keys }.forEach { indexesOfLines.add(inputMap.getValue(it)) }

        inputMap.values.flatten().filterNot {
            indexesOfLines
                .flatten().contains(it)
        }
            .distinct()
            .forEach { result.add(inputData[it]) }
    }


    fun doSearch(inputData: List<String>) {
        val searchStrategy = readSearchingStrategy()
        println("\nEnter a name or email to search all suitable people.")
        val data = readln().split(" ").map { it.lowercase() }

        when (searchStrategy) {
            "ANY" -> anySearch(inputData, data)
            "ALL" -> allSearch(inputData, data)
            "NONE" -> noneSearch(inputData, data)
        }
        printResult(result)
        result.clear()
    }

    fun printResult(result: List<String>) {
        val person = if (result.size != 1) "persons" else "person"
        if (result.isNotEmpty()) {
            println("${result.size} $person found:")
            for (i in result.indices) {
                print("${result[i]}\n")
            }
        } else println("No matching people found.\n")
    }
}


fun main(args: Array<String>) {
    inputData = File(args[1]).readLines()
//    inputData = File("C:\\Users\\chxzyfps\\IdeaProjects\\Simple Search Engine\\${args[1]}").readLines()
    val search = SearchingEngine()
    search.runEngine()
}
