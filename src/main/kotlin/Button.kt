import java.awt.Dimension
import javax.swing.JButton

abstract class MyButton(label: String) : JButton(label) {
    abstract fun action()

}

class Button : MyButton("First Button") {
    init {
        this.addActionListener {
            this.action()
        }
        preferredSize = Dimension(150, 50)
        background = background.darker()
        isBorderPainted = false
        isFocusPainted = false
    }

    override fun action() {
        println("pressed button!")
    }
}

