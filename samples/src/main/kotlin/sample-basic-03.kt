import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.kibibytes
import io.github.ardiien.datasize.DataSize.Companion.mebibytes
import io.github.ardiien.datasize.max
import io.github.ardiien.datasize.min


fun main() {
    val sortedList = listOf<DataSize>(1.kibibytes, 1.mebibytes, 20.kibibytes).sorted()
    println(sortedList)

    val gt: Boolean = 15.kibibytes > 1.kibibytes
    println(gt)

    val lte: Boolean = 15.kibibytes <= 14.kibibytes
    println(lte)

    val eq: Boolean = 15.kibibytes == 15.kibibytes
    println(eq)

    val neq: Boolean = 15.kibibytes != 5.kibibytes
    println(neq)


    val min: DataSize = min(2.mebibytes, 2.kibibytes)
    println(min)

    val max: DataSize = max(2.mebibytes, 2.kibibytes)
    println(max)
}
