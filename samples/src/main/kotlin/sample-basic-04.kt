import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.DataSizeFormatter
import io.github.ardiien.datasize.formatter.SimpleDataSizeFormatter
import java.text.DecimalFormat


fun main() {
    val format: DecimalFormat = SimpleDataSizeFormatter.createFormat()
    val formatter: DataSizeFormatter = SimpleDataSizeFormatter(format)
    val value: DataSize = 55.563.binary.kibibytes

    val defaultPrecision: String = formatter.format(value, fractionDigits = 0)
    println(defaultPrecision)

    val betterPrecision: String = formatter.format(value, fractionDigits = 1)
    println(betterPrecision)

    val maxAvailablePrecision: String = formatter.format(value, fractionDigits = 2)
    println(maxAvailablePrecision)
}