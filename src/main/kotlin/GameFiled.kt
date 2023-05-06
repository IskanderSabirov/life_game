class GameFiled(val width: Int = 20, val height: Int = 20) {

    private val defaultSquareSize = 20
    var currentSquareSize = defaultSquareSize

    var cornerX = 1

    var cornerY = 1

    private val cellNeighbours = listOf(
        Pair(1, 0),
        Pair(1, -1),
        Pair(1, 1),
        Pair(0, 1),
        Pair(0, -1),
        Pair(-1, 0),
        Pair(-1, -1),
        Pair(-1, 1)
    )


    var isGoing = false


    private val field: List<Cell> = (0 until (width + 2) * (height + 2)).map {
        Cell(it % (width + 2), it / (width + 2))
    }

    inner class Cell(private val x: Int, private val y: Int, var color: Int = 0) {
        var nextMoveColor = 0
        var isAlive = false
        var willLive = false
        var winStreak: Int = 0

        private fun getNeighbours(): List<Cell> {
            return if (this.x == 0 || this.x == width + 1 || this.y == 0 || this.y == height + 1) emptyList()
            else cellNeighbours.map { getCell(it.first + this.x, it.second + this.y) }
        }

        private fun countAliveNeighboursForAlive(): Int {  // считает кол-во живых соседей такого же цвета
            return this.getNeighbours().count { it.isAlive && it.color == this.color }
        }

        private fun countAliveNeighboursForDead(): MutableMap<Int, Int> { // возвращает словарь типа <номер цвета, кол-во соседей>
            val result = mutableMapOf<Int, Int>()
            this.getNeighbours().forEach {
                if (it.color != Colors.deadColor()) {
                    if (!result.containsKey(it.color))
                        result[it.color] = 0
                    result[it.color] = result[it.color]!! + 1
                }
            }
            return result
        }

        fun changeState() {
            nextMoveColor = Colors.deadColor()
            if (isAlive) {
                willLive = countAliveNeighboursForAlive() in GlobalVariables.needToSurvive
                if (willLive)
                    nextMoveColor = color
            } else {
                willLive = false
                countAliveNeighboursForDead().forEach {
                    if (it.value in GlobalVariables.needToBurn) {
                        nextMoveColor = it.key
                        willLive = true
                    }
                }
            }

        }

        fun isInFiled() = this.x in (1..width) && this.y in (1..height)

    }

    fun getCell(x: Int, y: Int): Cell = field[y * (width + 2) + x]

    fun makeOneMove() {

        field.filter { it.isInFiled() }.forEach {
            it.changeState()
        }
        field.filter { it.isInFiled() }.forEach {
            if (it.isAlive && it.willLive)
                it.winStreak++
            else
                it.winStreak = 0
            it.isAlive = it.willLive
            it.color = it.nextMoveColor
        }

//        Thread.sleep(100)
    }


    fun makeMoves(count: Int) {
        repeat(count) {
            makeOneMove()
//            Thread.sleep(100)
        }
    }

    fun generateField() {
        field.filter { it.isInFiled() }.forEach {
            it.isAlive = (0..10).random() >= 7
            if (it.isAlive)
                it.color = Colors.getRandomColor()
        }
    }

    fun clearField() {
        field.forEach {
            it.isAlive = false
            it.winStreak = 0
            it.color = Colors.deadColor()
        }
    }


    fun contains(x: Int, y: Int): Boolean {
        return (x in (1..width) && y in (1..height))
    }


}
