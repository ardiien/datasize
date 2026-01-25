import io.github.ardiien.datasize.DataSize.Companion.kilobytes


fun main() {
    val kilobyteFromInt = 1.kilobytes
    val kilobyteFromDouble = 1.0.kilobytes
    val kilobyteFromLong = 1L.kilobytes

    println(kilobyteFromInt.inBytes)
    println(kilobyteFromDouble.inBytes)
    println(kilobyteFromLong.inBytes)
}
