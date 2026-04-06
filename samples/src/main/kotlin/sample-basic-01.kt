import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.DataSize.Companion.decimal


fun main() {
    val kilobyteFromInt = 1.binary.kibibytes
    val kilobyteFromDouble = 1.0.decimal.kilobytes
    val kilobyteFromLong = 1L.binary.kibibytes

    println(kilobyteFromInt.inBytes)
    println(kilobyteFromDouble.inBytes)
    println(kilobyteFromLong.inBytes)
}
