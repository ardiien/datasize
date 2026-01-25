import io.github.ardiien.datasize.DataSize.Companion.kilobytes
import io.github.ardiien.datasize.DataSizeFormatter


fun main() {
    val value = 55.563.kilobytes

    val defaultPrecision = DataSizeFormatter.format(value, decimals = 0)
    val betterPrecision = DataSizeFormatter.format(value, decimals = 1)
    val maxAvailablePrecision = DataSizeFormatter.format(value, decimals = 2)

    println(defaultPrecision)
    println(betterPrecision)
    println(maxAvailablePrecision)
}