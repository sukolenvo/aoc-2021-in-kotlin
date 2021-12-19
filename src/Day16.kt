object Day16 {

  fun part1(input: String): Int {
    val packages = parse(input, 0, input.length)
    return packages.sumOf { it.versionSum() }
  }

  private fun parse(input: String, start: Int, end: Int): List<Package> {
    var position = start
    val packages = mutableListOf<Package>()
    while (position < end) {
      if (input.toCharArray().drop(position).all { it == '0' }) {
        break
      }
      val result = parsePackage(input, position)
      position = result.first
      packages.add(result.second)
    }
    return packages
  }

  private fun parsePackage(input: String, packageStart: Int): Pair<Int, Package> {
    val version = input.substring(packageStart, packageStart + 3).toInt(2)
    val type = input.substring(packageStart + 3, packageStart + 6).toInt(2)
    return when(type) {
      4 -> {
        val payloadStart = packageStart + 6
        var payloadEnd = payloadStart
        while (input[payloadEnd] == '1') {
          payloadEnd += 5
        }
        payloadEnd += 5
        Pair(payloadEnd, Package(type, version, input.substring(payloadStart, payloadEnd)))
      }
      else -> {
        val payloadEncodingType = input[packageStart + 6]
        if (payloadEncodingType == '0') {
          val payloadStart = packageStart + 22
          val payloadLength = input.substring(packageStart + 7, payloadStart).toInt(2)
          val children = parse(input, payloadStart, payloadStart + payloadLength)
          Pair(payloadStart + payloadLength, Package(type, version, input.substring(payloadStart, payloadStart + payloadLength), children))
        } else {
          val payloadStart = packageStart + 18
          val childrenCount = input.substring(packageStart + 7, payloadStart).toInt(2)
          var position = payloadStart
          val children = mutableListOf<Package>()
          for (i in 0 until childrenCount) {
            val child = parsePackage(input, position)
            position = child.first
            children.add(child.second)
          }
          Pair(position, Package(type, version, input.substring(payloadStart, position), children))
        }
      }
    }
  }

  fun part2(input: String): Long {
    return parse(input, 0, input.length).single { true }.calculateValue()
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val transmission = input[0].toCharArray().joinToString("") { it.digitToInt(16).toString(2).padStart(4, '0') }
    check(transmission.length % 4 == 0) { "padding missing" }
    println(transmission)
    println(part1(transmission))
    println(part2(transmission))
  }
}

data class Package(val type: Int, val version: Int, val payload: String, val children: List<Package> = listOf()) {
  fun versionSum(): Int = version + children.sumOf { it.versionSum() }

  fun calculateValue(): Long {
    return when(type) {
      4 -> {
        payload.chunked(5).joinToString("") { it.substring(1, 5) }.toLong(2)
      }
      0 -> {
        children.sumOf { it.calculateValue() }
      }
      1 -> {
        children.map { it.calculateValue() }.reduce { l, r -> l * r }
      }
      2 -> {
        children.minOf { it.calculateValue() }
      }
      3 -> {
        children.maxOf { it.calculateValue() }
      }
      5 -> {
        check(children.size == 2)
        if (children[0].calculateValue() > children[1].calculateValue()) {
          1
        } else {
          0
        }
      }
      6 -> {
        check(children.size == 2)
        if (children[0].calculateValue() < children[1].calculateValue()) {
          1
        } else {
          0
        }
      }
      7 -> {
        check(children.size == 2)
        if (children[0].calculateValue() == children[1].calculateValue()) {
          1
        } else {
          0
        }
      }
      else -> -1
    }
  }
}