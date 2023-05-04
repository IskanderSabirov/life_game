class GlobalVariables {
    companion object {

        var cornerX = 1

        var cornerY = 1

        val minimumSquareSize = 5

        val CellNeighbours = listOf(
            Pair(1, 0),
            Pair(1, -1),
            Pair(1, 1),
            Pair(0, 1),
            Pair(0, -1),
            Pair(-1, 0),
            Pair(-1, -1),
            Pair(-1, 1)
        )

        var needToSurvive = listOf(2, 3)
        var needToBurn = listOf(3)

    }
}