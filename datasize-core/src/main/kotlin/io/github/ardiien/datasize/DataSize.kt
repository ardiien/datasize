/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.DataSize.Companion.decimal
import io.github.ardiien.datasize.builder.BinaryArrayDataSizeBuilder
import io.github.ardiien.datasize.builder.BinaryNumberDataSizeBuilder
import io.github.ardiien.datasize.builder.DecimalArrayDataSizeBuilder
import io.github.ardiien.datasize.builder.DecimalNumberDataSizeBuilder
import io.github.ardiien.datasize.formatter.SimpleDataSizeFormatter
import io.github.ardiien.datasize.unit.BinaryUnit
import io.github.ardiien.datasize.unit.DataSizeUnit
import io.github.ardiien.datasize.unit.DecimalUnit
import io.github.ardiien.datasize.unit.DivisionMathContext
import io.github.ardiien.datasize.unit.UnitMathContext
import io.github.ardiien.datasize.unit.convertDataSizeUnit
import io.github.ardiien.datasize.unit.isBinaryUnit
import io.github.ardiien.datasize.unit.isDecimalUnit
import io.github.ardiien.datasize.unit.localaizer.SimpleDataSizeUnitLocalizer
import java.math.BigDecimal
import kotlin.math.roundToInt


/**
 * Represents a data size as a numeric value expressed in bytes with an associated [DataSizeUnit].
 *
 * A [DataSize] is a value object whose magnitude is defined by its underlying byte value.
 * All comparisons and arithmetic operations are performed using this canonical representation,
 * regardless of the unit used to construct the instance.
 *
 * Instances are created via builder extensions:
 * - [Long.binary], [Int.binary], [Double.binary], [ByteArray.binary].
 * - [Long.decimal], [Int.decimal], [Double.decimal], [ByteArray.decimal].
 *
 * Conversion to other units is available through:
 * - [toInt], [toLong], [toDouble].
 * - [inBytes], [inKilobytes], [inMegabytes], and related properties.
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

    /**
     * Returns `true` if both values represent the same number of bytes.
     * The unit is not considered for equality.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataSize) return false

        return this.rawValue.compareTo(other.rawValue) == 0
    }

    /**
     * Returns a hash code based on the underlying byte value.
     *
     * The unit is not included to preserve consistency with [equals].
     */
    override fun hashCode(): Int = rawValue.hashCode()

    /** Predefined values for the binary (IEC) unit system. */
    @Suppress("ClassName")
    public object binary {

        /** The size equal to exactly 0 bytes. */
        public val Zero: DataSize = DataSize(rawValue = BigDecimal.ZERO, unit = BinaryUnit.Byte)

        /** The size whose value is positive infinity. It is useful for representing unlimited size. */
        public val Infinite: DataSize = DataSize(rawValue = MAX_SIZE, unit = BinaryUnit.Byte)
    }

    /** Predefined values for the decimal (SI) unit system. */
    @Suppress("ClassName")
    public object decimal {

        /** The size equal to exactly 0 bytes. */
        public val Zero: DataSize = DataSize(rawValue = BigDecimal.ZERO, unit = DecimalUnit.Byte)

        /** The size whose value is positive infinity. It is useful for representing unlimited size. */
        public val Infinite: DataSize = DataSize(rawValue = MAX_SIZE, unit = DecimalUnit.Byte)
    }

    public companion object {

        /** Returns a [BinaryNumberDataSizeBuilder] initialized with this value. */
        public inline val Long.binary: BinaryNumberDataSizeBuilder
            get() = BinaryNumberDataSizeBuilder(this)

        /** Returns a [BinaryNumberDataSizeBuilder] initialized with this value. */
        public inline val Int.binary: BinaryNumberDataSizeBuilder
            get() = BinaryNumberDataSizeBuilder(this)

        /** Returns a [BinaryNumberDataSizeBuilder] initialized with this value. */
        public inline val Double.binary: BinaryNumberDataSizeBuilder
            get() = BinaryNumberDataSizeBuilder(this)

        /** Returns a [BinaryArrayDataSizeBuilder] initialized with this value. */
        public inline val ByteArray.binary: BinaryArrayDataSizeBuilder
            get() = BinaryArrayDataSizeBuilder(this.size)

        /** Returns a [DecimalNumberDataSizeBuilder] initialized with this value. */
        public inline val Long.decimal: DecimalNumberDataSizeBuilder
            get() = DecimalNumberDataSizeBuilder(this)

        /** Returns a [DecimalNumberDataSizeBuilder] initialized with this value. */
        public inline val Int.decimal: DecimalNumberDataSizeBuilder
            get() = DecimalNumberDataSizeBuilder(this)

        /** Returns a [DecimalNumberDataSizeBuilder] initialized with this value. */
        public inline val Double.decimal: DecimalNumberDataSizeBuilder
            get() = DecimalNumberDataSizeBuilder(this)

        /** Returns a [DecimalArrayDataSizeBuilder] initialized with this value. */
        public inline val ByteArray.decimal: DecimalArrayDataSizeBuilder
            get() = DecimalArrayDataSizeBuilder(this.size)
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

    public operator fun plus(other: DataSize): DataSize {
        val result = rawValue + other.rawValue
        return normalizedDataSizeOf(result, unit)
    }

    public operator fun minus(other: DataSize): DataSize {
        val result = rawValue - other.rawValue
        return normalizedDataSizeOf(result, unit)
    }


    /** Returns `true` if this value represents the maximum supported size. */
    public fun isInfinite(): Boolean =
        if (isBinary()) rawValue == binary.Infinite.rawValue else rawValue == decimal.Infinite.rawValue

    /** Returns `true` if this value is equal to zero bytes. */
    public fun isZero(): Boolean =
        if (isBinary()) rawValue == binary.Zero.rawValue else rawValue == decimal.Zero.rawValue

    /** Returns `true` if this instance uses a decimal (SI) unit. */
    public fun isDecimal(): Boolean = unit.isDecimalUnit()

    /** Returns `true` if this instance uses a binary (IEC) unit. */
    public fun isBinary(): Boolean = unit.isBinaryUnit()

    /** Compares this value with another [DataSize] based on byte magnitude. */
    override fun compareTo(other: DataSize): Int = this.rawValue.compareTo(other.rawValue)


    /**
     * Returns this value expressed as a [Double] in the specified [unit].
     * Precision may be lost for large values due to floating-point representation.
     */
    public fun toDouble(unit: DataSizeUnit): Double =
        convertDataSizeUnit(
            value = rawValue,
            sourceUnit = this.unit,
            targetUnit = unit,
        ).stripTrailingZeros().toDouble()

    /**
     * Returns this value expressed as an [Int] in the specified [unit].
     * Values outside the [Int] range are coerced.
     */
    public fun toInt(unit: DataSizeUnit): Int =
        convertDataSizeUnit(
            value = rawValue,
            sourceUnit = this.unit,
            targetUnit = unit,
        ).stripTrailingZeros().toInt()

    /** Returns this value expressed as a [Long] in the specified [unit]. */
    public fun toLong(unit: DataSizeUnit): Long =
        convertDataSizeUnit(
            value = rawValue,
            sourceUnit = this.unit,
            targetUnit = unit,
        ).stripTrailingZeros().toLong()


    /** Returns this value expressed in pebibytes. */
    public val inPebibytes: Double
        get() = toDouble(BinaryUnit.Pebibyte)

    /** Returns this value expressed in petabytes. */
    public val inPetabytes: Double
        get() = toDouble(DecimalUnit.Petabyte)

    /** Returns this value expressed in tebibytes. */
    public val inTebibytes: Double
        get() = toDouble(BinaryUnit.Tebibyte)

    /** Returns this value expressed in terabytes. */
    public val inTerabytes: Double
        get() = toDouble(DecimalUnit.Terabyte)

    /** Returns this value expressed in gibibytes. */
    public val inGibibytes: Double
        get() = toDouble(BinaryUnit.Gibibyte)

    /** Returns this value expressed in gigabytes. */
    public val inGigabytes: Double
        get() = toDouble(DecimalUnit.Gigabyte)

    /** Returns this value expressed in mebibytes. */
    public val inMebibytes: Double
        get() = toDouble(BinaryUnit.Mebibyte)

    /** Returns this value expressed in megabytes. */
    public val inMegabytes: Double
        get() = toDouble(DecimalUnit.Megabyte)

    /** Returns this value expressed in kibibytes. */
    public val inKibibytes: Double
        get() = toDouble(BinaryUnit.Kibibyte)

    /** Returns this value expressed in kilobytes. */
    public val inKilobytes: Double
        get() = toDouble(DecimalUnit.Kilobyte)

    /** Returns this value expressed in bytes. */
    public val inBytes: Long
        get() = toLong(if (isBinary()) BinaryUnit.Byte else DecimalUnit.Byte)

    /**
     * Returns a string representation of this value in bytes.
     *
     * - Returns `"Infinity"` for infinite values.
     * - Otherwise returns the byte value without unit suffix.
     */
    override fun toString(): String = when {
        isInfinite() -> "Infinity"
        isBinary() -> inBytes.toString()
        isDecimal() -> inBytes.toString()
        else -> "0"
    }

    /**
     * Returns a formatted string representation of this value.
     *
     * The value is converted to the specified [unit] and formatted using [formatter].
     *
     * Special case:
     * - Infinite values are formatted as `"Infinity"` without a unit.
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

internal val DefaultDataSizeUnitFormatter = SimpleDataSizeFormatter(
    format = SimpleDataSizeFormatter.createFormat(),
    localizer = SimpleDataSizeUnitLocalizer()
)
