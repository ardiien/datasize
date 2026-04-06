import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.formatter.DefaultDataSizeFormatter


fun main() {
    val formatter = DefaultDataSizeFormatter(DefaultDataSizeFormatter.createFormat())
    val value = 55.563.binary.kibibytes

    val defaultPrecision = formatter.format(value, fractionDigits = 0)
    val betterPrecision = formatter.format(value, fractionDigits = 1)
    val maxAvailablePrecision = formatter.format(value, fractionDigits = 2)

    println(defaultPrecision)
    println(betterPrecision)
    println(maxAvailablePrecision)
}