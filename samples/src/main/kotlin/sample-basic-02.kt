import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.DataSize.Companion.decimal
import io.github.ardiien.datasize.unit.BinaryUnit
import io.github.ardiien.datasize.unit.DecimalUnit


fun main() {
    val addition: DataSize = 5.binary.mebibytes + 15.binary.mebibytes
    val additionPreview: String = addition.toString(BinaryUnit.Mebibyte, fractionDigits = 1)
    println(additionPreview)

    val substraction: DataSize = 105.decimal.megabytes - 5.decimal.megabytes
    val substractionPreview: String = substraction.toString(DecimalUnit.Megabyte, fractionDigits = 1)
    println(substractionPreview)

    val multiplication: DataSize = 5.decimal.megabytes * 2
    val multiplicationPreview: String = multiplication.toString(DecimalUnit.Megabyte, fractionDigits = 1)
    println(multiplicationPreview)

    val division: DataSize = 15.binary.mebibytes / 2
    val divisionPreview: String = division.toString(BinaryUnit.Mebibyte, fractionDigits = 1)
    println(divisionPreview)

    val remainder: DataSize = 11.decimal.megabytes % 2.decimal.megabytes
    val remainderPreview: String = remainder.toString(DecimalUnit.Megabyte, fractionDigits = 1)
    println(remainderPreview)
}