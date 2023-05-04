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

const val width = 20 * 20
const val height = 20 * 20
const val squareSize = 20

fun main() {
    FlatLightLaf.setup()
//    HomeFrame()
    createWindow("Life Game", GameFiled(22, 22))
}

fun createWindow(title: String, game: GameFiled) = runBlocking(Dispatchers.Swing) {
    val window = SkiaWindow().apply {
        setLocationRelativeTo(null)
        preferredSize = Dimension(605, 600)
        isResizable = false
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        this.title = title
    }

    window.layer.renderer = Renderer(window.layer, game)

    window.add(JPanel().apply {
        layout = GridLayout(4, 1, 4, 4)
        add(OneMoveButton("Make one move", game))
        add(GenerateFieldButton("Generate Field", game))
        add(ClearFieldButton("Clear Field", game))
        add(Button(game))
    }, BorderLayout.WEST)

    window.layer.addMouseMotionListener(MouseMotionAdapter)

    val mouseListener: MouseListener = object : MouseListener {
        override fun mouseClicked(e: MouseEvent) {
            pressed(game)
            println(window.getWidth())
        }

        override fun mouseExited(e: MouseEvent) {}
        override fun mousePressed(e: MouseEvent) {}
        override fun mouseEntered(e: MouseEvent) {}
        override fun mouseReleased(e: MouseEvent) {}
    }

    val keyListener: KeyListener = object : KeyListener {
        override fun keyTyped(e: KeyEvent) {}

        override fun keyReleased(e: KeyEvent) {}

        override fun keyPressed(e: KeyEvent) {}

    }


    window.layer.addMouseListener(mouseListener)
    window.layer.addKeyListener(keyListener)
    window.setSize(width, height)
    window.pack()
    window.layer.awaitRedraw()
    window.isVisible = true
}

class Renderer(val layer: SkiaLayer, var game: GameFiled) : SkiaRenderer {
    val typeface = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf")
    val font = Font(typeface, 40f)
    val paint = Paint().apply {
        color = 0xff9BC730L.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }
    val clockFill = Paint().apply {
        color = 0xFFFFFFFF.toInt()
    }
    val clockFillHover = Paint().apply {
        color = 0xFFE4FF01.toInt()
    }
    val clockStroke = Paint().apply {
        color = 0xFF000000.toInt()
        mode = PaintMode.STROKE
        strokeWidth = 1f
    }
    val clockStrokeS = Paint().apply {
        color = 0xFFFF0000.toInt()
        mode = PaintMode.STROKE
        strokeWidth = 1f
    }
    val clockStrokeMH = Paint().apply {
        color = 0xFF0000FF.toInt()
        mode = PaintMode.STROKE
        strokeWidth = 3f
    }

    override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        val contentScale = layer.contentScale
        canvas.scale(contentScale, contentScale)

        drawFiledLines(canvas, game)

        drawFieldSquares(canvas, game)

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

fun distanceSq(x1: Float, y1: Float, x2: Float, y2: Float) = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)


fun drawFiledLines(canvas: Canvas, game: GameFiled) {
    val paint = Paint().apply {
        color = Color.makeRGB(0, 0, 0)
        mode = PaintMode.STROKE
        strokeWidth = 1f
    }
    for (i in 0..game.width) {
        canvas.drawLine(
            i.toFloat() * game.currentSquareSize,
            0F,
            i.toFloat() * game.currentSquareSize,
            game.height * game.currentSquareSize.toFloat(),
            paint
        )
    }

    for (i in 0..game.height) {
        canvas.drawLine(
            0F,
            i.toFloat() * game.currentSquareSize,
            game.width * game.currentSquareSize.toFloat(),
            i.toFloat() * game.currentSquareSize,
            paint
        )
    }
}

fun drawFieldSquares(canvas: Canvas, game: GameFiled) {
    val paint = Paint().apply {
        color = Color.makeRGB(123, 123, 123)
    }

    for (x in 1..game.width)
        for (y in 1..game.height)
            if (game.getCeil(x, y).isAlive) {
                canvas.drawRect(
                    Rect.makeXYWH(
                        (x - 1).toFloat() * game.currentSquareSize,
                        (y - 1).toFloat() * game.currentSquareSize,
                        game.currentSquareSize.toFloat(),
                        game.currentSquareSize.toFloat()
                    ), paint
                )
            }
}

fun pressed(game: GameFiled) {
    if (!(State.mouseX <= game.width * game.currentSquareSize && State.mouseY <= game.height * game.currentSquareSize))
        return
    val x: Int = (State.mouseX / squareSize).toInt()
    val y: Int = (State.mouseY / squareSize).toInt()
    print(x)
    println(y)
    game.getCeil(x + 1, y + 1).isAlive = !game.getCeil(x + 1, y + 1).isAlive
}