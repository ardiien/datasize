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
public class DecimalArrayDataSizeBuilder(private val number: Number) {

    init {
        check(number is Int) {
            "DataSize must be constructed only with Int."
        }
    }

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