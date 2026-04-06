/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.builder.BinaryArrayDataSizeBuilder
import io.github.ardiien.datasize.builder.BinaryNumberDataSizeBuilder
import io.github.ardiien.datasize.builder.DecimalArrayDataSizeBuilder
import io.github.ardiien.datasize.builder.DecimalNumberDataSizeBuilder
import io.github.ardiien.datasize.formatter.DefaultDataSizeFormatter
import io.github.ardiien.datasize.unit.BinaryUnit
import io.github.ardiien.datasize.unit.DataSizeUnit
import io.github.ardiien.datasize.unit.DecimalUnit
import io.github.ardiien.datasize.unit.DivisionMathContext
import io.github.ardiien.datasize.unit.UnitMathContext
import io.github.ardiien.datasize.unit.convertDataSizeUnit
import io.github.ardiien.datasize.unit.isBinaryUnit
import io.github.ardiien.datasize.unit.isDecimalUnit
import java.math.BigDecimal
import kotlin.math.roundToInt


/**
 * Represents a data size in various units (e.g., bytes, kilobytes, megabytes, etc.).
 *
 * This class provides methods for conversion between different data size units.
 * To construct a [DataSize] use either the function `toDataSize`,
 * or the extension properties bytes, kilobytes, and megabytes, available on
 * Int, Long, and Double numeric types.
 *
 * To get the value of this [DataSize] expressed in a particular [DataSizeUnit]s use the functions
 * toInt, toLong, and toDouble or the properties inBytes, inKilobytes, and inMegabytes.
 */
