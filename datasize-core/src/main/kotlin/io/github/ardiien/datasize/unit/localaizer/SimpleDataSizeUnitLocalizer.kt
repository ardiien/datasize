/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.unit.localaizer

import io.github.ardiien.datasize.DataSizeUnitLocalizer
import io.github.ardiien.datasize.unit.DataSizeUnit
import io.github.ardiien.datasize.unit.str


public class SimpleDataSizeUnitLocalizer : DataSizeUnitLocalizer {

    override fun name(unit: DataSizeUnit): String = unit.str.first
    override fun abbreviation(unit: DataSizeUnit): String = unit.str.second

    internal companion object {
        val Byte = "bytes" to "B"
        val Kilobyte = "kilobytes" to "KB"
        val Megabyte = "megabytes" to "MB"
        val Gigabyte = "gigabytes" to "GB"
        val Terabyte = "terabytes" to "TB"
        val Petabyte = "petabytes" to "PB"
    }
}