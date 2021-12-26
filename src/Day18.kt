import kotlin.math.ceil

object Day18 {

  fun part1(input: List<SnailfishNumber>): Long {
    return input.reduce { l, r -> l.add(r) }.magnitude()
  }

  fun part2(input: List<SnailfishNumber>): Long {
    return input.flatMap { i -> input.map { i.clone().add(it.clone()).magnitude().coerceAtLeast(it.clone().add(i.clone()).magnitude()) } }.maxOf { it }
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val numbers = input.map { parseNumber(it) }
    println(part1(numbers.map { it.clone() }))
    println(part2(numbers))
  }

  private fun parseNumber(number: String): SnailfishNumber {
    val queue = ArrayDeque<SnailfishNumber>()
    queue.addLast(SnailfishNumber(false))
    for (c in number) {
      if (c == '[') {
        queue.addLast(SnailfishNumber(false))
      } else if (c == ',') {
        queue.addLast(SnailfishNumber(false))
      } else if (c == ']') {
        val right = queue.removeLast()
        val left = queue.removeLast()
        queue.last().left = left
        queue.last().right = right
      } else {
        val item = queue.last()
        item.fixed = true
        item.value = item.value * 10 + c.digitToInt()
      }
    }
    return queue.single()
  }
}

data class SnailfishNumber(var fixed: Boolean) {
  lateinit var left: SnailfishNumber
  lateinit var right: SnailfishNumber
  var value: Int = 0

  constructor(value: Int) : this(true) {
    this.value = value
  }

  constructor(left: SnailfishNumber, right: SnailfishNumber) : this(false) {
    this.left = left
    this.right = right
  }

  fun add(other: SnailfishNumber): SnailfishNumber {
    val result = SnailfishNumber(this, other)
    result.reduce()
    return result
  }

  private fun reduce() {
    while (true) {
      if (explode(1) != null) {
        continue
      }
      if (!split()) {
        break
      }
    }
  }

  private fun split(): Boolean {
    if (fixed) {
      if (value >= 10) {
        this.fixed = false
        this.left = SnailfishNumber(value / 2)
        this.right = SnailfishNumber(ceil(value.toDouble() / 2).toInt())
        return true
      }
      return false
    }
    return left.split() || right.split()
  }

  private fun explode(level: Int): Pair<Int, Int>? {
    if (fixed) {
      return null
    }
    if (level >= 5 && left.fixed) {
      check(right.fixed) { "expecting fixed" }
      fixed = true
      value = 0
      return Pair(left.value, right.value)
    }
    var explode = left.explode(level + 1)
    if (explode != null) {
      right.addToLeft(explode.second)
      return Pair(explode.first, 0)
    }
    explode = right.explode(level + 1)
    if (explode != null) {
      left.addToRight(explode.first)
      return Pair(0, explode.second)
    }
    return null
  }

  private fun addToLeft(value: Int) {
    if (value == 0) {
      return
    }
    if (fixed) {
      this.value += value
    } else {
      this.left.addToLeft(value)
    }
  }

  private fun addToRight(value: Int) {
    if (value == 0) {
      return
    }
    if (fixed) {
      this.value += value
    } else {
      this.right.addToRight(value)
    }
  }

  fun magnitude(): Long {
    if (fixed) {
      return value.toLong()
    }
    return left.magnitude() * 3 + right.magnitude() * 2
  }

  override fun toString(): String {
    if (fixed) {
      return value.toString()
    }
    return "[$left,$right]"
  }

  fun clone(): SnailfishNumber {
    if (fixed) {
      return SnailfishNumber(value)
    }
    return SnailfishNumber(left.clone(), right.clone())
  }
}