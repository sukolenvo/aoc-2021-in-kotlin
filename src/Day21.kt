import java.math.BigInteger

object Day21 {

  fun part1(pos1: Int, pos2: Int): Int {
    var nextDie = 1
    var player1Score = 0
    var player1Pos = pos1
    var player2Score = 0
    var player2Pos = pos2
    while (true) {
      var move = (nextDie++ + nextDie++ + nextDie++)
      player1Pos = (move + player1Pos - 1) % 10 + 1
      player1Score += player1Pos
      if (player1Score >= 1000) {
        return (nextDie - 1) * player2Score
      }
      move = (nextDie++ + nextDie++ + nextDie++)
      player2Pos = (move + player2Pos - 1) % 10 + 1
      player2Score += player2Pos
      if (player2Score >= 1000) {
        return (nextDie - 1) * player1Score
      }
    }
  }

  fun part2(pos1: Int, pos2: Int): BigInteger {
    val dieOutcomes = (1..3).flatMap { f -> (1..3).map { f + it } }.flatMap { t -> (1..3).map { t + it } }
      .groupingBy { it }
      .eachCount()
    val (winFirst, winSecond) = rollDice(0, pos1, 0, pos2, dieOutcomes, true)
    return winFirst.max(winSecond)
  }

  private fun rollDice(player1Score: Int, player1Pos: Int, player2Score: Int, player2Pos: Int, dieOutcomes: Map<Int, Int>, player1Turn: Boolean): Pair<BigInteger, BigInteger> {
    if (player1Score >= 21) {
      return Pair(BigInteger.ONE, BigInteger.ZERO)
    }
    if (player2Score >= 21) {
      return Pair(BigInteger.ZERO, BigInteger.ONE)
    }
    return if (player1Turn) {
      dieOutcomes.map { (score, times) ->
        val pos1 = (player1Pos + score - 1) % 10 + 1
        val wins = rollDice(player1Score + pos1, pos1, player2Score, player2Pos, dieOutcomes, false)
        Pair(wins.first.multiply(BigInteger.valueOf(times.toLong())), wins.second.multiply(BigInteger.valueOf(times.toLong())))
      }
        .reduce { f, s -> Pair(f.first.add(s.first), f.second.add(s.second))}
    } else {
      dieOutcomes.map { (score, times) ->
        val pos2 = (player2Pos + score - 1) % 10 + 1
        val wins = rollDice(player1Score, player1Pos, player2Score + pos2, pos2, dieOutcomes, true)
        Pair(wins.first.multiply(BigInteger.valueOf(times.toLong())), wins.second.multiply(BigInteger.valueOf(times.toLong())))
      }
        .reduce { f, s -> Pair(f.first.add(s.first), f.second.add(s.second))}
    }
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    check(input.size == 2)
    val positions = input.map { it.replace(Regex(".* "), "") }.map { it.toInt() }
    println(part1(positions[0], positions[1]))
    println(part2(positions[0], positions[1]))
  }
}
