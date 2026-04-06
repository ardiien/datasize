import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.max
import io.github.ardiien.datasize.min


fun main() {
    val sortedList = listOf(
        1.binary.kibibytes, 1.binary.mebibytes, 20.binary.kibibytes
    ).sorted()

    val gt = 15.binary.kibibytes > 1.binary.kibibytes
    val lte = 15.binary.kibibytes <= 14.binary.kibibytes
    val eq = 15.binary.kibibytes == 15.binary.kibibytes
    val neq = 15.binary.kibibytes != 5.binary.kibibytes

    val min = min(2.binary.mebibytes, 2.binary.kibibytes)
    val max = max(2.binary.mebibytes, 2.binary.kibibytes)

    println(sortedList)

    println(gt)
    println(lte)
    println(eq)
    println(neq)

    println(min)
    println(max)
}
