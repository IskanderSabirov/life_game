import org.jetbrains.skija.Paint
import org.jetbrains.skija.Rect
import java.awt.Dimension
import java.io.File
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JOptionPane

abstract class MyButton(label: String, val game: GameFiled) : JButton(label) {
    init {
        this.addActionListener {
            this.action()
        }
        preferredSize = Dimension(150, 50)
        background = background.darker()
        isBorderPainted = false
        isFocusPainted = false
    }

    abstract fun action()

}

class Button(game: GameFiled) : MyButton("First Button", game) {

    override fun action() {
        println("pressed button!")
    }
}

class OneMoveButton(game: GameFiled) : MyButton("Make one move", game) {
    override fun action() {
        game.makeOneMove()
    }
}

class GenerateFieldButton(game: GameFiled) : MyButton("Generate Field", game) {
    override fun action() {
        game.generateField()
    }
}


class ClearFieldButton(game: GameFiled) : MyButton("Clear Field", game) {
    override fun action() {
        game.clearField()
    }
}


class StartButton(game: GameFiled) : MyButton("Start", game) {
    override fun action() {
        game.isGoing = true
    }
}


class StopButton(game: GameFiled) : MyButton("Stop", game) {
    override fun action() {
        game.isGoing = false
    }
}

class MakeAnyMovesButton(game: GameFiled) : MyButton("Make some moves", game) {
    override fun action() {
        val frame = JFrame()
        val string = JOptionPane.showInputDialog(frame, "How many moves do you want?") ?: return
        val n = string.toIntOrNull()
        if (n == null)
            showError("Input is incorrect")
        else
            game.makeMoves(n)
    }
}


class ChangeRulesButton(game: GameFiled) : MyButton("Change rules", game) {
    override fun action() {
        change("survive")
        change("burn")
    }

    private fun change(whatChange: String) {
        if (whatChange != "survive" && whatChange != "burn")
            return
        val result = JOptionPane.showConfirmDialog(null, "Do you want change rules to $whatChange?")
        if (result == JOptionPane.YES_OPTION) {
            val string = JOptionPane.showInputDialog(
                null,
                "Write need neighbours to $whatChange by ' ' (bigger when 0 and less when 8):"
            )
            val newRule = parseStringToInt(string) ?: return
            when (whatChange) {
                "survive" -> GlobalVariables.needToSurvive = newRule
                else -> GlobalVariables.needToBurn = newRule
            }
        }
    }

    private fun parseStringToInt(string: String): MutableList<Int>? {
        val result = mutableListOf<Int>()
        string.split(" ").filter { it != "" }.forEach {
            val int = it.toIntOrNull()
            if (int == null || int <= 0 || int >= 9) {
                showError("Incorrect data to change rules")
                return null
            }
            result.add(int)
        }
        return result
    }

}

class AddNewColorButton(game: GameFiled) : MyButton("Add new color", game) {
    override fun action() {
        Colors.addNewColor()
    }
}


class ChooseColorButton(game: GameFiled) : MyButton("Choose current color", game) {
    override fun action() {
        val res = JOptionPane.showInputDialog(
            null,
            "Write chosen color (1 - ${Colors.colorsCount()}):"
        ) ?: return
        val result = res.toIntOrNull()
        if (result == null || result < 1 || result > Colors.colorsCount())
            showError("Incorrect data to change rules")

        GlobalVariables.currentColor = result!!

    }
}

class SaveGame(game: GameFiled) : MyButton("Save game", game) {
    override fun action() {
        val file = File(GlobalVariables.lastGame)

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
}

class LoadGame(game: GameFiled) : MyButton("Load game", game) {
    override fun action() {
        val file = File(GlobalVariables.lastGame)

        val lines = file.readLines()

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

        game.setFiled(sizes[0], sizes[1], cells, squareSize)

    }

    private fun splitSize(string: String): MutableList<Int>? {
        val answer = mutableListOf<Int>()
        string.split(" ").filter { it != "" }.forEach {
            val tmp = it.toIntOrNull() ?: return null
            answer.add(tmp)
        }
        if (answer.size != 2)
            return null
        return answer
    }

    private fun splitNeededTo(string: String): MutableList<Int>? {
        val answer = mutableListOf<Int>()
        string.split(" ").filter { it != "" }.forEach {
            val tmp = it.toIntOrNull() ?: return null
            if (tmp !in (1..8))
                return answer
            answer.add(tmp)
        }
        return answer
    }

}


fun showError(message: String) {
    JOptionPane.showMessageDialog(null, message)
}










