import io.github.ardiien.datasize.DataSize.Companion.kilobytes
import io.github.ardiien.datasize.DataSize.Companion.megabytes
import io.github.ardiien.datasize.max
import io.github.ardiien.datasize.min


fun main() {
    val sortedList = listOf(
        1.kilobytes, 1.megabytes, 20.kilobytes
    ).sorted()

    val gt = 15.kilobytes > 1.kilobytes
    val lte = 15.kilobytes <= 14.kilobytes
    val eq = 15.kilobytes == 15.kilobytes
    val neq = 15.kilobytes != 5.kilobytes

    val min = min(2.megabytes, 2.kilobytes)
    val max = max(2.megabytes, 2.kilobytes)

    println(sortedList)

    println(gt)
    println(lte)
    println(eq)
    println(neq)

    println(min)
    println(max)
}
