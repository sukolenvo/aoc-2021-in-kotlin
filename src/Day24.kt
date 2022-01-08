import kotlin.math.pow

object Day24 {

  fun part1(input: List<String>, monad: List<AluOp>): Long {
    var possibleStates = listOf(State(listOf(), Array(4) { 0 }))
    for (i in monad.indices) {
      if (input[i].startsWith("inp")) {
        possibleStates = possibleStates.groupingBy { it.wxyz[3] }
          .reduce { _, l, r -> l.coerseAtLeast(r) }
          .values
          .toList()
        possibleStates = possibleStates.flatMap { state -> (1..9).map { state.withInput(it) } }

        println("$i has ${possibleStates.size} states")
      } else {
        possibleStates.parallelStream().forEach { monad[i].invoke(it) }
      }
    }
    val model = possibleStates.filter { it.wxyz[3] == 0L }
      .reduce { l, r -> l.coerseAtLeast(r) }
      .model
    return model.mapIndexed { i, v -> 10.0.pow(13 - i).toLong() * v }.sum()
  }

  fun part2(input: List<String>, monad: List<AluOp>): Long {
    var possibleStates = listOf(State(listOf(), Array(4) { 0 }))
    for (i in monad.indices) {
      if (input[i].startsWith("inp")) {
        possibleStates = possibleStates.groupingBy { it.wxyz[3] }
          .reduce { _, l, r -> l.coerseAtMost(r) }
          .values
          .toList()
        possibleStates = possibleStates.flatMap { state -> (1..9).map { state.withInput(it) } }

        println("$i has ${possibleStates.size} states")
      } else {
        possibleStates.parallelStream().forEach { monad[i].invoke(it) }
      }
    }
    val model = possibleStates.filter { it.wxyz[3] == 0L }
      .reduce { l, r -> l.coerseAtMost(r) }
      .model
    return model.mapIndexed { i, v -> 10.0.pow(13 - i).toLong() * v }.sum()
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    // op 0 has 9 states
    // op 18 has 81 states
    // op 36 has 729 states
    // op 54 has 6561 states
    // op 72 has 7290 states
    // op 90 has 65610 states
    // op 108 has 590490 states
    // op 126 has 5314410 states
    // op 144 has 5904900 states
    // op 162 has 6386040 states
    // op 180 has 57474360 states
    // op 198 has 63628578 states
    // op 216 has 63570987 states
    // op 234 has 63630441 states
    // println(part1(input, input.map { parseOperation(it) }))
    println(part2(input, input.map { parseOperation(it) }))
  }

  private fun parseOperation(line: String): AluOp {
    val params = line.split(" ")
    return when (params[0]) {
      "inp" -> {
        {} // noop
      }
      "add" -> {
        check(params.size == 3)
        val aIndex = "wxyz".indexOf(params[1])
        val bIndexOf = "wxyz".indexOf(params[2])
        val b : (State) -> Long = if ("wxyz".indexOf(params[2]) == -1) {
          { params[2].toLong() }
        } else {
          { state: State -> state.wxyz[bIndexOf]}
        }
        val op: AluOp = { state ->
          state.wxyz[aIndex] = state.wxyz[aIndex] + b.invoke(state)
        }
        op
      }
      "mul" -> {
        check(params.size == 3)
        val aIndex = "wxyz".indexOf(params[1])
        val bIndexOf = "wxyz".indexOf(params[2])
        val b : (State) -> Long = if ("wxyz".indexOf(params[2]) == -1) {
          { params[2].toLong() }
        } else {
          { state: State -> state.wxyz[bIndexOf]}
        }
        val op: AluOp = { state ->
          state.wxyz[aIndex] = state.wxyz[aIndex] * b.invoke(state)
        }
        op
      }
      "div" -> {
        check(params.size == 3)
        val aIndex = "wxyz".indexOf(params[1])
        val bIndexOf = "wxyz".indexOf(params[2])
        val b : (State) -> Long = if ("wxyz".indexOf(params[2]) == -1) {
          { params[2].toLong() }
        } else {
          { state: State -> state.wxyz[bIndexOf]}
        }
        val op: AluOp = { state ->
          state.wxyz[aIndex] = state.wxyz[aIndex] / b.invoke(state)
        }
        op
      }
      "mod" -> {
        check(params.size == 3)
        val aIndex = "wxyz".indexOf(params[1])
        val bIndexOf = "wxyz".indexOf(params[2])
        val b : (State) -> Long = if ("wxyz".indexOf(params[2]) == -1) {
          { params[2].toLong() }
        } else {
          { state: State -> state.wxyz[bIndexOf]}
        }
        val op: AluOp = { state ->
          state.wxyz[aIndex] = state.wxyz[aIndex] % b.invoke(state)
        }
        op
      }
      "eql" -> {
        check(params.size == 3)
        val aIndex = "wxyz".indexOf(params[1])
        val bIndexOf = "wxyz".indexOf(params[2])
        val b : (State) -> Long = if ("wxyz".indexOf(params[2]) == -1) {
          { params[2].toLong() }
        } else {
          { state: State -> state.wxyz[bIndexOf]}
        }
        val op: AluOp = { state ->
          state.wxyz[aIndex] = if (state.wxyz[aIndex] == b.invoke(state)) {
            1
          } else {
            0
          }
        }
        op
      }
      else -> {
        throw Exception("Invalid operation")
      }
    }
  }
}

typealias AluOp = (State) -> Unit

class State(val model: List<Int>, val wxyz: Array<Long>) {

  fun withInput(input: Int): State {
    val model = this.model.toMutableList()
    model.add(input)
    val variables = this.wxyz.clone()
    variables[0] = input.toLong()
    return State(model, variables)
  }

  fun coerseAtLeast(other: State): State {
    for (i in model.indices) {
      if (model[i] > other.model[i]) {
        return this
      }
      if (model[i] < other.model[i]) {
        return other
      }
    }
    throw Error("Same models")
  }

  fun coerseAtMost(other: State): State {
    for (i in model.indices) {
      if (model[i] > other.model[i]) {
        return other
      }
      if (model[i] < other.model[i]) {
        return this
      }
    }
    throw Error("Same models")
  }


  override fun toString(): String {
    return "w: ${wxyz[0]},x: ${wxyz[1]},y: ${wxyz[2]},z: ${wxyz[3]}"
  }
}