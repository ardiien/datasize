/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.builder

import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.ExperimentalDataSizeApi
import io.github.ardiien.datasize.unit.DecimalUnit
import io.github.ardiien.datasize.unit.convertDataSizeUnit


@ExperimentalDataSizeApi
public class DecimalNumberDataSizeBuilder(private val number: Number) {

    init {
        check(number is Int || number is Long || number is Double) {
            "DataSize must be constructed only with Int, Long, or Double."
        }
    }

    /** Returns a [DataSize] representing this value in petabytes (PB). */
    public inline val petabytes: DataSize
        get() = toDataSize(unit = DecimalUnit.Petabyte)

    /** Returns a [DataSize] representing this value in terabytes (TB). */
    public inline val terabytes: DataSize
        get() = toDataSize(unit = DecimalUnit.Terabyte)

    /** Returns a [DataSize] representing this value in gigabytes (GB). */
    public inline val gigabytes: DataSize
        get() = toDataSize(unit = DecimalUnit.Gigabyte)

    /** Returns a [DataSize] representing this value in megabytes (MB). */
    public inline val megabytes: DataSize
        get() = toDataSize(unit = DecimalUnit.Megabyte)

    /** Returns a [DataSize] representing this value in kilobytes (KB). */
    public inline val kilobytes: DataSize
        get() = toDataSize(unit = DecimalUnit.Kilobyte)

    /** Returns a [DataSize] representing this value in bytes. */
    public inline val bytes: DataSize
        get() = toDataSize(unit = DecimalUnit.Byte)

    /**
     * Converts this numeric value expressed in the given decimal [unit] into a [DataSize].
     * The resulting [DataSize] is normalized to bytes.
     *
     * @throws IllegalStateException if the resulting value is not within the supported range.
     */
    public fun toDataSize(unit: DecimalUnit): DataSize {
        val targetUnit = DecimalUnit.Byte
        val value = convertDataSizeUnit(
            value = number,
            sourceUnit = unit,
            targetUnit = targetUnit,
        )

        return DataSize(value, targetUnit)
    }
}