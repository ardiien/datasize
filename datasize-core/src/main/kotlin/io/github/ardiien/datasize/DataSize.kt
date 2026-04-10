/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.formatter.SimpleDataSizeFormatter
import io.github.ardiien.datasize.localizer.SimpleDataSizeUnitLocalizer
import kotlin.math.roundToLong


/**
 * Represents a data size as a numeric value expressed in bytes with an associated [DataSizeUnit].
 *
 * A [DataSize] is a value object whose magnitude is defined by its underlying byte value.
 * All comparisons and arithmetic operations are performed using this canonical representation,
 * regardless of the unit used to construct the instance.
 *
 * No checks are performed to ensure values correspond to whole bits, so `1.1.bytes` is allowed
 * even though it has little practical meaning. If this behavior is not desired, it must be enforced externally.
 *
 * Conversion to other units is available through:
 * - [toInt], [toLong], [toDouble].
 * - [inBytes], [inKibibytes], [inKilobytes], and related properties.
 */
@JvmInline
public value class DataSize internal constructor(internal val rawValue: Long) : Comparable<DataSize> {

    init {
        require(rawValue >= 0) {
            "DataSize must be >= 0, but was $rawValue."
        }
    }

    public companion object {

        /** The size equal to exactly 0 bytes. */
        public val Zero: DataSize = DataSize(rawValue = 0L)

        /** Returns a [DataSize] representing this value in petabytes. */
        public inline val Number.petabytes: DataSize
            get() = toDataSize(unit = DecimalUnit.Petabyte)

        /** Returns a [DataSize] representing this value in pebibytes. */
        public inline val Number.pebibytes: DataSize
            get() = toDataSize(unit = BinaryUnit.Pebibyte)

        /** Returns a [DataSize] representing this value in terabytes. */
        public inline val Number.terabytes: DataSize
            get() = toDataSize(unit = DecimalUnit.Terabyte)

        /** Returns a [DataSize] representing this value in tebibytes. */
        public inline val Number.tebibytes: DataSize
            get() = toDataSize(unit = BinaryUnit.Tebibyte)

        /** Returns a [DataSize] representing this value in gigabytes. */
        public inline val Number.gigabytes: DataSize
            get() = toDataSize(unit = DecimalUnit.Gigabyte)

        /** Returns a [DataSize] representing this value in gibibytes. */
        public inline val Number.gibibytes: DataSize
            get() = toDataSize(unit = BinaryUnit.Gibibyte)

        /** Returns a [DataSize] representing this value in megabytes. */
        public inline val Number.megabytes: DataSize
            get() = toDataSize(unit = DecimalUnit.Megabyte)

        /** Returns a [DataSize] representing this value in mebibytes. */
        public inline val Number.mebibytes: DataSize
            get() = toDataSize(unit = BinaryUnit.Mebibyte)

        /** Returns a [DataSize] representing this value in kilobytes. */
        public inline val Number.kilobytes: DataSize
            get() = toDataSize(unit = DecimalUnit.Kilobyte)

        /** Returns a [DataSize] representing this value in kibibytes. */
        public inline val Number.kibibytes: DataSize
            get() = toDataSize(unit = BinaryUnit.Kibibyte)

        /** Returns a [DataSize] representing this value in bytes. */
        public inline val Int.bytes: DataSize
            get() = toDataSize(unit = ByteUnit.Byte)

        /** Returns a [DataSize] representing this value in bytes. */
        public inline val Long.bytes: DataSize
            get() = toDataSize(unit = ByteUnit.Byte)

        /** Returns a [DataSize] representing this value in bytes. */
        public inline val ByteArray.bytes: DataSize
            get() = this.size.toDataSize(unit = ByteUnit.Byte)

        public fun Number.toDataSize(unit: DataSizeUnit): DataSize {
            val value = numberToDataSize(value = this, from = unit)
            return DataSize(value)
        }
    }


    public operator fun rem(other: DataSize): DataSize {
        val newValue = rawValue.rem(other.rawValue)
        return normalizedDataSizeOf(newValue)
    }

    public operator fun div(other: Int): DataSize {
        require(other > 0) { "$other must be a positive value to perform division." }

        val newValue = rawValue / other
        return normalizedDataSizeOf(newValue)
    }

    public operator fun times(other: Int): DataSize {
        if (other == 0) return Zero

        val newValue = rawValue * other
        return normalizedDataSizeOf(newValue)
    }

    public operator fun plus(other: DataSize): DataSize {
        val newValue = rawValue + other.rawValue
        return normalizedDataSizeOf(newValue)
    }

    public operator fun minus(other: DataSize): DataSize {
        val newValue = rawValue - other.rawValue
        return normalizedDataSizeOf(newValue)
    }

    /** Returns `true` if this value is equal to zero bytes. */
    public fun isZero(): Boolean = rawValue == Zero.rawValue

    /** Compares this value with another [DataSize] based on byte magnitude. */
    override fun compareTo(other: DataSize): Int = this.rawValue.compareTo(other.rawValue)


    /**
     * Returns this value expressed as a [Double] in the specified unit.
     * Precision may be lost for large values due to floating-point representation.
     */
    public fun toDouble(unit: DataSizeUnit): Double =
        dataSizeToDouble(value = rawValue, to = unit)

    /** Returns this value expressed as a [Long] in the specified unit. */
    public fun toLong(): Long = rawValue


    /** Returns this value expressed in pebibytes. */
    public val inPebibytes: Double
        get() = toDouble(unit = BinaryUnit.Pebibyte)

    /** Returns this value expressed in petabytes. */
    public val inPetabytes: Double
        get() = toDouble(unit = DecimalUnit.Petabyte)

    /** Returns this value expressed in tebibytes. */
    public val inTebibytes: Double
        get() = toDouble(unit = BinaryUnit.Tebibyte)

    /** Returns this value expressed in terabytes. */
    public val inTerabytes: Double
        get() = toDouble(unit = DecimalUnit.Terabyte)

    /** Returns this value expressed in gibibytes. */
    public val inGibibytes: Double
        get() = toDouble(unit = BinaryUnit.Gibibyte)

    /** Returns this value expressed in gigabytes. */
    public val inGigabytes: Double
        get() = toDouble(unit = DecimalUnit.Gigabyte)

    /** Returns this value expressed in mebibytes. */
    public val inMebibytes: Double
        get() = toDouble(unit = BinaryUnit.Mebibyte)

    /** Returns this value expressed in megabytes. */
    public val inMegabytes: Double
        get() = toDouble(unit = DecimalUnit.Megabyte)

    /** Returns this value expressed in kibibytes. */
    public val inKibibytes: Double
        get() = toDouble(unit = BinaryUnit.Kibibyte)

    /** Returns this value expressed in kilobytes. */
    public val inKilobytes: Double
        get() = toDouble(unit = DecimalUnit.Kilobyte)

    /** Returns this value expressed in bytes. */
    public val inBytes: Long
        get() = toLong()

    /** Returns a string representation of this value in bytes. */
    override fun toString(): String = inBytes.toString()

    /**
     * Returns a formatted string representation of this value.
     * The value is converted to the specified [unit] and formatted using [formatter].
     *
     * @param unit the unit to express the value in.
     * @param fractionDigits number of digits after the decimal point (must be non-negative).
     * @param formatter formatting strategy used to produce the output.
     *
     * @throws IllegalArgumentException if [fractionDigits] is negative.
     */
    public fun toString(
        unit: DataSizeUnit,
        fractionDigits: Int = 0,
        formatter: DataSizeFormatter = DefaultDataSizeUnitFormatter,
    ): String = if (unit.isBinaryUnit()) {
        formatter.binaryFormat(this, unit, fractionDigits)
    } else if (unit.isDecimalUnit()) {
        formatter.decimalFormat(this, unit, fractionDigits)
    } else {
        error("Unknown unit: ${unit.tag()}")
    }
}

