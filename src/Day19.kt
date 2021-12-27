import kotlin.math.absoluteValue

object Day19 {

  fun part1(input: List<ScannerData>): Int {
    val absolutePositions = getAbsolutePositions(input)
    return absolutePositions.values.flatten().distinct().count()
  }

  private fun getAbsolutePositions(input: List<ScannerData>): Map<ScannerData, List<Position>> {
    val transformations = mutableMapOf<Pair<ScannerData, ScannerData>, PositionTransformation>()
    for (i in input.indices) {
      for (j in (i + 1) until input.size) {
        val transformation = input[i].tryResolve(input[j])
        if (transformation != null) {
          transformations[Pair(input[i], input[j])] = transformation
          transformations[Pair(input[j], input[i])] = input[j].tryResolve(input[i])!!
        }
      }
    }
    val absoluteTransformations = mutableMapOf<ScannerData, PositionTransformation>()
    absoluteTransformations[input[0]] = { it }
    while (absoluteTransformations.size < input.size) {
      val next = transformations.keys.filter { absoluteTransformations.containsKey(it.first) }
        .filterNot { absoluteTransformations.containsKey(it.second) }
      check(next.isNotEmpty())
      next.forEach { key ->
        val nextTransformation = transformations.getValue(key)
        absoluteTransformations[key.second] =
          { absoluteTransformations.getValue(key.first).invoke(nextTransformation.invoke(it)) }
      }
    }
    return input.associateBy({ it },
      { scanner -> scanner.beaconPositions.map { absoluteTransformations.getValue(scanner).invoke(it) } })
  }

  fun part2(input: List<ScannerData>): Int {
    input.forEach { it.beaconPositions.add(Position(0, 0, 0)) }
    val scannerCoordinates = getAbsolutePositions(input)
      .values
      .map { it.last() }
    return scannerCoordinates.flatMap { from -> scannerCoordinates.map { it.manhatanDistanceTo(from) } }
      .maxOf { it }
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    val scanners = mutableListOf(ScannerData("unknown"))
    input.forEach { line ->
      if (line.startsWith("---")) {
        scanners.last().name = line
      } else if (line.isEmpty()) {
        scanners.add(ScannerData("unknown"))
      } else {
        val abc = line.split(",").map { it.toInt() }
        scanners.last().beaconPositions.add(Position(abc[0], abc[1], abc[2]))
      }
    }
    println(part1(scanners))
    println(part2(scanners))
  }
}

typealias PositionTransformation = (Position) -> Position

data class ScannerData(var name: String) {
  val beaconPositions = mutableListOf<Position>()

  fun tryResolve(scanner: ScannerData): PositionTransformation? {
    return scanner.beaconPositions.flatMap { beacon ->
      val distances = scanner.beaconPositions.map { it.distanceSquareTo(beacon) }
      val knownDistancesByBeacon = beaconPositions.map { absolutePosition ->
        beaconPositions.map { it.distanceSquareTo(absolutePosition) }.toSet()
      }
      val candidatePositions = knownDistancesByBeacon.mapIndexedNotNull { i, knownDistances ->
        val matchedDistances = distances.count { knownDistances.contains(it) }
        if (matchedDistances >= 12) {
          beaconPositions[i]
        } else {
          null
        }
      }
      val result = mutableListOf<Pair<PositionTransformation, Int>>()
      for (candidatePosition in candidatePositions) {
        val transformations: List<PositionTransformation> = rotations.map { rotation ->
          val afterRotation = rotation.invoke(beacon)
          val deltaA = candidatePosition.a - afterRotation.a
          val deltaB = candidatePosition.b - afterRotation.b
          val deltaC = candidatePosition.c - afterRotation.c
          {
            val item = rotation.invoke(it)
            Position(item.a + deltaA, item.b + deltaB, item.c + deltaC)
          }
        }
        result.addAll(transformations.map { transform ->
          Pair(transform, scanner.beaconPositions.map { transform.invoke(it) }.count { beaconPositions.contains(it) })
        })
      }
      result
    }
      .filter { it.second >= 12 }
      .maxByOrNull { it.second }?.first
  }
}

data class Position(val a: Int, val b: Int, val c: Int) {

  fun distanceSquareTo(other: Position): Int {
    return (a - other.a) * (a - other.a) + (b - other.b) * (b - other.b) + (c - other.c) * (c - other.c)
  }

  fun manhatanDistanceTo(other: Position): Int {
    return (a - other.a).absoluteValue + (b - other.b).absoluteValue + (c - other.c).absoluteValue
  }
}

val rotations = listOf<PositionTransformation>(
  { Position(it.a, it.b, it.c) },
  { Position(it.a, it.b, -it.c) },
  { Position(it.a, -it.b, it.c) },
  { Position(it.a, -it.b, -it.c) },
  { Position(-it.a, it.b, it.c) },
  { Position(-it.a, it.b, -it.c) },
  { Position(-it.a, -it.b, it.c) },
  { Position(-it.a, -it.b, -it.c) },
  { Position(it.a, it.c, it.b) },
  { Position(it.a, it.c, -it.b) },
  { Position(it.a, -it.c, it.b) },
  { Position(it.a, -it.c, -it.b) },
  { Position(-it.a, it.c, it.b) },
  { Position(-it.a, it.c, -it.b) },
  { Position(-it.a, -it.c, it.b) },
  { Position(-it.a, -it.c, -it.b) },
  { Position(it.c, it.a, it.b) },
  { Position(it.c, it.a, -it.b) },
  { Position(it.c, -it.a, it.b) },
  { Position(it.c, -it.a, -it.b) },
  { Position(-it.c, it.a, it.b) },
  { Position(-it.c, it.a, -it.b) },
  { Position(-it.c, -it.a, it.b) },
  { Position(-it.c, -it.a, -it.b) },
  { Position(it.c, it.b, it.a) },
  { Position(it.c, it.b, -it.a) },
  { Position(it.c, -it.b, it.a) },
  { Position(it.c, -it.b, -it.a) },
  { Position(-it.c, it.b, it.a) },
  { Position(-it.c, it.b, -it.a) },
  { Position(-it.c, -it.b, it.a) },
  { Position(-it.c, -it.b, -it.a) },
  { Position(it.b, it.a, it.c) },
  { Position(it.b, it.a, -it.c) },
  { Position(it.b, -it.a, it.c) },
  { Position(it.b, -it.a, -it.c) },
  { Position(-it.b, it.a, it.c) },
  { Position(-it.b, it.a, -it.c) },
  { Position(-it.b, -it.a, it.c) },
  { Position(-it.b, -it.a, -it.c) },
  { Position(it.b, it.c, it.a) },
  { Position(it.b, it.c, -it.a) },
  { Position(it.b, -it.c, it.a) },
  { Position(it.b, -it.c, -it.a) },
  { Position(-it.b, it.c, it.a) },
  { Position(-it.b, it.c, -it.a) },
  { Position(-it.b, -it.c, it.a) },
  { Position(-it.b, -it.c, -it.a) },
)