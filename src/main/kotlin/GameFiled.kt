class GameFiled(val width: Int = 20, val height: Int = 20) {

    private val defaultSquareSize = 20

    var currentSquareSize = defaultSquareSize

    private val field: List<Ceil> = (0 until (width + 2) * (height + 2)).map {
        Ceil(it / (width + 2), it % (width + 2))
    }

    private fun getCeil(x: Int, y: Int): Ceil = field[y * (width + 2) + x]

    inner class Ceil(val x: Int, val y: Int) {
        var isAlive = false
        var willLive = false
        var winStreak: Int = 0

        private fun getNeighbours(): List<Ceil> {
            return if (this.x == 0 || this.x == width || this.y == 0 || this.y == height) emptyList()
            else GlobalVariables.ceilNeighbours.map { getCeil(it.first + this.x, it.second + this.y) }
        }

        private fun countAliveNeighbours() = this.getNeighbours().count { it.isAlive }

        fun changeState() {
            willLive =
                this.countAliveNeighbours() in if (isAlive) GlobalVariables.needToSurvive else GlobalVariables.needToBurn
        }

        fun isInFiled() = this.x in (1..width) && this.y in (1..height)

    }

    private fun isField(ceil: Ceil): Boolean = (ceil.x in 0..width && ceil.y in 0..height)

    fun makeOneMove() {
        field.filter { it.isInFiled() }.forEach {
            it.changeState()
        }
        field.forEach {
            it.isAlive = it.willLive
        }
    }

    fun makeMoves(count: Int) {
        repeat(count) {
            makeOneMove()
        }
    }



}
