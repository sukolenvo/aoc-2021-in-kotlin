import java.math.BigInteger

object Day22 {

  fun part1(input: List<Cube>): Int {
    return input
      .filter { it.value == 1 }.sumOf {
        var result = 0
        val alreadyProcessed = ((input.indexOf(it) + 1) until input.size)
          .map { input[it] }
        for (x in it.area.startX.coerceAtLeast(-50)..it.area.endX.coerceAtMost(50)) {
          for (y in it.area.startY.coerceAtLeast(-50)..it.area.endY.coerceAtMost(50)) {
            for (z in it.area.startZ.coerceAtLeast(-50)..it.area.endZ.coerceAtMost(50)) {
              if (alreadyProcessed.none { it.area.collision(x, y, z) }) {
                result++
              }
            }
          }
        }
        result
      }
  }

  fun part2(input: List<Cube>): BigInteger {
    var areas = mutableListOf<Area>()
    for (cube in input) {
      areas = areas.flatMap { it.deduct(cube.area) }.toMutableList()
      if (cube.value == 1) {
        areas.add(cube.area)
      }
    }
    return areas.map {
      BigInteger.valueOf(it.endX.toLong() - it.startX + 1)
        .multiply(BigInteger.valueOf(it.endY.toLong() - it.startY + 1))
        .multiply(BigInteger.valueOf(it.endZ.toLong() - it.startZ + 1))
    }
      .reduce { l, r -> l.add(r) }
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val cubes = input.map {
      val value = if (it.startsWith("on")) {
        1
      } else {
        0
      }
      val coordinates = it.replace(Regex(".* "), "")
        .split(",")
        .map { it.replace(Regex(".*="), "") }
        .flatMap { it.split("..") }
        .map { it.toInt() }
      Cube(Area(coordinates[0], coordinates[1], coordinates[2], coordinates[3], coordinates[4], coordinates[5]), value)
    }
    // println(part1(cubes))
    println(part2(cubes))
  }
}

data class Area(val startX: Int, val endX: Int, val startY: Int, val endY: Int, val startZ: Int, val endZ: Int) {

  fun collision(x: Int, y: Int, z: Int): Boolean {
    return x in (startX..endX) && y in (startY..endY) && z in (startZ..endZ)
  }

  fun collision(area: Area): Boolean {
    return area.startX <= endX && area.endX >= startX &&
        area.startY <= endY && area.endY >= startY &&
        area.startZ <= endZ && area.endZ >= startZ
  }

  fun deduct(other: Area): List<Area> {
    if (!collision(other)) {
      return listOf(this)
    }
    return listOf(
      Area(startX, other.startX - 1, startY, other.startY - 1, startZ, other.startZ - 1),
      Area(other.startX.coerceAtLeast(startX), other.endX.coerceAtMost(endX), startY, other.startY - 1, startZ, other.startZ - 1),
      Area(other.endX + 1, endX, startY, other.startY - 1, startZ, other.startZ - 1),
      Area(startX, other.startX - 1, other.startY.coerceAtLeast(startY), other.endY.coerceAtMost(endY), startZ, other.startZ - 1),
      Area(other.startX.coerceAtLeast(startX), other.endX.coerceAtMost(endX), other.startY.coerceAtLeast(startY), other.endY.coerceAtMost(endY), startZ, other.startZ - 1),
      Area(other.endX + 1, endX, other.startY.coerceAtLeast(startY), other.endY.coerceAtMost(endY), startZ, other.startZ - 1),
      Area(startX, other.startX - 1, other.endY + 1, endY, startZ, other.startZ - 1),
      Area(other.startX.coerceAtLeast(startX), other.endX.coerceAtMost(endX), other.endY + 1, endY, startZ, other.startZ - 1),
      Area(other.endX + 1, endX, other.endY + 1, endY, startZ, other.startZ - 1),

      Area(startX, other.startX - 1, startY, other.startY - 1, other.startZ.coerceAtLeast(startZ), other.endZ.coerceAtMost(endZ)),
      Area(other.startX.coerceAtLeast(startX), other.endX.coerceAtMost(endX), startY, other.startY - 1, other.startZ.coerceAtLeast(startZ), other.endZ.coerceAtMost(endZ)),
      Area(other.endX + 1, endX, startY, other.startY - 1, other.startZ.coerceAtLeast(startZ), other.endZ.coerceAtMost(endZ)),
      Area(startX, other.startX - 1, other.startY.coerceAtLeast(startY), other.endY.coerceAtMost(endY), other.startZ.coerceAtLeast(startZ), other.endZ.coerceAtMost(endZ)),
      Area(other.startX.coerceAtLeast(startX), other.endX.coerceAtMost(endX), other.startY.coerceAtLeast(startY), other.endY.coerceAtMost(endY), other.startZ.coerceAtLeast(startZ), other.endZ.coerceAtMost(endZ)),
      Area(other.endX + 1, endX, other.startY.coerceAtLeast(startY), other.endY.coerceAtMost(endY), other.startZ.coerceAtLeast(startZ), other.endZ.coerceAtMost(endZ)),
      Area(startX, other.startX - 1, other.endY + 1, endY, other.startZ.coerceAtLeast(startZ), other.endZ.coerceAtMost(endZ)),
      Area(other.startX.coerceAtLeast(startX), other.endX.coerceAtMost(endX), other.endY + 1, endY, other.startZ.coerceAtLeast(startZ), other.endZ.coerceAtMost(endZ)),
      Area(other.endX + 1, endX, other.endY + 1, endY, other.startZ.coerceAtLeast(startZ), other.endZ.coerceAtMost(endZ)),

      Area(startX, other.startX - 1, startY, other.startY - 1, other.endZ + 1, endZ),
      Area(other.startX.coerceAtLeast(startX), other.endX.coerceAtMost(endX), startY, other.startY - 1, other.endZ + 1, endZ),
      Area(other.endX + 1, endX, startY, other.startY - 1, other.endZ + 1, endZ),
      Area(startX, other.startX - 1, other.startY.coerceAtLeast(startY), other.endY.coerceAtMost(endY), other.endZ + 1, endZ),
      Area(other.startX.coerceAtLeast(startX), other.endX.coerceAtMost(endX), other.startY.coerceAtLeast(startY), other.endY.coerceAtMost(endY), other.endZ + 1, endZ),
      Area(other.endX + 1, endX, other.startY.coerceAtLeast(startY), other.endY.coerceAtMost(endY), other.endZ + 1, endZ),
      Area(startX, other.startX - 1, other.endY + 1, endY, other.endZ + 1, endZ),
      Area(other.startX.coerceAtLeast(startX), other.endX.coerceAtMost(endX), other.endY + 1, endY, other.endZ + 1, endZ),
      Area(other.endX + 1, endX, other.endY + 1, endY, other.endZ + 1, endZ),
    )
      .filter { it.isValid() }
      .filterNot { it.collision(other) }
  }

  private fun isValid(): Boolean {
    return startX <= endX && startY <= endY && startZ <= endZ
  }
}

data class Cube(val area: Area, val value: Int) {

}
