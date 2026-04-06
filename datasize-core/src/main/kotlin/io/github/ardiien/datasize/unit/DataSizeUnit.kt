/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.unit

import io.github.ardiien.datasize.ExperimentalDataSizeApi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal
import java.math.MathContext
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


/** Base used for decimal units (powers of 10). */
private const val SI_BASE: Double = 10.0

/** Base used for binary units (powers of 2). */
private const val IEC_BASE: Double = 2.0


/**
 * Represents a unit of data size.
 *
 * Each unit defines a fixed number of bytes and belongs to a specific unit system
 * (decimal or binary). Instances of this class are immutable and provide a
 * type-safe way to work with data size units.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Binary_prefix">Binary prefix</a>
 * @see <a href="https://en.wikipedia.org/wiki/File_size">File size</a>
 */
@ExperimentalDataSizeApi
public abstract class DataSizeUnit internal constructor(
    private val name: String,
    private val abbreviation: String,
    base: Double,
    exponent: Int,
) {
    private val value: BigDecimal = base.toBigDecimal().pow(exponent)

    /** Returns the full human-readable name of this unit. */
    public fun name(): String = name

    /** Returns the abbreviated symbol of this unit. */
    public fun abbreviation(): String = abbreviation

    /** Returns the exact number of bytes represented by a single unit. */
    public fun value(): BigDecimal = value

    /** Returns all units belonging to the same unit system, ordered from largest to smallest. */
    public open fun entries(): ImmutableList<DataSizeUnit> = persistentListOf()
}

/** Returns `true` if this unit belongs to the decimal (SI) system. */
@ExperimentalDataSizeApi
@OptIn(ExperimentalContracts::class)
public fun DataSizeUnit.isDecimalUnit(): Boolean {
    contract { returns(true) implies (this@isDecimalUnit is DecimalUnit) }
    return this is DecimalUnit
}

/** Returns `true` if this unit belongs to the binary (IEC) system. */
@ExperimentalDataSizeApi
@OptIn(ExperimentalContracts::class)
public fun DataSizeUnit.isBinaryUnit(): Boolean {
    contract { returns(true) implies (this@isBinaryUnit is BinaryUnit) }
    return this is BinaryUnit
}

/**
 * Represents decimal (SI) data size units based on powers of 10.
 * Units range from [Byte] (10⁰) to [Petabyte] (10¹⁵).
 */
@ExperimentalDataSizeApi
public sealed class DecimalUnit(
    name: String,
    abbreviation: String,
    base: Double,
    exponent: Int,
) : DataSizeUnit(name, abbreviation, base, exponent) {

    public data object Byte : DecimalUnit(name = "bytes", abbreviation = "B", base = SI_BASE, exponent = 0)
    public data object Kilobyte : DecimalUnit(name = "kilobytes", abbreviation = "KB", base = SI_BASE, exponent = 3)
    public data object Megabyte : DecimalUnit(name = "megabytes", abbreviation = "MB", base = SI_BASE, exponent = 6)
    public data object Gigabyte : DecimalUnit(name = "gigabytes", abbreviation = "GB", base = SI_BASE, exponent = 9)
    public data object Terabyte : DecimalUnit(name = "terabytes", abbreviation = "TB", base = SI_BASE, exponent = 12)
    public data object Petabyte : DecimalUnit(name = "petabytes", abbreviation = "PB", base = SI_BASE, exponent = 15)

    public override fun entries(): ImmutableList<DataSizeUnit> = decimalEntries
}

private val decimalEntries: ImmutableList<DecimalUnit> = persistentListOf(
    DecimalUnit.Petabyte,
    DecimalUnit.Terabyte,
    DecimalUnit.Gigabyte,
    DecimalUnit.Megabyte,
    DecimalUnit.Kilobyte,
    DecimalUnit.Byte,
)

/**
 * Represents binary (IEC) data size units based on powers of 2.
 * Units range from [Byte] (2⁰) to [Pebibyte] (2⁵⁰).
 *
 * Note: For consistency with common user expectations, unit names and abbreviations
 * use decimal-style labels (e.g., "KB", "MB"), even though the underlying values follow the binary system.
 */
@ExperimentalDataSizeApi
public sealed class BinaryUnit(
    name: String,
    abbreviation: String,
    base: Double,
    exponent: Int,
) : DataSizeUnit(name, abbreviation, base, exponent) {

    public data object Byte : BinaryUnit(name = "bytes", abbreviation = "B", base = IEC_BASE, exponent = 0)
    public data object Kibibyte : BinaryUnit(name = "kilobytes", abbreviation = "KB", base = IEC_BASE, exponent = 10)
    public data object Mebibyte : BinaryUnit(name = "megabytes", abbreviation = "MB", base = IEC_BASE, exponent = 20)
    public data object Gibibyte : BinaryUnit(name = "gigabytes", abbreviation = "GB", base = IEC_BASE, exponent = 30)
    public data object Tebibyte : BinaryUnit(name = "terabytes", abbreviation = "TB", base = IEC_BASE, exponent = 40)
    public data object Pebibyte : BinaryUnit(name = "petabytes", abbreviation = "PB", base = IEC_BASE, exponent = 50)

    public override fun entries(): ImmutableList<DataSizeUnit> = binaryEntries
}

private val binaryEntries: ImmutableList<BinaryUnit> = persistentListOf(
    BinaryUnit.Pebibyte,
    BinaryUnit.Tebibyte,
    BinaryUnit.Gibibyte,
    BinaryUnit.Mebibyte,
    BinaryUnit.Kibibyte,
    BinaryUnit.Byte,
)

internal fun convertDataSizeUnit(
    value: Number,
    sourceUnit: DataSizeUnit,
    targetUnit: DataSizeUnit,
): BigDecimal {
    val value = when (value) {
        is Int -> BigDecimal(value, UnitMathContext)
        is Long -> BigDecimal(value, UnitMathContext)
        is Double -> {
            require(!(value.isNaN() || value.isInfinite())) { "DataSizeUnit value cannot be NaN or Infinite." }
            BigDecimal(value, UnitMathContext)
        }
        else -> error("Unsupported number type ${value::class}")
    }

    if (sourceUnit == targetUnit) return value

    return sourceUnit.value()
        .divide(targetUnit.value(), DivisionMathContext)
        .multiply(value, UnitMathContext)
}

internal fun convertDataSizeUnit(
    value: BigDecimal,
    sourceUnit: DataSizeUnit,
    targetUnit: DataSizeUnit,
): BigDecimal {
    if (sourceUnit == targetUnit) return value

    return sourceUnit.value()
        .divide(targetUnit.value(), DivisionMathContext)
        .multiply(value, UnitMathContext)
}

internal val UnitMathContext: MathContext = MathContext.UNLIMITED
internal val DivisionMathContext: MathContext = MathContext.DECIMAL128
