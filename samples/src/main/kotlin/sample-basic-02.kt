import io.github.ardiien.datasize.BinaryUnit
import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.mebibytes
import io.github.ardiien.datasize.DataSize.Companion.megabytes
import io.github.ardiien.datasize.DecimalUnit


fun main() {
    val addition: DataSize = 5.mebibytes + 15.mebibytes
    val additionPreview: String = addition.toString(BinaryUnit.Mebibyte, fractionDigits = 1)
    println(additionPreview)

    val substraction: DataSize = 105.megabytes - 5.megabytes
    val substractionPreview: String = substraction.toString(DecimalUnit.Megabyte, fractionDigits = 1)
    println(substractionPreview)

    val multiplication: DataSize = 5.megabytes * 2
    val multiplicationPreview: String = multiplication.toString(DecimalUnit.Megabyte, fractionDigits = 1)
    println(multiplicationPreview)

    val division: DataSize = 15.mebibytes / 2
    val divisionPreview: String = division.toString(BinaryUnit.Mebibyte, fractionDigits = 1)
    println(divisionPreview)

    val remainder: DataSize = 11.megabytes % 2.megabytes
    val remainderPreview: String = remainder.toString(DecimalUnit.Megabyte, fractionDigits = 1)
    println(remainderPreview)
}