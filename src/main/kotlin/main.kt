import com.formdev.flatlaf.FlatLightLaf
import kotlinx.coroutines.Dispatchers
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
import javax.swing.JPanel
import javax.swing.WindowConstants


fun main() {
    FlatLightLaf.setup()
    createWindow("Life Game", GameFiled(100, 100))
}

fun createWindow(title: String, game: GameFiled) = runBlocking(Dispatchers.Swing) {
    val window = SkiaWindow().apply {
        setLocationRelativeTo(null)
        preferredSize = Dimension(GlobalVariables.windowWidth, GlobalVariables.windowHeight)
        isResizable = false
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        this.title = title
    }

    window.layer.renderer = Renderer(window.layer, game)

    window.add(JPanel().apply {
        layout = GridLayout(10, 1, 3, 3)
        add(OneMoveButton(game))
        add(GenerateFieldButton(game))
        add(ClearFieldButton(game))
        add(StartButton(game))
        add(StopButton(game))
        add(MakeAnyMovesButton(game))
        add(ChangeRulesButton(game))
        add(AddNewColorButton(game))
        add(ChooseColorButton(game))
        add(SaveGame(game))
        add(LoadGame(game))
    }, BorderLayout.WEST)

    window.layer.addMouseMotionListener(MouseMotionAdapter)

    val mouseListener = object : MouseListener {
        override fun mouseClicked(e: MouseEvent) {
            pressed(game)
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
                KeyEvent.VK_W -> if (game.cornerY > GlobalVariables.minimalCorner) game.cornerY--
                KeyEvent.VK_S -> if (game.cornerY < game.height) game.cornerY++
                KeyEvent.VK_A -> if (game.cornerX > GlobalVariables.minimalCorner) game.cornerX--
                KeyEvent.VK_D -> if (game.cornerX < game.width) game.cornerX++
            }
        }
    }

    val wheelListener = object : MouseWheelListener {
        override fun mouseWheelMoved(e: MouseWheelEvent?) {
            if (e == null)
                return
            resizeSquare(e.wheelRotation, game)
        }
    }

    val windowListener = object : WindowListener {
        override fun windowOpened(e: WindowEvent?) {
            loadRules(game)
//            loadGame(game)
        }

        override fun windowClosing(e: WindowEvent?) {
            saveRules(game)
//            saveGame(game)
        }

        override fun windowClosed(e: WindowEvent?) {}
        override fun windowIconified(e: WindowEvent?) {}
        override fun windowDeiconified(e: WindowEvent?) {}
        override fun windowActivated(e: WindowEvent?) {}
        override fun windowDeactivated(e: WindowEvent?) {}

    }

    window.layer.addMouseListener(mouseListener)
    window.layer.addKeyListener(keyListener)
    window.layer.addMouseWheelListener(wheelListener)
    window.addWindowListener(windowListener)
    window.pack()
    window.layer.awaitRedraw()
    window.isVisible = true
}

class Renderer(private val layer: SkiaLayer, private var game: GameFiled) : SkiaRenderer {

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

    val typeface = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf")
    val font = Font(typeface, 13f)

    val x: Int = (State.mouseX / game.currentSquareSize).toInt() + game.cornerX
    val y: Int = (State.mouseY / game.currentSquareSize).toInt() + game.cornerY

    if (!game.contains(x, y) || !game.getCell(x, y).isAlive) return

    val paint = Paint().apply {
        game.getCell(x, y).color
        mode = PaintMode.STROKE
        strokeWidth = 1f
    }

    val text = "alive: ${game.getCell(x, y).winStreak} moves"
    canvas.drawString(text, State.mouseX, State.mouseY, font, paint)
}

fun drawFiledLines(canvas: Canvas, game: GameFiled) {
    val paint = Paint().apply {
        color = Color.makeRGB(0, 0, 0)
        mode = PaintMode.STROKE
        strokeWidth = 1f
    }
    for (i in 1..game.width - game.cornerX + 1) {
        canvas.drawLine(
            i.toFloat() * game.currentSquareSize,
            0F,
            i.toFloat() * game.currentSquareSize,
            game.height * game.currentSquareSize.toFloat() - (game.cornerY - 1) * game.currentSquareSize.toFloat(),
            paint
        )
    }

    for (i in 1..game.height - game.cornerY + 1) {
        canvas.drawLine(
            0F,
            i.toFloat() * game.currentSquareSize,
            game.width * game.currentSquareSize.toFloat() - (game.cornerX - 1) * game.currentSquareSize.toFloat(),
            i.toFloat() * game.currentSquareSize,
            paint
        )
    }
}

fun drawFieldSquares(canvas: Canvas, game: GameFiled) {


    for (y in game.cornerY..game.height)
        for (x in game.cornerX..game.width)
            if (game.contains(x, y) && game.getCell(x, y).isAlive) {
                val paint = Paint().apply {
                    color = Colors.getColor(game.getCell(x, y).color)
                }
                canvas.drawRect(
                    Rect.makeXYWH(
                        (x - game.cornerX).toFloat() * game.currentSquareSize,
                        (y - game.cornerY).toFloat() * game.currentSquareSize,
                        game.currentSquareSize.toFloat(),
                        game.currentSquareSize.toFloat()
                    ), paint
                )
            }
}

fun pressed(game: GameFiled) {
    val x: Int = (State.mouseX / game.currentSquareSize).toInt() + game.cornerX
    val y: Int = (State.mouseY / game.currentSquareSize).toInt() + game.cornerY
    if (!game.contains(x, y)) return
    val cell = game.getCell(x, y)
    cell.color =
        if (cell.color != GlobalVariables.currentColor || !cell.isAlive) GlobalVariables.currentColor else Colors.deadColor()
    cell.isAlive = (cell.color == GlobalVariables.currentColor)
    game.getCell(x, y).winStreak = 0
}

fun resizeSquare(change: Int, game: GameFiled) {
    if (game.currentSquareSize - change >= GlobalVariables.minimumSquareSize)
        game.currentSquareSize -= change
//    println(change)
}