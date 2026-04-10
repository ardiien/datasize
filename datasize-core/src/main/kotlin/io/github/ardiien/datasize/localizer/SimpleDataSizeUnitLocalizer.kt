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


public class SimpleDataSizeUnitLocalizer : DataSizeUnitLocalizer {

    override fun name(unit: DataSizeUnit): String = unit.str.first
    override fun abbreviation(unit: DataSizeUnit): String = unit.str.second

    /**
     * For consistency with common user expectations, unit names and abbreviations
     * use decimal-style labels (e.g., "KB", "MB"), even though the underlying values follow the binary system.
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