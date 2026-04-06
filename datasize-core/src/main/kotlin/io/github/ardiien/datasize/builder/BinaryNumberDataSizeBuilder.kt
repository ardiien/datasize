/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.builder

import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.ExperimentalDataSizeApi
import io.github.ardiien.datasize.unit.BinaryUnit
import io.github.ardiien.datasize.unit.convertDataSizeUnit


@ExperimentalDataSizeApi
public class BinaryNumberDataSizeBuilder(private val number: Number) {

    init {
        check(number is Int || number is Long || number is Double) {
            "DataSize must be constructed only with Int, Long, or Double."
        }
    }

    /** Returns a [DataSize] representing this value in pebibytes (PiB). */
    public inline val pebibytes: DataSize
        get() = toDataSize(unit = BinaryUnit.Pebibyte)

    /** Returns a [DataSize] representing this value in tebibytes (TiB). */
    public inline val tebibytes: DataSize
        get() = toDataSize(unit = BinaryUnit.Tebibyte)

    /** Returns a [DataSize] representing this value in gibibytes (GiB). */
    public inline val gibibytes: DataSize
        get() = toDataSize(unit = BinaryUnit.Gibibyte)

    /** Returns a [DataSize] representing this value in mebibytes (MiB). */
    public inline val mebibytes: DataSize
        get() = toDataSize(unit = BinaryUnit.Mebibyte)

    /** Returns a [DataSize] representing this value in kibibytes (KiB). */
    public inline val kibibytes: DataSize
        get() = toDataSize(unit = BinaryUnit.Kibibyte)

    /** Returns a [DataSize] representing this value in bytes. */
    public inline val bytes: DataSize
        get() = toDataSize(unit = BinaryUnit.Byte)

    /**
     * Converts this numeric value expressed in the given binary [unit] into a [DataSize].
     * The resulting [DataSize] is normalized to bytes.
     *
     * @throws IllegalStateException if the resulting value is not within the supported range.
     */
    public fun toDataSize(unit: BinaryUnit): DataSize {
        val targetUnit = BinaryUnit.Byte
        val value = convertDataSizeUnit(
            value = number,
            sourceUnit = unit,
            targetUnit = targetUnit,
        )

        return DataSize(value, targetUnit)
    }
}