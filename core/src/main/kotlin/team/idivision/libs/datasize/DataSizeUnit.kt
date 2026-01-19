/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package team.idivision.libs.datasize

import kotlin.math.floor
import kotlin.math.pow


internal const val BINARY_BASE: Long = 1024
internal const val DECIMAL_BASE: Long = 1000

internal const val BYTE_SCALE: Long = 1
internal const val KILO_SCALE: Long = BYTE_SCALE * BINARY_BASE
internal const val MEGA_SCALE: Long = KILO_SCALE * BINARY_BASE
internal const val GIGA_SCALE: Long = MEGA_SCALE * BINARY_BASE
internal const val TERA_SCALE: Long = GIGA_SCALE * BINARY_BASE


/**
 * The list of possible size measurement units, in which a data size can be expressed.
 * The smallest time unit is [Bytes] and the largest is [Terabytes].
 */
public enum class DataSizeUnit(private val scale: Long) {
    Terabytes(TERA_SCALE),
    Gigabytes(GIGA_SCALE),
    Megabytes(MEGA_SCALE),
    Kilobytes(KILO_SCALE),
    Bytes(BYTE_SCALE);


    /** Converts the given size [value] of the current [scale] into the specified [unit]. */
    internal fun convert(value: Long, unit: DataSizeUnit): Long =
        cvt(value, this.scale, unit.scale)

    internal companion object {
        private fun cvt(v: Long, src: Long, dst: Long): Long {
            return if (src == dst) v
            else floor((v / (dst / src.toDouble()))).toLong()
        }
    }
}

@ExperimentalDataSizeApi
public fun toDecimalUnit(
    value: Double,
    unit: DataSizeUnit,
): Double {
    val power = unit.base()
    val binaryBase = BINARY_BASE.toDouble().pow(power)
    val decimalBase = DECIMAL_BASE.toDouble().pow(power)

    return value * (binaryBase / decimalBase)
}

@ExperimentalDataSizeApi
public fun toDecimalUnit(
    value: Int,
    unit: DataSizeUnit,
): Double = toDecimalUnit(value.toDouble(), unit)

public fun toBinaryUnit(
    value: Double,
    unit: DataSizeUnit,
): Double {
    val power = unit.base()
    val binaryBase = BINARY_BASE.toDouble().pow(power)
    val decimalBase = DECIMAL_BASE.toDouble().pow(power)

    return value * (decimalBase / binaryBase)
}

public fun toBinaryUnit(
    value: Int,
    unit: DataSizeUnit,
): Double = toBinaryUnit(value.toDouble(), unit)

/** Converts the given size [value] expressed in the specified [sourceUnit] into the specified [targetUnit] ar [Long]. */
internal fun convertDataSizeUnit(
    value: Long,
    sourceUnit: DataSizeUnit,
    targetUnit: DataSizeUnit,
): Long = sourceUnit.convert(value, targetUnit)

/** Converts the given size [value] expressed in the specified [sourceUnit] into the specified [targetUnit] ar [Double]. */
internal fun convertDataSizeUnit(
    value: Double,
    sourceUnit: DataSizeUnit,
    targetUnit: DataSizeUnit,
): Double {
    if (sourceUnit == targetUnit) return value

    val sourceInTargets = targetUnit.convert(1, sourceUnit)
    if (sourceInTargets > 0)
        return (value / sourceInTargets)

    val otherInThis = sourceUnit.convert(1, targetUnit)
    return (value * otherInThis)
}

internal fun DataSizeUnit.shortName(): String =
    when (this) {
        DataSizeUnit.Terabytes -> "TB"
        DataSizeUnit.Gigabytes -> "GB"
        DataSizeUnit.Megabytes -> "MB"
        DataSizeUnit.Kilobytes -> "KB"
        DataSizeUnit.Bytes -> "B"
    }

internal fun DataSizeUnit.base(): Int =
    when (this) {
        DataSizeUnit.Terabytes -> 4
        DataSizeUnit.Gigabytes -> 3
        DataSizeUnit.Megabytes -> 2
        DataSizeUnit.Kilobytes -> 1
        DataSizeUnit.Bytes -> 0
    }