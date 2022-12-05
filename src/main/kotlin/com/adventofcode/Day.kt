package com.adventofcode

import com.adventofcode.Day.process
import com.adventofcode.Day.solution
import java.util.*


object Day {
  private var script = false
  private val scheme = mutableListOf<String>()
  private val stackedColumns = mutableListOf<LinkedList<Char>>()

  fun process(line: String) {
    if (script) {
      return processCode(line)
    }
    if (line.isBlank()) {
      processScheme()
      script = true
      return
    }
    scheme.add(line)
  }

  private fun processScheme() {
    val columns = scheme.last().split("   ").last().trim().toInt()
    for (x in 0 until columns) {
      stackedColumns.add(LinkedList())
    }
    for (line in scheme.dropLast(1).asReversed()) {
      line.windowed(3, 4).forEachIndexed { idx, crateEncoded ->
        if (crateEncoded.isNotBlank()) {
          check(crateEncoded.length == 3 && crateEncoded.first() == '[' && crateEncoded.last() == ']')
          val crate = crateEncoded[1]
          stackedColumns[idx].push(crate)
        }
      }
    }
  }

  private fun processCode(line: String) {
    val (crates, from, to) = line.split("move ", " from ", " to ").filterNot(String::isBlank).map(String::toInt)
    val fromColumn = stackedColumns[from - 1]
    val toColumn = stackedColumns[to - 1]
    if(crates == 1) {
      fromColumn.pop().let(toColumn::push)
    } else {
      val buffered = fromColumn.take(crates)
      for(x in buffered.asReversed()) {
        fromColumn.removeFirst()
        toColumn.push(x)
      }
    }
  }


  fun solution(): String {
    return stackedColumns.map(LinkedList<Char>::getFirst).joinToString("")
  }
}

fun main() {
  ::main.javaClass
    .getResourceAsStream("/input")!!
    .bufferedReader()
    .forEachLine(::process)
  println(solution())
}
