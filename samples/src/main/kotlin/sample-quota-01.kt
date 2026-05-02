import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSize.Companion.gibibytes
import io.github.ardiien.datasize.IecUnit
import kotlin.math.roundToInt


data class QuotaState(
    val used: DataSize,
    val total: DataSize,
) {
    val percentUsed: Float
        get() = (used.inBytes.toFloat() / total.inBytes.toFloat()).coerceIn(0f, 1f)

    val isNearLimit: Boolean
        get() = percentUsed >= 0.90f
}

fun main() {
    val quota = QuotaState(
        used = 4.5.gibibytes,
        total = 5.gibibytes,
    )
    val used: String = quota.used.toIecString(fractionDigits = 2)
    val total: String = quota.total.toString(unit = IecUnit.Gibibyte)
    val percentage: Int = (quota.percentUsed * 100).roundToInt()

    println("$used / $total ($percentage%)")
    println("Near limit: ${quota.isNearLimit}")
}