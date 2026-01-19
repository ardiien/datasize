import team.idivision.libs.datasize.DataSize.Companion.megabytes
import team.idivision.libs.datasize.DataSizeUnit


fun main() {
    val addition = 5.megabytes + 15.megabytes
    val substraction = 105.megabytes - 5.megabytes

    val multiplication = 5.megabytes * 2
    val division = 15.megabytes / 2
    val remainder = 11.megabytes % 2.megabytes

    println(addition.toString(DataSizeUnit.Megabytes, decimals = 1))
    println(substraction.toString(DataSizeUnit.Megabytes, decimals = 1))

    println(multiplication.toString(DataSizeUnit.Megabytes, decimals = 1))
    println(division.toString(DataSizeUnit.Megabytes, decimals = 1))
    println(remainder.toString(DataSizeUnit.Megabytes, decimals = 1))
}