private fun normalizedDataSizeOf(value: Long): DataSize =
    DataSize(value.coerceAtLeast(0L))

private fun numberToDataSize(
    value: Number,
    from: DataSizeUnit,
    to: DataSizeUnit = ByteUnit.Byte,
): Long {
    if (from == ByteUnit.Byte && to == ByteUnit.Byte) return value.toLong()

    val unit = from.value() / to.value()
    return when (value) {
        is Byte -> unit * value
        is Short -> unit * value
        is Int -> unit * value
        is Long -> unit * value
        is Float -> {
            require(!(value.isNaN() || value.isInfinite())) { "DataSize cannot be NaN or Infinite." }
            (unit * value).roundToLong()
        }
        is Double -> {
            require(!(value.isNaN() || value.isInfinite())) { "DataSize cannot be NaN or Infinite." }
            (unit * value).roundToLong()
        }
        else -> error("Unsupported number type.")
    }
}

private fun dataSizeToDouble(value: Long, to: DataSizeUnit): Double =
    value.toDouble() * ByteUnit.Byte.value() / to.value()

/** Returns the larger of two [DataSize] values (byte-based comparison). */
public fun max(a: DataSize, b: DataSize): DataSize = if (a.rawValue >= b.rawValue) a else b

/** Returns the smaller of two [DataSize] values (byte-based comparison). */
public fun min(a: DataSize, b: DataSize): DataSize = if (a.rawValue <= b.rawValue) a else b

/** Returns value or [DataSize.Zero] if null. */
@Suppress("NOTHING_TO_INLINE")
public inline fun DataSize?.orZero(): DataSize = this ?: DataSize.Zero

internal val DefaultDataSizeUnitFormatter = SimpleDataSizeFormatter(
    format = SimpleDataSizeFormatter.createFormat(),
    localizer = SimpleDataSizeUnitLocalizer()
)
