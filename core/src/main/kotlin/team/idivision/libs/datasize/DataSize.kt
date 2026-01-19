/*
 * Copyright 2026 ardiien
 *  Licensed under the Apache License, Version 2.0.
 *  See http://www.apache.org/licenses/LICENSE-2.0
 */
package team.idivision.libs.datasize

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong


/**
 * Represents a data size in various units (e.g., bytes, kilobytes, megabytes, etc.).
 *
 * This class provides methods for conversion between different data size units.
 * To construct a [DataSize] use either the extension function [toDataSize],
 * or the extension properties bytes, kilobytes, and megabytes, available on
 * Int, Long, and Double numeric types.
 *
 * To get the value of this [DataSize] expressed in a particular [DataSizeUnit]s use the functions
 * toInt, toLong, and toDouble or the properties inBytes, inKilobytes, and inMegabytes.
 *
 * For more information about unit calculations, see [Storage Insights](https://www.ibm.com/docs/en/storage-insights?topic=overview-units-measurement-storage-data).
 */
@JvmInline
public value class DataSize internal constructor(
    private val rawValue: Long
) : Comparable<DataSize> {
    private val sizeUnit
        get() = DataSizeUnit.Bytes

    init {
        check(rawValue in 0..MAX_SIZE) {
            "DataSize must be in range 0 <= $rawValue <= $MAX_SIZE."
        }
    }

    public companion object {
        /** The size equal to exactly 0 bytes. */
        public val Zero: DataSize = DataSize(rawValue = 0)

        /** The size whose value is positive infinity. It is useful for representing unlimited size. */
        public val Infinite: DataSize = DataSize(rawValue = MAX_SIZE)


        /** Returns a [DataSize] equal to this [Long] number of terabytes. */
        public inline val Long.terabytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Terabytes)

        /** Returns a [DataSize] equal to this [Int] number of terabytes. */
        public inline val Int.terabytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Terabytes)

        /** Returns a [DataSize] equal to this [Double] number of terabytes. */
        public inline val Double.terabytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Terabytes)


        /** Returns a [DataSize] equal to this [Long] number of gigabytes. */
        public inline val Long.gigabytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Gigabytes)

        /** Returns a [DataSize] equal to this [Int] number of gigabytes. */
        public inline val Int.gigabytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Gigabytes)

        /** Returns a [DataSize] equal to this [Double] number of gigabytes. */
        public inline val Double.gigabytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Gigabytes)


        /** Returns a [DataSize] equal to this [Long] number of megabytes. */
        public inline val Long.megabytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Megabytes)

        /** Returns a [DataSize] equal to this [Int] number of megabytes. */
        public inline val Int.megabytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Megabytes)

        /** Returns a [DataSize] equal to this [Double] number of megabytes. */
        public inline val Double.megabytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Megabytes)


        /** Returns a [DataSize] equal to this [Long] number of kilobytes. */
        public inline val Long.kilobytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Kilobytes)

        /** Returns a [DataSize] equal to this [Int] number of kilobytes. */
        public inline val Int.kilobytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Kilobytes)

        /** Returns a [DataSize] equal to this [Double] number of kilobytes. */
        public inline val Double.kilobytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Kilobytes)


        /** Returns a [DataSize] equal to this [Long] number of bytes. */
        public inline val Long.bytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Bytes)

        /** Returns a [DataSize] equal to this [Int] number of bytes. */
        public inline val Int.bytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Bytes)

        /** Returns a [DataSize] equal to this [Double] number of bytes. */
        public inline val Double.bytes: DataSize
            get() = toDataSize(unit = DataSizeUnit.Bytes)


        /** Returns a number directly converted from [sourceUnit] to [targetUnit] type. */
        public fun convert(
            value: Double,
            sourceUnit: DataSizeUnit,
            targetUnit: DataSizeUnit,
            precision: Int = 2,
        ): Double = convertDataSizeUnit(value, sourceUnit, targetUnit)
            .roundTo(precision)

        private fun Double.roundTo(precision: Int): Double {
            val factor = 10.0.pow(precision.toDouble())
            return floor(this * factor) / factor
        }
    }

    public operator fun rem(other: DataSize): DataSize =
        dataSizeOf(this.rawValue.rem(other.rawValue))

    /**
     * Divides the [rawValue] by the given [scale] and returns the result as [DataSize].
     *
     * @param scale The divisor for the operation. Must be a positive integer.
     * @throws IllegalArgumentException if [scale] is less than or equal to zero.
     */
    public operator fun div(scale: Int): DataSize {
        require(scale > 0) { "scale $scale must be a positive value to perform division" }

        val result = rawValue / scale
        return dataSizeOf(value = result)
    }

    /**
     * Divides the [rawValue] by the given [scale] and returns the result as [DataSize].
     *
     * @param scale The divisor for the operation. Must be a positive integer.
     * @throws IllegalArgumentException if [scale] is less than or equal to zero.
     */
    public operator fun div(scale: Double): DataSize {
        val intScale = scale.roundToInt()

        if (intScale.toDouble() == scale && intScale != 0) {
            return div(scale = intScale)
        }

        require(scale > 0) { "scale $scale must be a positive value to perform division" }

        val unit = sizeUnit
        val result = toDouble(unit) / scale
        return result.toDataSize(unit = unit)
    }

    /**
     * Divides the [rawValue], converted to a [Double], by the given [DataSize],
     * and returns the result as a [Double] in the larger [sizeUnit] of the two.
     *
     * @param other The divisor for the operation. Must be a positive data size unit.
     * @throws IllegalArgumentException if [other] is equal to zero.
     */
    public operator fun div(other: DataSize): Double {
        require(other.rawValue > 0) {
            "data size unit $other must be a positive value to perform division"
        }

        val coarserUnit = maxOf(this.sizeUnit, other.sizeUnit)
        return this.toDouble(coarserUnit) / other.toDouble(coarserUnit)
    }

    public operator fun times(scale: Int): DataSize {
        if (scale == 0) return Zero

        val result = rawValue * scale
        return dataSizeOfNormalized(value = result)
    }

    public operator fun times(scale: Double): DataSize {
        val intScale = scale.roundToInt()
        if (intScale.toDouble() == scale) {
            return times(intScale)
        }

        val unit = sizeUnit
        val result = toDouble(unit) * scale
        return result.toDataSize(unit = unit)
    }

    public operator fun plus(other: DataSize): DataSize {
        val result = rawValue + other.rawValue
        return dataSizeOfNormalized(value = result)
    }

    public operator fun minus(other: DataSize): DataSize {
        val result = rawValue - other.rawValue
        return dataSizeOfNormalized(value = result)
    }


    /** Returns true, if the data size value is infinite. */
    public fun isInfinite(): Boolean = rawValue == Infinite.rawValue

    /** Returns true, if the data size value is zero. */
    public fun isZero(): Boolean = rawValue == Zero.rawValue

    override fun compareTo(other: DataSize): Int =
        this.rawValue.compareTo(other.rawValue)


    /**
     * Returns the value of this data size expressed as a [Double] number of the specified [DataSizeUnit].
     * The operation may involve rounding when the result cannot be represented exactly with a [Double] number.
     */
    public fun toDouble(unit: DataSizeUnit): Double =
        convertDataSizeUnit(
            value = rawValue.coerceIn(0, MAX_SIZE).toDouble(),
            sourceUnit = sizeUnit,
            targetUnit = unit,
        )

    /**
     * Returns the value of this data size expressed as a [Int] number of the specified [DataSizeUnit].
     * If the result doesn't fit in the range of [Int] type, it is coerced into that range.
     */
    public fun toInt(unit: DataSizeUnit): Int =
        convertDataSizeUnit(
            value = rawValue.coerceIn(0, MAX_SIZE),
            sourceUnit = sizeUnit,
            targetUnit = unit,
        ).toInt()

    /** Returns the value of this data size expressed as a [Long] number of the specified [DataSizeUnit]. */
    public fun toLong(unit: DataSizeUnit): Long =
        convertDataSizeUnit(
            value = rawValue.coerceIn(0, MAX_SIZE),
            sourceUnit = sizeUnit,
            targetUnit = unit,
        )


    /** The value of this [DataSize] expressed as a [Double] number of terabytes. */
    public val inTerabytes: Double
        get() = toDouble(unit = DataSizeUnit.Terabytes)

    /** The value of this [DataSize] expressed as a [Double] number of gigabytes. */
    public val inGigabytes: Double
        get() = toDouble(unit = DataSizeUnit.Gigabytes)

    /** The value of this [DataSize] expressed as a [Double] number of megabytes. */
    public val inMegabytes: Double
        get() = toDouble(unit = DataSizeUnit.Megabytes)

    /** The value of this [DataSize] expressed as a [Double] number of kilobytes. */
    public val inKilobytes: Double
        get() = toDouble(unit = DataSizeUnit.Kilobytes)

    /** The value of this [DataSize] expressed as a [Long] number of bytes. */
    public val inBytes: Long
        get() = toLong(unit = DataSizeUnit.Bytes)

    /**
     * Returns a string representation of this data size value expressed in the given [unit]
     * and formatted with the specified [decimals] number of digits after decimal point.
     *
     * Special case:
     *  - an infinite data size is formatted as `"Infinity"` without a unit.
     *
     * @param decimals the number of digits after decimal point to show. The value must be non-negative.
     *        No more than 2 decimals will be shown, even if a larger number is requested.
     * @return the value of data size in the specified [unit] followed by that unit abbreviated name: `B`, `KB`, `MB`, `GB`, or `TB`.
     * @throws IllegalArgumentException if [decimals] is less than zero.
     */
    public fun toString(unit: DataSizeUnit, decimals: Int = 0): String {
        require(decimals >= 0) { "decimals must not be negative, but was $decimals" }

        val number = toDouble(unit)
        if (number.isInfinite()) return number.toString()

        return "${createFormatForDecimals(number, decimals.coerceAtMost(2)).format(number)} ${unit.shortName()}"
    }

    /**
     * Returns a string representation of this data size value in decimal base expressed in the given [unit]
     * and formatted with the specified [decimals] number of digits after decimal point.
     *
     * Special case:
     *  - an infinite data size is formatted as `"Infinity"` without a unit.
     *
     * @param decimals the number of digits after decimal point to show. The value must be non-negative.
     *        No more than 2 decimals will be shown, even if a larger number is requested.
     * @return the value of data size in the specified [unit] followed by that unit abbreviated name: `B`, `KB`, `MB`, `GB`, or `TB`.
     * @throws IllegalArgumentException if [decimals] is less than zero.
     */
    @ExperimentalDataSizeApi
    public fun toDecimalString(unit: DataSizeUnit, decimals: Int = 0): String {
        require(decimals >= 0) { "decimals must be not negative, but was $decimals" }

        val number = toDecimalUnit(toDouble(unit), unit)
        if (number.isInfinite()) return number.toString()

        return "${createFormatForDecimals(number, decimals.coerceAtMost(2)).format(number)} ${unit.shortName()}"
    }

    private fun createFormatForDecimals(number: Double, decimals: Int) =
        DecimalFormat("0").apply {
            if (decimals > 0) maximumFractionDigits = decimals
            roundingMode = RoundingMode.HALF_UP
            decimalFormatSymbols = DecimalFormatSymbols().apply {
                decimalSeparator = ','
                groupingSeparator = '.'
                isGroupingUsed = number >= 10000
                groupingSize = 3
            }
        }
}

