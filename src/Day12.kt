object Day12 {

  private val visitedCaves = mutableSetOf<Cave>()
  private var visitedTwice: Cave? = null

  fun part1(input: Collection<Cave>): Int {
    return visit(input.single { it.isStartCave() })
  }

  private fun visit(cave: Cave): Int {
    if (cave.isEndCave()) {
      return 1
    }
    if (cave.isSmallCave() && visitedCaves.contains(cave)) {
      return 0
    }
    visitedCaves.add(cave)
    val result = cave.connectedCaves.sumOf { visit(it) }
    visitedCaves.remove(cave)
    return result
  }

  fun part2(input: Collection<Cave>): Int {
    return visit2(input.single { it.isStartCave() })
  }

  private fun visit2(cave: Cave): Int {
    if (cave.isEndCave()) {
      return 1
    }
    var unvisitTwice = false
    if (cave.isSmallCave() && visitedCaves.contains(cave)) {
      if (visitedTwice == null) {
        visitedTwice = cave
        unvisitTwice = true
      } else {
        return 0
      }
    }
    visitedCaves.add(cave)
    val result = cave.connectedCaves.filterNot { it.isStartCave() }.sumOf { visit2(it) }
    if (unvisitTwice) {
      visitedTwice = null
    } else {
      visitedCaves.remove(cave)
    }
    return result
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val caves = mutableMapOf<String, Cave>()
    input.forEach {
      val path = it.split("-")
      check(path.size == 2) { "path should connect two caves" }
      val l = caves.getOrPut(path[0]) { Cave(path[0]) }
      val r = caves.getOrPut(path[1]) { Cave(path[1]) }
      l.connectedCaves.add(r)
      r.connectedCaves.add(l)
    }
    check(caves.containsKey("start")) { "start cave required" }
    check(caves.containsKey("end")) { "end cave required" }
    println(part1(caves.values))
    println(part2(caves.values))
  }
}

data class Cave(val name: String) {

  val connectedCaves: MutableList<Cave> = mutableListOf()

  fun isStartCave() = name == "start"

  fun isEndCave() = name == "end"

  fun isSmallCave() = name[0].isLowerCase()
}
