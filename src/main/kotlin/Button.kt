//import org.jetbrains.skija.Paint
//import org.jetbrains.skija.Rect
import java.awt.Dimension
//import java.io.File
//import java.io.File
import javax.swing.JButton
//import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane

//import javax.swing.filechooser.FileNameExtensionFilter

abstract class MyButton(label: String, val game: GameFiled) : JButton(label) {
    init {
        this.addActionListener {
            this.action()
        }
        preferredSize = Dimension(120, 50)
        background = background.darker()
        isBorderPainted = false
        isFocusPainted = false
    }

    abstract fun action()

}

class OneMoveButton(game: GameFiled) : MyButton("Make one move", game) {
    override fun action() {
        game.makeOneMove()
//        chooseFile()
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

class MakeAnyMovesButton(game: GameFiled) : MyButton("Make moves", game) {
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

enum class Rule {
    SURVIVE, BORN
}

class ChangeRulesButton(game: GameFiled) : MyButton("Change rules", game) {
    override fun action() {
        change(Rule.BORN)
        change(Rule.SURVIVE)
    }

    private fun change(whatChange: Rule) {
        if (whatChange != Rule.SURVIVE && whatChange != Rule.BORN)
            return
        val result = JOptionPane.showConfirmDialog(null, "Do you want change rules to ${whatChange}?")
        if (result == JOptionPane.YES_OPTION) {
            val string = JOptionPane.showInputDialog(
                null,
                "Write need neighbours to $whatChange by ' ' (bigger when 0 and less when 8):"
            )
            val newRule = parseStringToInt(string) ?: return
            when (whatChange) {
                Rule.SURVIVE -> GlobalVariables.needToSurvive = newRule
                else -> GlobalVariables.needToBorn = newRule
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

class AddNewColorButton(game: GameFiled) : MyButton("Configure colors", game) {
    override fun action() {
        val res = JOptionPane.showInputDialog(
            null,
            "Write how many colors do you want use:"
        ) ?: return
        if (res.toIntOrNull() == null)
            showError("Incorrect number")
        Colors.setColorCount(res.toIntOrNull()!!)
    }
}

class ChooseColorButton(game: GameFiled) : MyButton("Choose color", game) {
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
        saveWithChoose(game)
    }
}

class LoadGame(game: GameFiled) : MyButton("Load game", game) {
    override fun action() {
        loadWithChoose(game)
    }

}

fun showError(message: String) {
    JOptionPane.showMessageDialog(null, message)
}