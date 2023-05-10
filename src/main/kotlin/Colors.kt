import org.jetbrains.skija.Color

object Colors {
    private val data = mutableListOf(Color.makeRGB(0, 0, 0), Color.makeRGB(1, 1, 1))

    var colorsCount = 1
    fun deadColor() = 0

    fun getColor(index: Int) = data[index]

    fun getRandomColor() = (1..colorsCount).random()

    fun addNewColor() {
        colorsCount++
        var color = Color.makeRGB((0..255).random(), (0..255).random(), (0..255).random())
        while (data.contains(color))
            color = Color.makeRGB((0..255).random(), (0..255).random(), (0..255).random())
        data.add(color)
    }
}