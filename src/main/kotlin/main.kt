import com.formdev.flatlaf.FlatLightLaf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
import org.jetbrains.skija.*
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkiaRenderer
import org.jetbrains.skiko.SkiaWindow
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.*
import java.awt.event.MouseMotionAdapter
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.WindowConstants
import kotlin.concurrent.thread

const val width = 20 * 20
const val height = 20 * 20
const val squareSize = 20

fun main() {
    FlatLightLaf.setup()
    createWindow("Life Game", GameFiled(1024, 1024))
}

fun createWindow(title: String, game: GameFiled) = runBlocking(Dispatchers.Swing) {
    val window = SkiaWindow().apply {
        setLocationRelativeTo(null)
        preferredSize = Dimension(800, 800)
        isResizable = false
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        this.title = title
    }

    window.layer.renderer = Renderer(window.layer, game)

    window.add(JPanel().apply {
        layout = GridLayout(5, 1, 3, 3)
        add(OneMoveButton(game))
        add(GenerateFieldButton(game))
        add(ClearFieldButton(game))
        add(StartButton(game))
        add(StopButton(game))
    }, BorderLayout.WEST)

    window.layer.addMouseMotionListener(MouseMotionAdapter)

    val mouseListener = object : MouseListener {
        override fun mouseClicked(e: MouseEvent) {
            pressed(game)
//            println(window.width)
        }

        override fun mouseExited(e: MouseEvent) {}
        override fun mousePressed(e: MouseEvent) {}
        override fun mouseEntered(e: MouseEvent) {}
        override fun mouseReleased(e: MouseEvent) {}
    }

    val keyListener = object : KeyListener {
        override fun keyTyped(e: KeyEvent) {}
        override fun keyReleased(e: KeyEvent) {}
        override fun keyPressed(e: KeyEvent) {
            when (e.keyCode) {
                KeyEvent.VK_W -> if (GlobalVariables.cornerY > 1) GlobalVariables.cornerY--
                KeyEvent.VK_S -> if (GlobalVariables.cornerY < game.height) GlobalVariables.cornerY++
                KeyEvent.VK_A -> if (GlobalVariables.cornerX > 1) GlobalVariables.cornerX--
                KeyEvent.VK_D -> if (GlobalVariables.cornerX < game.width) GlobalVariables.cornerX++
            }
        }
    }


    window.layer.addMouseListener(mouseListener)
    window.layer.addKeyListener(keyListener)
    window.setSize(width, height)
    window.pack()
    window.layer.awaitRedraw()
    window.isVisible = true
}

class Renderer(val layer: SkiaLayer, var game: GameFiled) : SkiaRenderer {

    override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {

        val contentScale = layer.contentScale
        canvas.scale(contentScale, contentScale)

        drawFiledLines(canvas, game)

        drawFieldSquares(canvas, game)

        drawCellWinStreak(canvas, game)

        if (game.isGoing) {
            game.makeOneMove()
//            Thread.sleep(100)
        }
        layer.needRedraw()
    }

}

object State {
    var mouseX = 0f
    var mouseY = 0f
}

object MouseMotionAdapter : MouseMotionAdapter() {
    override fun mouseMoved(event: MouseEvent) {
        State.mouseX = event.x.toFloat()
        State.mouseY = event.y.toFloat()
    }
}

fun drawCellWinStreak(canvas: Canvas, game: GameFiled) {
    val paint = Paint().apply {
        color = Color.makeRGB(0, 0, 0)
        mode = PaintMode.STROKE
        strokeWidth = 1f
    }
    val typeface = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf")
    val font = Font(typeface, 13f)

    val x: Int = (State.mouseX / squareSize).toInt() + GlobalVariables.cornerX
    val y: Int = (State.mouseY / squareSize).toInt() + GlobalVariables.cornerY

    if (!game.contains(x, y) || !game.getCell(x, y).isAlive) return

    val text = "alive: ${game.getCell(x, y).winStreak} moves"
    canvas.drawString(text, State.mouseX, State.mouseY, font, paint)
}


fun drawFiledLines(canvas: Canvas, game: GameFiled) {
    val paint = Paint().apply {
        color = Color.makeRGB(0, 0, 0)
        mode = PaintMode.STROKE
        strokeWidth = 1f
    }
    for (i in 1..game.width - GlobalVariables.cornerX + 1) {
        canvas.drawLine(
            i.toFloat() * game.currentSquareSize,
            0F,
            i.toFloat() * game.currentSquareSize,
            game.height * game.currentSquareSize.toFloat() - (GlobalVariables.cornerY - 1) * game.currentSquareSize.toFloat(),
            paint
        )
    }

    for (i in 1..game.height - GlobalVariables.cornerY + 1) {
        canvas.drawLine(
            0F,
            i.toFloat() * game.currentSquareSize,
            game.width * game.currentSquareSize.toFloat() - (GlobalVariables.cornerX - 1) * game.currentSquareSize.toFloat(),
            i.toFloat() * game.currentSquareSize,
            paint
        )
    }
}

fun drawFieldSquares(canvas: Canvas, game: GameFiled) {
    val paint = Paint().apply {
        color = Color.makeRGB(123, 123, 123)
    }

    for (x in GlobalVariables.cornerX..game.width) for (y in GlobalVariables.cornerY..game.height) if (game.contains(
            x,
            y
        ) && game.getCell(x, y).isAlive
    ) {
        canvas.drawRect(
            Rect.makeXYWH(
                (x - GlobalVariables.cornerX).toFloat() * game.currentSquareSize,
                (y - GlobalVariables.cornerY).toFloat() * game.currentSquareSize,
                game.currentSquareSize.toFloat(),
                game.currentSquareSize.toFloat()
            ), paint
        )
    }
}

fun pressed(game: GameFiled) {
    val x: Int = (State.mouseX / squareSize).toInt() + GlobalVariables.cornerX
    val y: Int = (State.mouseY / squareSize).toInt() + GlobalVariables.cornerY
    if (!game.contains(x, y)) return
    game.getCell(x, y).isAlive = !game.getCell(x, y).isAlive
    game.getCell(x, y).winStreak = 0
}