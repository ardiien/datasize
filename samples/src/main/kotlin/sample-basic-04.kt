import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.kibibytes
import io.github.ardiien.datasize.DataSizeFormatter
import io.github.ardiien.datasize.formatter.SimpleDataSizeFormatter
import io.github.ardiien.datasize.localizer.SimpleDataSizeUnitLocalizer
import java.text.DecimalFormat


fun main() {
    val format: DecimalFormat = SimpleDataSizeFormatter.createFormat()
    val localizer = SimpleDataSizeUnitLocalizer()
    val formatter: DataSizeFormatter = SimpleDataSizeFormatter(format, localizer)
    val value: DataSize = 55.563.kibibytes

    val defaultPrecision: String = formatter.binaryFormat(value, fractionDigits = 0)
    println(defaultPrecision)

    val betterPrecision: String = formatter.binaryFormat(value, fractionDigits = 1)
    println(betterPrecision)

    val maxAvailablePrecision: String = formatter.binaryFormat(value, fractionDigits = 2)
    println(maxAvailablePrecision)
}