import java.awt.Dimension
import javax.swing.JButton

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