/**
 * Returns [DataSize] converted from a double with [DataSizeUnit] type.
 *
 * @throws IllegalArgumentException value is Not-a-Number(NaN) or infinite.
 * @throws IllegalStateException value is not within 0 <= value <= [MAX_SIZE]
 */
public fun Double.toDataSize(unit: DataSizeUnit): DataSize {
    require(!this.isNaN() && !this.isInfinite()) { "DataSizeUnit value cannot be NaN or Infinite." }

    val value = convertDataSizeUnit(
        value = this,
        sourceUnit = unit,
        targetUnit = DataSizeUnit.Bytes,
    ).roundToLong()

    return DataSize(value)
}

/**
 * Returns [DataSize] converted from an integer with [DataSizeUnit] type.
 *
 * @throws IllegalStateException value is not within 0 <= value <= [MAX_SIZE]
 */
public fun Int.toDataSize(unit: DataSizeUnit): DataSize {
    val value = convertDataSizeUnit(
        value = this.toLong(),
        sourceUnit = unit,
        targetUnit = DataSizeUnit.Bytes,
    )

    return DataSize(value)
}

/**
 * Returns [DataSize] converted from a long with [DataSizeUnit] type.
 *
 * @throws IllegalStateException value is not within 0 <= value <= [MAX_SIZE]
 */
public fun Long.toDataSize(unit: DataSizeUnit): DataSize {
    val value = convertDataSizeUnit(
        value = this,
        sourceUnit = unit,
        targetUnit = DataSizeUnit.Bytes,
    )

    return DataSize(value)
}

public fun max(a: DataSize, b: DataSize): DataSize =
    dataSizeOf(max(a.inBytes, b.inBytes))

public fun min(a: DataSize, b: DataSize): DataSize =
    dataSizeOf(min(a.inBytes, b.inBytes))

// Max size of 8 exabytes should be enough.
internal const val MAX_SIZE = Long.MAX_VALUE

private fun dataSizeOfNormalized(value: Long) =
    dataSizeOf(value.coerceIn(0, MAX_SIZE))

private fun dataSizeOf(value: Long) = DataSize(value)

/** Returns the specified [DataSize] if not `null`, or [DataSize.Zero] otherwise. */
@Suppress("NOTHING_TO_INLINE")
public inline fun DataSize?.orZero(): DataSize = this ?: DataSize.Zero
