import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.DataSize.Companion.decimal
import io.github.ardiien.datasize.unit.BinaryUnit
import io.github.ardiien.datasize.unit.DecimalUnit


fun main() {
    val addition = 5.binary.mebibytes + 15.binary.mebibytes
    val substraction = 105.decimal.megabytes - 5.decimal.megabytes

    val multiplication = 5.decimal.megabytes * 2
    val division = 15.binary.mebibytes / 2
    val remainder = 11.decimal.megabytes % 2.decimal.megabytes

    println(addition.toString(BinaryUnit.Mebibyte, fractionDigits = 1))
    println(substraction.toString(DecimalUnit.Megabyte, fractionDigits = 1))

    println(multiplication.toString(DecimalUnit.Megabyte, fractionDigits = 1))
    println(division.toString(BinaryUnit.Mebibyte, fractionDigits = 1))
    println(remainder.toString(DecimalUnit.Megabyte, fractionDigits = 1))
}