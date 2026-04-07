import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.DataSize.Companion.decimal


fun main() {
    val kilobyteFromInt: DataSize = 1.binary.kibibytes
    val fromInt: Long = kilobyteFromInt.inBytes
    println(fromInt)

    val kilobyteFromDouble: DataSize = 1.0.decimal.kilobytes
    val fromDouble: Long = kilobyteFromDouble.inBytes
    println(fromDouble)

    val kilobyteFromLong: DataSize = 1L.binary.kibibytes
    val fromLong: Long = kilobyteFromLong.inBytes
    println(fromLong)
}
