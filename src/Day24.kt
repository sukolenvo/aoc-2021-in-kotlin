import kotlin.math.pow

object Day24 {

  fun part1(monad: AluOp): Long {
    val model = "99999999999999".toCharArray().map { it.digitToInt() }.toTypedArray()
    check(model.size == 14)
    while (true) {
      val variables = mutableMapOf(Pair("w", 0L), Pair("x", 0), Pair("y", 0), Pair("z", 0))
      monad.invoke(model.toMutableList(), variables)
      if (variables["z"] == 0L) {
        return model.mapIndexed { i, v -> 10.0.pow(13 - i).toLong() * v }.sum()
      }
      decrease(model)
    }
  }

  private fun decrease(model: Array<Int>) {
    for (i in model.indices) {
      if (model[model.size - i - 1] == 1) {
        model[model.size - i - 1] = 9
      } else {
        model[model.size - i - 1]--
        return
      }
    }
    throw Exception("Can't decrease model")
  }

  fun part2(input: List<String>): Int {
    return 0
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    println(part1(buildMonad(input)))
    println(part2(input))
  }

  private fun buildMonad(input: List<String>): AluOp {
    val operations = input.map { parseOperation(it) }
    return { input, variables ->
      operations.forEach { it.invoke(input, variables) }
    }
  }

  private fun parseOperation(line: String): AluOp {
    val params = line.split(" ")
    return when (params[0]) {
      "inp" -> {
        check(params.size == 2)
        val op: AluOp = { input, variables ->
          variables[params[1]] = input.removeFirst().toLong()
        }
        op
      }
      "add" -> {
        check(params.size == 3)
        val op: AluOp = { _, variables ->
          val operand = variables[params[2]] ?: params[2].toLong()
          variables[params[1]] = variables[params[1]]!! + operand
        }
        op
      }
      "mul" -> {
        check(params.size == 3)
        val op: AluOp = { _, variables ->
          val operand = variables[params[2]] ?: params[2].toLong()
          variables[params[1]] = variables[params[1]]!! * operand
        }
        op
      }
      "div" -> {
        check(params.size == 3)
        val op: AluOp = { _, variables ->
          val operand = variables[params[2]] ?: params[2].toLong()
          check(operand != 0L)
          variables[params[1]] = variables[params[1]]!! / operand
        }
        op
      }
      "mod" -> {
        check(params.size == 3)
        val op: AluOp = { _, variables ->
          val operand = variables[params[2]] ?: params[2].toLong()
          check(operand > 0)
          check(variables[params[1]]!! >= 0)
          variables[params[1]] = variables[params[1]]!! % operand
        }
        op
      }
      "eql" -> {
        check(params.size == 3)
        val op: AluOp = { _, variables ->
          val operand = variables[params[2]] ?: params[2].toLong()
          variables[params[1]] = if (variables[params[1]]!! == operand) {
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

typealias AluOp = (MutableList<Int>, MutableMap<String, Long>) -> Unit
