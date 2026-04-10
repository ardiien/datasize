/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.localizer

import io.github.ardiien.datasize.BinaryUnit
import io.github.ardiien.datasize.ByteUnit
import io.github.ardiien.datasize.DataSizeUnit
import io.github.ardiien.datasize.DataSizeUnitLocalizer
import io.github.ardiien.datasize.DecimalUnit
import kotlinx.collections.immutable.persistentMapOf


/**
 * Default implementation of [DataSizeUnitLocalizer] that provides simple,
 * predefined names and abbreviations for data size units.
 *
 * For consistency with common user expectations, both decimal and binary units
 * use decimal-style labels (for example, "KB", "MB"), even though binary units are based on powers of 2.
 */
public class SimpleDataSizeUnitLocalizer : DataSizeUnitLocalizer {

    override fun name(unit: DataSizeUnit): String = unit.str.first
    override fun abbreviation(unit: DataSizeUnit): String = unit.str.second

    /**
     * Returns a pair of name and abbreviation associated with this unit.
     *
     * @throws IllegalStateException if the unit tag is not supported.
     */
    internal val DataSizeUnit.str: Pair<String, String>
        get() = Labels[tag()] ?: error("Unsupported tag: ${tag()}")

    internal companion object {
        private val Byte = "bytes" to "B"
        private val Kilobyte = "kilobytes" to "KB"
        private val Megabyte = "megabytes" to "MB"
        private val Gigabyte = "gigabytes" to "GB"
        private val Terabyte = "terabytes" to "TB"
        private val Petabyte = "petabytes" to "PB"

        /** Mapping between unit tags and their corresponding human-readable names and abbreviations. */
        val Labels = persistentMapOf(
            ByteUnit.Byte.tag() to Byte,
            DecimalUnit.Kilobyte.tag() to Kilobyte,
            DecimalUnit.Megabyte.tag() to Megabyte,
            DecimalUnit.Gigabyte.tag() to Gigabyte,
            DecimalUnit.Terabyte.tag() to Terabyte,
            DecimalUnit.Petabyte.tag() to Petabyte,
            BinaryUnit.Kibibyte.tag() to Kilobyte,
            BinaryUnit.Mebibyte.tag() to Megabyte,
            BinaryUnit.Gibibyte.tag() to Gigabyte,
            BinaryUnit.Tebibyte.tag() to Terabyte,
            BinaryUnit.Pebibyte.tag() to Petabyte,
        )
    }
}