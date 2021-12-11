import kotlin.math.pow

data class Task(val vocab: List<String>, val display: List<String>) {
  fun decodeDisplay(): Int {
    val patterns = Array(10) { "" }
    patterns[1] = vocab.single { it.length == 2 }
    patterns[4] = vocab.single { it.length == 4 }
    patterns[7] = vocab.single { it.length == 3 }
    patterns[8] = vocab.single { it.length == 7 }
    val bd = patterns[4].toCharArray().filterNot { patterns[1].contains(it) }
    val zeroTwoThree = vocab.filterNot { patterns.contains(it) }
      .filter { unknownNumbers -> unknownNumbers.toCharArray().count { bd.contains(it) } == 1 }
    patterns[0] = zeroTwoThree.single { it.length == 6 }
    val beg = patterns[0].toCharArray().filterNot { patterns[7].contains(it) }
    patterns[6] = vocab.filterNot { patterns.contains(it) }
      .single { unknownNumbers -> beg.toCharArray().all { unknownNumbers.contains(it) } }
    patterns[9] = vocab.filterNot { patterns.contains(it) }.single { it.length == 6 }
    val b = bd.single { beg.contains(it) }
    patterns[5] = vocab.filterNot { patterns.contains(it) }.single { it.contains(b) }
    val e = patterns[6].toCharArray().single { !patterns[5].contains(it) }
    patterns[2] = vocab.filterNot { patterns.contains(it) }.single { it.contains(e) }
    patterns[3] = vocab.single { !patterns.contains(it) }
    check(patterns.filterNot { it.isEmpty() }.distinct().count() == 10)
    return display.reversed().mapIndexed { index, pattern ->
      val digit = patterns.indexOf(pattern)
      check(digit != -1) { "pattern not found" }
      digit * (10.0.pow(index).toInt())
    }.sum()
  }
}

object Day08 {

  @JvmStatic
  fun main(args: Array<String>) {
    fun part1(input: List<Task>): Int {
      return input.flatMap { it.display.asSequence() }.filter { setOf(2, 4, 3, 7).contains(it.length) }.count()
    }

    fun part2(input: List<Task>): Int {
      return input.sumOf { it.decodeDisplay() }
    }

    val input = readInput(javaClass.name)
    val tasks = input.map {
      it.split(" | ").windowed(2).map { (vocab, display) ->
        Task(vocab.trim().split(" ").map { it.sort() }, display.trim().split(" ").map { it.sort() }) }[0]
    }
    println(part1(tasks))
    println(part2(tasks))
  }
}
