import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.max
import io.github.ardiien.datasize.min


fun main() {
    val sortedList = listOf<DataSize>(1.binary.kibibytes, 1.binary.mebibytes, 20.binary.kibibytes).sorted()
    println(sortedList)

    val gt: Boolean = 15.binary.kibibytes > 1.binary.kibibytes
    println(gt)

    val lte: Boolean = 15.binary.kibibytes <= 14.binary.kibibytes
    println(lte)

    val eq: Boolean = 15.binary.kibibytes == 15.binary.kibibytes
    println(eq)

    val neq: Boolean = 15.binary.kibibytes != 5.binary.kibibytes
    println(neq)


    val min: DataSize = min(2.binary.mebibytes, 2.binary.kibibytes)
    println(min)

    val max: DataSize = max(2.binary.mebibytes, 2.binary.kibibytes)
    println(max)
}
