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

class OneMoveButton(label: String, game: GameFiled) : MyButton(label, game) {
    override fun action() {
        game.makeOneMove()
    }
}

class GenerateFieldButton(label: String, game: GameFiled) : MyButton(label, game) {
    override fun action() {
        game.generateField()
    }
}


class ClearFieldButton(label: String, game: GameFiled) : MyButton(label, game) {
    override fun action() {
        game.clearField()
    }
}


class StartButton(label: String, game: GameFiled) : MyButton(label, game) {
    override fun action() {
        game.isGoing = true
    }
}


class StopButton(label: String, game: GameFiled) : MyButton(label, game) {
    override fun action() {
        game.isGoing = false
    }
}




