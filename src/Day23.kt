object Day23 {

  fun part1(map: List<Position>, amphipods: List<Amphipod>): Int {
    val occupied = HashMap(amphipods.associateBy { it.position })
    val leftToMove = amphipods.filterNot { it.isInPlace(occupied) }.sortedBy { it.getMoveCost() }
    move(map, leftToMove, occupied, 0)
    return result!!
  }

  var result: Int? = null
  private fun move(
    map: List<Position>,
    leftToMove: List<Amphipod>,
    occupied: HashMap<Position, Amphipod>,
    currentCost: Int
  ) {
    if (leftToMove.isEmpty()) {
      if (result == null || result!! > currentCost) {
        result = currentCost
      }
      return
    }
    for (amphipod in leftToMove) {
      val targetMove = amphipod.getTargetMove(occupied)
      if (targetMove != null) {
        val positionBefore = amphipod.position
        occupied.remove(amphipod.position)
        occupied[targetMove] = amphipod
        val moveCost = amphipod.moveTo(targetMove)
        move(map, leftToMove.filterNot { it == amphipod }, occupied, currentCost + moveCost)
        occupied.remove(amphipod.position)
        occupied[positionBefore] = amphipod
        amphipod.undoMove(positionBefore)
        return
      }
    }
    for (amphipod in leftToMove) {
      val possibleMoves = amphipod.getOutMoves(occupied)
      for (move in possibleMoves) {
        val from = amphipod.position
        val cost = amphipod.moveTo(move) + currentCost
        if (result == null || cost < result!!) {
          occupied.remove(from)
          occupied[move] = amphipod
          move(map, leftToMove, occupied, cost)
          occupied.remove(move)
          occupied[from] = amphipod
        }
        amphipod.undoMove(from)
      }
    }
  }

  fun part2(map: List<Position>, amphipods: List<Amphipod>): Int {
    result = null
    val mapPart2 = addPositionsPart2(map)
    val amphipodsPart2 = addAmphipodsPart2(mapPart2, amphipods)
    val occupied = HashMap(amphipodsPart2.associateBy { it.position })
    val leftToMove = amphipodsPart2.filterNot { it.isInPlace(occupied) }.sortedBy { it.getMoveCost() }
    move(mapPart2, leftToMove, occupied, 0)
    return result!!
  }

  private fun addPositionsPart2(map: List<Position>): List<Position> {
    val mapPart2 = map.toMutableList()
    // Map
    // #############
    // #0123456789A#
    // ###B#C#D#E###
    //   #F#G#H#I#
    //   #J#K#L#M#
    //   #N#O#P#Q#
    //   #########
    val j = Position("j")
    val k = Position("k")
    val l = Position("l")
    val m = Position("m")
    val n = Position("n")
    val o = Position("o")
    val p = Position("p")
    val q = Position("q")
    map.single { it.name == "f" }.addAdjustedNodes(j)
    map.single { it.name == "g" }.addAdjustedNodes(k)
    map.single { it.name == "h" }.addAdjustedNodes(l)
    map.single { it.name == "i" }.addAdjustedNodes(m)
    j.addAdjustedNodes(map.single { it.name == "f" }, n)
    k.addAdjustedNodes(map.single { it.name == "g" }, o)
    l.addAdjustedNodes(map.single { it.name == "h" }, p)
    m.addAdjustedNodes(map.single { it.name == "i" }, q)
    n.addAdjustedNodes(j)
    o.addAdjustedNodes(k)
    p.addAdjustedNodes(l)
    q.addAdjustedNodes(m)
    mapPart2.addAll(listOf(j, k, l, m, n, o, p, q))
    return mapPart2
  }

  private fun addAmphipodsPart2(map: List<Position>, amphipods: List<Amphipod>): List<Amphipod> {
    val amphipodsPart2 = amphipods.toMutableList()
    amphipods.single { it.position.name == "f" }.position = map.single { it.name == "n" }
    amphipods.single { it.position.name == "g" }.position = map.single { it.name == "o" }
    amphipods.single { it.position.name == "h" }.position = map.single { it.name == "p" }
    amphipods.single { it.position.name == "i" }.position = map.single { it.name == "q" }
    amphipodsPart2.add(Amphipod("D", map.single { it.name == "f" }))
    amphipodsPart2.add(Amphipod("C", map.single { it.name == "g" }))
    amphipodsPart2.add(Amphipod("B", map.single { it.name == "h" }))
    amphipodsPart2.add(Amphipod("A", map.single { it.name == "i" }))
    amphipodsPart2.add(Amphipod("D", map.single { it.name == "j" }))
    amphipodsPart2.add(Amphipod("B", map.single { it.name == "k" }))
    amphipodsPart2.add(Amphipod("A", map.single { it.name == "l" }))
    amphipodsPart2.add(Amphipod("C", map.single { it.name == "m" }))
    amphipodsPart2.forEach {
      it.targetPositions.clear()
      when (it.type) {
        "A" -> it.targetPositions.addAll(
          map.filter { it.name == "b" || it.name == "f" || it.name == "j" || it.name == "n" }.reversed()
        )
        "B" -> it.targetPositions.addAll(
          map.filter { it.name == "c" || it.name == "g" || it.name == "k" || it.name == "o" }.reversed()
        )
        "C" -> it.targetPositions.addAll(
          map.filter { it.name == "d" || it.name == "h" || it.name == "l" || it.name == "p" }.reversed()
        )
        "D" -> it.targetPositions.addAll(
          map.filter { it.name == "e" || it.name == "i" || it.name == "m" || it.name == "q" }.reversed()
        )
      }
    }
    for (from in map) {
      from.paths.clear()
      bfs(from)
    }
    return amphipodsPart2;
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val map = setupMap()
    val amphipods = setupAmphipods(input, map)
    println(part1(map, amphipods))
    println(part2(map, amphipods))
  }

  private fun setupMap(): List<Position> {
    // Map
    // #############
    // #0123456789A#
    // ###B#C#D#E###
    //   #F#G#H#I#
    //   #########
    val zero = Position("0")
    val one = Position("1")
    val two = Position("2")
    val three = Position("3")
    val four = Position("4")
    val five = Position("5")
    val six = Position("6")
    val seven = Position("7")
    val eight = Position("8")
    val nine = Position("9")
    val a = Position("a")
    val b = Position("b")
    val c = Position("c")
    val d = Position("d")
    val e = Position("e")
    val f = Position("f")
    val g = Position("g")
    val h = Position("h")
    val i = Position("i")
    zero.addAdjustedNodes(one)
    one.addAdjustedNodes(zero, two)
    two.addAdjustedNodes(one, three, b)
    three.addAdjustedNodes(two, four)
    four.addAdjustedNodes(three, five, c)
    five.addAdjustedNodes(four, six)
    six.addAdjustedNodes(five, seven, d)
    seven.addAdjustedNodes(six, eight)
    eight.addAdjustedNodes(seven, nine, e)
    nine.addAdjustedNodes(eight, a)
    a.addAdjustedNodes(nine)
    b.addAdjustedNodes(two, f)
    c.addAdjustedNodes(four, g)
    d.addAdjustedNodes(six, h)
    e.addAdjustedNodes(eight, i)
    f.addAdjustedNodes(b)
    g.addAdjustedNodes(c)
    h.addAdjustedNodes(d)
    i.addAdjustedNodes(e)
    val result = listOf(zero, one, two, three, four, five, six, seven, eight, nine, a, b, c, d, e, f, g, h, i)
    for (from in result) {
      bfs(from)
    }
    return result
  }

  private fun bfs(from: Position) {
    val queue = ArrayDeque<Position>()
    queue.addAll(from.adjustedNodes)
    from.adjustedNodes.forEach { from.paths[it.name] = listOf(it) }
    while (queue.isNotEmpty()) {
      val position = queue.removeFirst()
      for (adjustedNode in position.adjustedNodes) {
        if (!from.paths.containsKey(adjustedNode.name)) {
          from.paths[adjustedNode.name] = from.paths[position.name]!! + adjustedNode
          queue.add(adjustedNode)
        }
      }
    }
  }

  private fun setupAmphipods(input: List<String>, map: List<Position>): List<Amphipod> {
    val posB = map.single { it.name == "b" }
    val posC = map.single { it.name == "c" }
    val posD = map.single { it.name == "d" }
    val posE = map.single { it.name == "e" }
    val posF = map.single { it.name == "f" }
    val posG = map.single { it.name == "g" }
    val posH = map.single { it.name == "h" }
    val posI = map.single { it.name == "i" }
    val amphipodB = Amphipod(input[2][3].toString(), posB)
    val amphipodC = Amphipod(input[2][5].toString(), posC)
    val amphipodD = Amphipod(input[2][7].toString(), posD)
    val amphipodE = Amphipod(input[2][9].toString(), posE)
    val amphipodF = Amphipod(input[3][3].toString(), posF)
    val amphipodG = Amphipod(input[3][5].toString(), posG)
    val amphipodH = Amphipod(input[3][7].toString(), posH)
    val amphipodI = Amphipod(input[3][9].toString(), posI)
    val result = listOf(amphipodB, amphipodC, amphipodD, amphipodE, amphipodF, amphipodG, amphipodH, amphipodI)
    result.forEach {
      when (it.type) {
        "A" -> it.targetPositions.addAll(map.filter { it.name == "b" || it.name == "f" }.reversed())
        "B" -> it.targetPositions.addAll(map.filter { it.name == "c" || it.name == "g" }.reversed())
        "C" -> it.targetPositions.addAll(map.filter { it.name == "d" || it.name == "h" }.reversed())
        "D" -> it.targetPositions.addAll(map.filter { it.name == "e" || it.name == "i" }.reversed())
      }
    }
    return result
  }

  data class Position(val name: String) {
    val adjustedNodes = mutableListOf<Position>()
    val paths = mutableMapOf<String, List<Position>>()

    fun addAdjustedNodes(vararg nodes: Position) {
      nodes.forEach { adjustedNodes.add(it) }
    }

    fun distanceTo(position: Position): Int {
      return paths.getValue(position.name).size
    }
  }

  class Amphipod(val type: String, var position: Position, var walkedDistance: Int = 0) {
    val targetPositions = mutableListOf<Position>()

    fun getOutMoves(amphipods: Map<Position, Amphipod>): List<Position> {
      if (walkedDistance != 0) {
        return listOf()
      }
      return "013579a".toCharArray()
        .map { it.toString() }
        .map { position.paths.getValue(it) }
        .filter { it.none { amphipods.contains(it) } }
        .map { it.last() }
    }

    fun getTargetMove(amphipods: Map<Position, Amphipod>): Position? {
      for (targetPos in targetPositions) {
        val amphipod = amphipods[targetPos]
        if (amphipod == null) {
          val path = this.position.paths.getValue(targetPos.name)
          if (path.none { amphipods.contains(it) }) {
            return path.last()
          }
          return null
        }
        if (amphipod.type != this.type) {
          return null
        }
      }
      throw Exception("no room for $this")
    }

    private fun getPossibleTargetPosition(amphipods: Map<Position, Amphipod>): Position? {
      for (position in targetPositions) {
        val amphipod = amphipods[position]
        if (amphipod == null) {
          return position
        }
        if (amphipod.type != type) {
          return null
        }
        if (amphipod == this) {
          return null
        }
      }
      throw Exception("not enough room for $this")
    }

    fun moveTo(position: Position): Int {
      val distance = this.position.distanceTo(position)
      walkedDistance += distance
      this.position = position
      return distance * getMoveCost()
    }

    fun undoMove(position: Position) {
      val distance = this.position.distanceTo(position)
      walkedDistance -= distance
      this.position = position
    }

    fun isInPlace(amphipods: Map<Position, Amphipod>): Boolean {
      return targetPositions.map { amphipods[it] }.takeWhile { it?.type == this.type }.contains(this)
    }

    fun getMoveCost() = when (type) {
      "A" -> 1
      "B" -> 10
      "C" -> 100
      "D" -> 1000
      else -> throw Exception("Invalid type")
    }

    override fun toString() = "Amphipod($type): $position"
  }
}
