import java.io.File
import javax.swing.JFileChooser
//import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

fun saveGame(game: GameFiled, file: File) {

    if (!file.exists())
        return

    file.writeText("${game.width} ${game.height} \n")

    file.appendText("${game.currentSquareSize}\n")

    GlobalVariables.needToBurn.forEach {
        file.appendText("$it ")
    }
    file.appendText("\n")

    GlobalVariables.needToSurvive.forEach {
        file.appendText("$it ")
    }
    file.appendText("\n")

    file.appendText("${Colors.colorsCount()}\n")

    game.saveGame(file)
}

fun saveRules(game: GameFiled) {
    val file = File(GlobalVariables.lastRules)
    file.writeText("${game.currentSquareSize}\n")

    GlobalVariables.needToBurn.forEach {
        file.appendText("$it ")
    }
    file.appendText("\n")

    GlobalVariables.needToSurvive.forEach {
        file.appendText("$it ")
    }
    file.appendText("\n")
}

fun loadRules(game: GameFiled) {
    val lines = File(GlobalVariables.lastRules).readLines()
    if (lines.size != 3) {
        showError("Incorrect lines count in last game rules file")
        return
    }
    val squareSize = lines[0].toIntOrNull()
    val toBurn = splitNeededTo(lines[1])
    val toSurvive = splitNeededTo(lines[2])
    if (squareSize == null || toBurn == null || toSurvive == null) {
        showError("Incorrect data in last game rules")
        return
    }
    game.currentSquareSize = squareSize
    GlobalVariables.needToBurn = toBurn
    GlobalVariables.needToSurvive = toSurvive

}

fun loadGame(game: GameFiled, fileName: File) {

    val lines = fileName.readLines()

    if (lines.size != 6) {
        showError("Incorrect data in file to download game")
        return
    }

    val sizes = splitSize(lines[0])
    val squareSize = lines[1].toIntOrNull()
    val toBurn = splitNeededTo(lines[2])
    val toSurvive = splitNeededTo(lines[3])
    val colorCount = lines[4].toIntOrNull()

    if (squareSize == null || sizes == null || toBurn == null || toSurvive == null || colorCount == null) {
        showError("Incorrect data in file`s information to download file")
        return
    }

    val cells = mutableListOf<Int>()

    lines[5].split(" ").filter { it != "" }.forEach {
        val tmp = it.toIntOrNull()
        if (tmp == null || tmp !in (0..colorCount)) {
            showError("Incorrect data about cell in file")
            return
        }
        cells.add(tmp)
    }

    if (cells.size != (sizes[0] + 2) * (sizes[1] + 2)) {
        showError("Incorrect count of cells in file")
        return
    }

    Colors.setColorCount(colorCount)

    game.currentSquareSize = squareSize
    GlobalVariables.needToBurn = toBurn
    GlobalVariables.needToSurvive = toSurvive
    game.setFiled(sizes[0], sizes[1], cells)

}

fun splitSize(string: String): MutableList<Int>? {
    val answer = mutableListOf<Int>()
    string.split(" ").filter { it != "" }.forEach {
        val tmp = it.toIntOrNull() ?: return null
        answer.add(tmp)
    }
    if (answer.size != 2)
        return null
    return answer
}

fun splitNeededTo(string: String): MutableList<Int>? {
    val answer = mutableListOf<Int>()
    string.split(" ").filter { it != "" }.forEach {
        val tmp = it.toIntOrNull() ?: return null
        if (tmp !in (1..8))
            return answer
        answer.add(tmp)
    }
    return answer
}

fun chooseFile(): File? {
    val chooser = JFileChooser("Choosing game`s file")
    val filter = FileNameExtensionFilter(
        "*.txt files", "txt"
    )
    chooser.fileFilter = filter
    val ret = chooser.showDialog(null, "Choose file")
    if (ret == JFileChooser.APPROVE_OPTION) {
        val file = chooser.selectedFile
        if (file.extension != "txt")
            return null
//        println("$file")
        return file

    }
    return null
}

fun loadWithChoose(game: GameFiled) {
    val file = chooseFile()
    if (file == null) {
        showError("Incorrect file to save or download")
        return
    }
    loadGame(game, file)
}

fun saveWithChoose(game: GameFiled) {
    val file = chooseFile()
    if (file == null) {
        showError("Incorrect file to save or download")
        return
    }
    saveGame(game, file)
}

