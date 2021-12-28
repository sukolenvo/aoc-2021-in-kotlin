object Day20 {

  fun part1(algorithm: Array<Int>, image: InfiniteImage): Int {
    return processImage(algorithm, processImage(algorithm, image)).image.flatten().count { it == 1 }
  }

  private fun processImage(algorithm: Array<Int>, image: InfiniteImage): InfiniteImage {
    val result = Array(image.image.size + 2) { Array(image.image[0].size + 2) { 0 } }
    for (i in result.indices) {
      for (j in result[0].indices) {
        val index = getNumber(i - 2, j - 2, image).shl(6) + getNumber(i - 1, j - 2, image).shl(3) + getNumber(i, j - 2, image)
        result[i][j] = algorithm[index]
      }
    }
    return InfiniteImage().apply {
      this.image = result
      padding = algorithm[image.padding * 511]
    }
  }

  private fun getNumber(startI: Int, startJ: Int, image: InfiniteImage): Int {
    if (startI < 0 || startI >= image.image.size) {
      return image.padding * 7
    }
    val values = (startJ..(startJ + 2)).map {
      if (it < 0 || it >= image.image[0].size) {
        image.padding
      } else {
        image.image[startI][it]
      }
    }
    return values[0] * 4 + values[1] * 2 + values[2]
  }

  fun part2(algorithm: Array<Int>, image: InfiniteImage): Int {
    var image = image
    for (i in 1..50) {
      image = processImage(algorithm, image)
    }
    return image.image.flatten().count { it == 1 }
  }

  @JvmStatic
  fun main(args: Array<String>) {
    val input = readInput(javaClass.name)
    check(input[0].length == 512)
    val algorithm = input[0].map {
      when (it) {
        '#' -> 1
        '.' -> 0
        else -> throw Exception("Invalid algo")
      }
    }.toTypedArray()

    val image = input.drop(2).map { line ->
      line.map {
        when (it) {
          '#' -> 1
          '.' -> 0
          else -> throw Exception("Invalid input")
        }
      }.toTypedArray()
    }.toTypedArray()
    println(part1(algorithm, InfiniteImage().apply {
      this.image = image
    }))
    println(part2(algorithm, InfiniteImage().apply {
      this.image = image
    }))
  }
}


class InfiniteImage {
  lateinit var image: Array<Array<Int>>
  var padding: Int = 0
}