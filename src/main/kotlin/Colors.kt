import org.jetbrains.skija.Color

object Colors {
    private val data = mutableListOf(Color.makeRGB(0, 0, 0), Color.makeRGB(1, 1, 1))

    private var colorsCount = 1 // показывает нынешнее кол-во достпуных цветов, может быть меньше резмера листа цветов, нужен для понимания того,
                                // сколько сейчас у нас цветов есть, скажем, если у нас было 5 цветов, а мы закгрузили поле, где было 2,
                                // чтобы мы не могли обращаться к недоступным цветам после загрузки

    var currentColor = 1

    fun deadColor() = 0

    fun colorsCount() = colorsCount
    fun setColorCount(count: Int) {
        if (count > data.size - 1)
            repeat(count - data.size + 1) {
                addNewColor()
            }
        colorsCount = count
    }

    fun getColor(index: Int) = data[index]

    fun getRandomColor() = (1..colorsCount).random()

    private fun addNewColor() {
        colorsCount++
        var color = Color.makeRGB((0..255).random(), (0..255).random(), (0..255).random())
        while (data.contains(color))
            color = Color.makeRGB((0..255).random(), (0..255).random(), (0..255).random())
        data.add(color)
    }
}