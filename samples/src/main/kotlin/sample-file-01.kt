import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.bytes
import io.github.ardiien.datasize.DataSize.Companion.mebibytes
import java.io.File
import java.nio.file.Path
import kotlin.io.path.createTempFile


private fun prepareFile(): File {
    val path: Path = createTempFile(prefix = "datasize")

    return path.toFile().apply {
        setWritable(true)
        deleteOnExit()

        writeText(
            """
            Nunc imperdiet euismod est, nec condimentum eros laoreet tincidunt. Maecenas pellentesque leo at mi ullamcorper, at semper diam elementum. Nunc porttitor iaculis rhoncus. In aliquam, felis molestie gravida dictum, mi mauris suscipit arcu, vel pulvinar enim justo ac leo. Fusce vitae nisi facilisis, consequat mi at, porta augue. Curabitur ut mi ac lacus sagittis vestibulum. Vestibulum congue semper justo, tempor sagittis dolor sodales eget.
            Mauris tortor tortor, convallis et venenatis nec, pulvinar ac urna. Fusce facilisis nisi dui, porttitor gravida nunc luctus in. In orci quam, scelerisque vitae hendrerit nec, pellentesque nec leo. Nulla rutrum non orci in condimentum. Nulla semper lectus pellentesque ligula luctus, nec bibendum justo fringilla. Donec mollis condimentum odio et ullamcorper. Donec scelerisque quam purus, nec venenatis odio semper et. Duis quis quam aliquam lorem congue lobortis. Duis ut nunc facilisis, tempor tortor ac, gravida risus. Suspendisse egestas eu mauris id et.
            Phasellus non dapibus sem. Donec viverra tincidunt ligula, ut sollicitudin orci lobortis sit amet. Vivamus elementum vulputate quam, eget sollicitudin nisi accumsan et. Maecenas gravida, lectus sed blandit egestas, lorem magna mollis nisi, sit amet pretium ligula nibh id quam. Phasellus feugiat, nisl ut accumsan fermentum, ligula magna mollis odio, eu posuere dolor eros ac nisi. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse tincidunt lectus risus, vel iaculis diam ultrices vitae. Nulla a arcu magna. Nullam rhoncus porta vestibulum. Phasellus odio justo, volutpat ac dolor ac, tincidunt malesuada sapien. Quisque malesuada ante id magna efficitur, eget consequat eros gravida. Nam tempus, mauris eu feugiat volutpat, nisi justo dapibus leo, vel gravida felis diam sit amet lorem.
            Vivamus sit amet pretium est. Ut ut quam mollis, maximus erat ut, varius enim. Maecenas quis posuere elit. In at commodo sapien. Nunc ullamcorper odio id mauris vehicula, et mattis mauris eros.
            """.trimIndent()
        )
    }
}

private val limit: DataSize = 1.mebibytes

private fun validateUpload(fileSize: DataSize): Result<Unit> {
    return if (fileSize <= limit) {
        Result.success(Unit)
    } else {
        val formatted = fileSize.toIecString(fractionDigits = 2)
        Result.failure(IllegalArgumentException("File too large: $formatted (max ${limit.toIecString()})"))
    }
}

fun main() {
    val file: File = prepareFile()
    val fileSize: DataSize = file.length().bytes

    validateUpload(fileSize).fold(
        onSuccess = {
            println(fileSize.toIecString(fractionDigits = 2))
        },
        onFailure = {
            println(it.message)
        },
    )
}