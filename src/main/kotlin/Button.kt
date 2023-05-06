import java.awt.Dimension
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
            JOptionPane.showMessageDialog(frame, "Input is incorrect")
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
                JOptionPane.showMessageDialog(null, "Incorrect data to change rules")
                return null
            }
            result.add(int)
        }
        return result
    }

}






