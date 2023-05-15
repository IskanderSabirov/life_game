import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

fun saveGame(game: GameFiled, file: File) {

    if (!file.exists())
        return

    val text = StringBuilder()

    text.append("${game.width} ${game.height} \n")

    text.append("${game.currentSquareSize}\n")

    Rules.needToBorn.forEach {
        text.append("$it ")
    }
    text.append("\n")

    Rules.needToSurvive.forEach {
        text.append("$it ")
    }
    text.append("\n")

    text.append("${Colors.colorsCount()}\n")

    file.writeText(text.toString())

    game.saveGame(file)
}

fun saveRules(game: GameFiled) {
    val file = File(Rules.lastRules)
    val text = StringBuilder()

    text.append("${game.currentSquareSize}\n")


    Rules.needToBorn.forEach {
        text.append("$it ")
    }

    text.append("\n")

    Rules.needToSurvive.forEach {
        text.append("$it ")
    }

    text.append("\n")

    file.writeText(text.toString())
}

fun loadRules(game: GameFiled) {
    val lines = File(Rules.lastRules).readLines()
    if (lines.size != 3) {
        showError("Incorrect lines count in last game rules file")
        return
    }
    val squareSize = lines[0].toIntOrNull()
    val toBorn = splitNeededTo(lines[1])
    val toSurvive = splitNeededTo(lines[2])
    if (squareSize == null || toBorn == null || toSurvive == null) {
        showError("Incorrect data in last game rules")
        return
    }
    game.currentSquareSize = squareSize
    Rules.needToBorn = toBorn
    Rules.needToSurvive = toSurvive

}

fun loadGame(game: GameFiled, fileName: File) {

    val lines = fileName.readLines()

    if (lines.size != 6) {
        showError("Incorrect data in file to load game")
        return
    }

    val sizes = splitSize(lines[0])
    val squareSize = lines[1].toIntOrNull()
    val toBorn = splitNeededTo(lines[2])
    val toSurvive = splitNeededTo(lines[3])
    val colorCount = lines[4].toIntOrNull()

    if (squareSize == null || sizes == null || toBorn == null || toSurvive == null || colorCount == null) {
        showError("Incorrect data in file`s information to load file")
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
    Rules.needToBorn = toBorn
    Rules.needToSurvive = toSurvive
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

    val answer = string.split(" ").filter { it != "" }.map { it.toIntOrNull() }
    if (answer.any { it == null || it !in (1..8) }) {
        return null
    }

    return (answer as MutableList<Int>) // среда не видит что есть проверки всех элемнтов на null
}

fun chooseFile(): File? {
    val chooser = JFileChooser("Choosing game`s file")
    val filter = FileNameExtensionFilter(
        "*.txt files", "txt"
    )
    chooser.fileFilter = filter
    val ret = chooser.showDialog(null, "Choose file")
    if (ret == JFileChooser.APPROVE_OPTION) {
        return chooser.selectedFile
    }
    return null
}

fun loadWithChoose(game: GameFiled) {
    val file = chooseFile()
    if (file == null) {
        showError("Incorrect file to save or load")
        return
    }
    loadGame(game, file)
}

fun saveWithChoose(game: GameFiled) {
    val file = chooseFile()
    if (file == null) {
        showError("Incorrect file to save or load")
        return
    }
    saveGame(game, file)
}

