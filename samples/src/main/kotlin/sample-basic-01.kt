import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.kibibytes
import io.github.ardiien.datasize.DataSize.Companion.kilobytes


fun main() {
    val kilobyteFromInt: DataSize = 1.kibibytes
    val fromInt: Long = kilobyteFromInt.inBytes
    println(fromInt)

    val kilobyteFromDouble: DataSize = 1.0.kilobytes
    val fromDouble: Long = kilobyteFromDouble.inBytes
    println(fromDouble)

    val kilobyteFromLong: DataSize = 1L.kibibytes
    val fromLong: Long = kilobyteFromLong.inBytes
    println(fromLong)
}
