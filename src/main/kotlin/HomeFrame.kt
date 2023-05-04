import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import javax.swing.Box
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

class HomeFrame : JFrame("My first GUI") {

    private val w = 150

    init {
        layout = BorderLayout()
        setSize(700,350)
//        size = Dimension(700, 350)
        isVisible = true
        isResizable = false
        add(JPanel().apply {
            layout = GridLayout(3, 1)
            background = this.background.brighter()
            add(JPanel().apply {
                background = Color.LIGHT_GRAY
                preferredSize = Dimension(w, 25)
                add(JLabel("Welcome").apply {

                })
            }, BorderLayout.WEST)


            add(Box.createVerticalGlue())
        }, BorderLayout.WEST)


        defaultCloseOperation = EXIT_ON_CLOSE
    }

//    init {
////        layout = FlowLayout(FlowLayout.LEFT, 0, 2)
//        layout = BorderLayout()
//        size = Dimension(600, 600)
//        background = this.background.brighter()
//        isResizable = false
//        isVisible = true
//
//        add(JPanel().apply {
//            size = Dimension(300, 100)
//            isResizable = false
//            layout = BoxLayout(this, BoxLayout.Y_AXIS)
//            background = Color.LIGHT_GRAY
//            add(Button())
//            add(Button())
//            add(Button())
//
//            add(Box.createVerticalGlue())
//        }, BorderLayout.WEST)
//
//        defaultCloseOperation = EXIT_ON_CLOSE
//    }
}