public class DataSize internal constructor(
    internal val rawValue: BigDecimal,
    internal val unit: DataSizeUnit,
) : Comparable<DataSize> {

    init {
        check(rawValue in BigDecimal.ZERO..MAX_SIZE) {
            "DataSize must be in range 0 <= $rawValue <= $MAX_SIZE."
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataSize) return false

        return this.rawValue.compareTo(other.rawValue) == 0
    }

    override fun hashCode(): Int = rawValue.hashCode()

    @Suppress("ClassName")
    public object binary {

        /** The size equal to exactly 0 bytes. */
        public val Zero: DataSize = DataSize(rawValue = BigDecimal.ZERO, unit = BinaryUnit.Byte)

        /** The size whose value is positive infinity. It is useful for representing unlimited size. */
        public val Infinite: DataSize = DataSize(rawValue = MAX_SIZE, unit = BinaryUnit.Byte)
    }

    @Suppress("ClassName")
    public object decimal {

        /** The size equal to exactly 0 bytes. */
        public val Zero: DataSize = DataSize(rawValue = BigDecimal.ZERO, unit = DecimalUnit.Byte)

        /** The size whose value is positive infinity. It is useful for representing unlimited size. */
        public val Infinite: DataSize = DataSize(rawValue = MAX_SIZE, unit = DecimalUnit.Byte)
    }

    public companion object {

        /** Returns a [BinaryNumberDataSizeBuilder] initialized with this [Long] value later interpreted as [DataSize]. */
        public inline val Long.binary: BinaryNumberDataSizeBuilder
            get() = BinaryNumberDataSizeBuilder(this)

        /** Returns a [BinaryNumberDataSizeBuilder] initialized with this [Int] value later interpreted as [DataSize]. */
        public inline val Int.binary: BinaryNumberDataSizeBuilder
            get() = BinaryNumberDataSizeBuilder(this)

        /** Returns a [BinaryNumberDataSizeBuilder] initialized with this [Double] value later interpreted as [DataSize]. */
        public inline val Double.binary: BinaryNumberDataSizeBuilder
            get() = BinaryNumberDataSizeBuilder(this)

        /** Returns a [BinaryArrayDataSizeBuilder] initialized with this [Long] value later interpreted as [DataSize]. */
        public inline val ByteArray.binary: BinaryArrayDataSizeBuilder
            get() = BinaryArrayDataSizeBuilder(this.size)

        /** Returns a [DecimalNumberDataSizeBuilder] initialized with this [Long] value later interpreted as [DataSize]. */
        public inline val Long.decimal: DecimalNumberDataSizeBuilder
            get() = DecimalNumberDataSizeBuilder(this)

        /** Returns a [DecimalNumberDataSizeBuilder] initialized with this [Int] value later interpreted as [DataSize]. */
        public inline val Int.decimal: DecimalNumberDataSizeBuilder
            get() = DecimalNumberDataSizeBuilder(this)

        /** Returns a [DecimalNumberDataSizeBuilder] initialized with this [Double] value later interpreted as [DataSize]. */
        public inline val Double.decimal: DecimalNumberDataSizeBuilder
            get() = DecimalNumberDataSizeBuilder(this)

        /** Returns a [DecimalArrayDataSizeBuilder] initialized with this [Long] value later interpreted as [DataSize]. */
        public inline val ByteArray.decimal: DecimalArrayDataSizeBuilder
            get() = DecimalArrayDataSizeBuilder(this.size)

        /** Returns a new [DataSize] converted from the current [unit] to new [targetUnit] replacing the base. */
        public fun convert(value: DataSize, targetUnit: DataSizeUnit): DataSize {
            val result = convertDataSizeUnit(value.rawValue, value.unit, targetUnit)
            return DataSize(result, targetUnit)
        }
    }


    public operator fun rem(other: DataSize): DataSize =
        normalizedDataSizeOf(rawValue.remainder(other.rawValue, DivisionMathContext), unit)

    /**
     * Divides the [rawValue] by the given [scale] and returns the result as [DataSize].
     *
     * @param scale The divisor for the operation. Must be a positive integer.
     * @throws IllegalArgumentException if [scale] is less than or equal to zero.
     */
    public operator fun div(scale: Int): DataSize {
        require(scale > 0) { "scale $scale must be a positive value to perform division" }

        val result = rawValue.divide(BigDecimal(scale, UnitMathContext), DivisionMathContext)
        return normalizedDataSizeOf(result, unit)
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

        val result = rawValue.divide(BigDecimal(scale, UnitMathContext), DivisionMathContext)
        return normalizedDataSizeOf(result, unit)
    }

    /**
     * Divides the [rawValue] by the given [DataSize], and returns the result.
     *
     * @param other The divisor for the operation. Must be a positive data size unit.
     * @throws IllegalArgumentException if [other] is equal to zero.
     */
    public operator fun div(other: DataSize): DataSize {
        require(other.rawValue > BigDecimal.ZERO) {
            "data size $other must be a positive value to perform division"
        }

        val result = rawValue.divide(other.rawValue, DivisionMathContext)
        return normalizedDataSizeOf(result, unit)
    }

    public operator fun times(scale: Int): DataSize {
        if (scale == 0) return if (isBinary()) binary.Zero else decimal.Zero

        val result = rawValue.multiply(BigDecimal(scale, UnitMathContext), UnitMathContext)
        return normalizedDataSizeOf(result, unit)
    }

    public operator fun times(scale: Double): DataSize {
        val intScale = scale.roundToInt()
        if (intScale.toDouble() == scale) {
            return times(intScale)
        }

        val result = rawValue.multiply(BigDecimal(scale, UnitMathContext), UnitMathContext)
        return normalizedDataSizeOf(result, unit)
    }

    public operator fun times(other: DataSize): DataSize {
        require(other.rawValue > BigDecimal.ZERO) {
            "data size $other must be a positive value to perform multiplication"
        }

        val result = rawValue.multiply(other.rawValue, UnitMathContext)
        return normalizedDataSizeOf(result, unit)
    }

    public operator fun plus(other: DataSize): DataSize {
        val result = rawValue + other.rawValue
        return normalizedDataSizeOf(result, unit)
    }

    public operator fun minus(other: DataSize): DataSize {
        val result = rawValue - other.rawValue
        return normalizedDataSizeOf(result, unit)
    }


    /** Returns true, if the data size value is infinite. */
    public fun isInfinite(): Boolean =
        if (isBinary()) rawValue == binary.Infinite.rawValue else rawValue == decimal.Infinite.rawValue

    /** Returns true, if the data size value is zero. */
    public fun isZero(): Boolean =
        if (isBinary()) rawValue == binary.Zero.rawValue else rawValue == decimal.Zero.rawValue

    /** Returns true, if the data size unit is [DecimalUnit]. */
    public fun isDecimal(): Boolean = unit.isDecimalUnit()

    /** Returns true, if the data size unit is [BinaryUnit]. */
    public fun isBinary(): Boolean = unit.isBinaryUnit()

    override fun compareTo(other: DataSize): Int = this.rawValue.compareTo(other.rawValue)


    /**
     * Returns the value of this data size expressed as a [Double] number of the specified [DataSizeUnit].
     * The operation may involve rounding when the result cannot be represented exactly with a [Double] number.
     */
    public fun toDouble(unit: DataSizeUnit): Double =
        convertDataSizeUnit(
            value = rawValue,
            sourceUnit = this.unit,
            targetUnit = unit,
        ).stripTrailingZeros().toDouble()

    /**
     * Returns the value of this data size expressed as a [Int] number of the specified [DataSizeUnit].
     * If the result doesn't fit in the range of [Int] type, it is coerced into that range.
     */
    public fun toInt(unit: DataSizeUnit): Int =
        convertDataSizeUnit(
            value = rawValue,
            sourceUnit = this.unit,
            targetUnit = unit,
        ).stripTrailingZeros().toInt()

    /** Returns the value of this data size expressed as a [Long] number of the specified [DataSizeUnit]. */
    public fun toLong(unit: DataSizeUnit): Long =
        convertDataSizeUnit(
            value = rawValue,
            sourceUnit = this.unit,
            targetUnit = unit,
        ).stripTrailingZeros().toLong()


    /** The value of this [DataSize] expressed as a [Double] number of pebibytes. */
    public val inPebibytes: Double
        get() = toDouble(BinaryUnit.Pebibyte)

    /** The value of this [DataSize] expressed as a [Double] number of petabytes. */
    public val inPetabytes: Double
        get() = toDouble(DecimalUnit.Petabyte)

    /** The value of this [DataSize] expressed as a [Double] number of tebibytes. */
    public val inTebibytes: Double
        get() = toDouble(BinaryUnit.Tebibyte)

    /** The value of this [DataSize] expressed as a [Double] number of terabytes. */
    public val inTerabytes: Double
        get() = toDouble(DecimalUnit.Terabyte)

    /** The value of this [DataSize] expressed as a [Double] number of gibibytes. */
    public val inGibibytes: Double
        get() = toDouble(BinaryUnit.Gibibyte)

    /** The value of this [DataSize] expressed as a [Double] number of gigabytes. */
    public val inGigabytes: Double
        get() = toDouble(DecimalUnit.Gigabyte)

    /** The value of this [DataSize] expressed as a [Double] number of mebibytes. */
    public val inMebibytes: Double
        get() = toDouble(BinaryUnit.Mebibyte)

    /** The value of this [DataSize] expressed as a [Double] number of megabytes. */
    public val inMegabytes: Double
        get() = toDouble(DecimalUnit.Megabyte)

    /** The value of this [DataSize] expressed as a [Double] number of kibibytes. */
    public val inKibibytes: Double
        get() = toDouble(BinaryUnit.Kibibyte)

    /** The value of this [DataSize] expressed as a [Double] number of kilobytes. */
    public val inKilobytes: Double
        get() = toDouble(DecimalUnit.Kilobyte)

    /** The value of this [DataSize] expressed as a [Long] number of bytes. */
    public val inBytes: Long
        get() = toLong(if (isBinary()) BinaryUnit.Byte else DecimalUnit.Byte)

    override fun toString(): String = when {
        isInfinite() -> "Infinity"
        isBinary() -> toLong(BinaryUnit.Byte).toString()
        isDecimal() -> toLong(DecimalUnit.Byte).toString()
        else -> "0"
    }

    /**
     * Returns a string representation of this data size value expressed in the given [unit]
     * and formatted with the specified [fractionDigits] number of digits after decimal point.
     *
     * Special case:
     *  - an infinite data size is formatted as `"Infinity"` without a unit.
     *
     * @param fractionDigits the number of digits after decimal point to show. The value must be non-negative.
     *        No more than 2 decimals will be shown, even if a larger number is requested with default formatter.
     * @return the value of data size in the specified [unit] followed by that unit abbreviated name: `B`, `KB`, `MB`, `GB`, or `TB`.
     * @throws IllegalArgumentException if [fractionDigits] is less than zero.
     */
    public fun toString(
        unit: DataSizeUnit,
        fractionDigits: Int = 0,
        formatter: DataSizeFormatter = DefaultDataSizeFormatter(DefaultDataSizeFormatter.createFormat()),
    ): String = formatter.format(this, unit, fractionDigits)
}

// Maximum representable value (~9 exabytes).
internal val MAX_SIZE: BigDecimal = BigDecimal(Long.MAX_VALUE, UnitMathContext)

private fun normalizedDataSizeOf(value: BigDecimal, unit: DataSizeUnit): DataSize =
    DataSize(value.coerceIn(BigDecimal.ZERO, MAX_SIZE), unit)


/** Returns the larger of two [DataSize] values (byte-based comparison). */
public fun max(a: DataSize, b: DataSize): DataSize = if (a.rawValue >= b.rawValue) a else b

/** Returns the smaller of two [DataSize] values (byte-based comparison). */
public fun min(a: DataSize, b: DataSize): DataSize = if (a.rawValue <= b.rawValue) a else b

/** Returns value or binary zero if null.*/
@Suppress("NOTHING_TO_INLINE")
public inline fun DataSize?.orBinaryZero(): DataSize = this ?: DataSize.binary.Zero

/** Returns value or decimal zero if null.*/
@Suppress("NOTHING_TO_INLINE")
public inline fun DataSize?.orDecimalZero(): DataSize = this ?: DataSize.decimal.Zero
