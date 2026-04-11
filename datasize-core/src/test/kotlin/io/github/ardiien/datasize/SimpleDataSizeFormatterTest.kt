/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.DataSize.Companion.bytes
import io.github.ardiien.datasize.DataSize.Companion.gibibytes
import io.github.ardiien.datasize.formatter.SimpleDataSizeFormatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class SimpleDataSizeFormatterTest {

    private val formatter: SimpleDataSizeFormatter = DefaultDataSizeUnitFormatter

    @Test
    fun `binaryFormat with zero fraction digits returns correct string`() {
        val expected = "5 GB"

        val size = 5.32.gibibytes
        val actual = formatter.binaryFormat(
            value = size,
            unit = IecUnit.Gibibyte,
            fractionDigits = 0,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `binaryFormat with one fraction digit returns correct string`() {
        val expected = "5,3 GB"

        val size = 5.32.gibibytes
        val actual = formatter.binaryFormat(
            value = size,
            unit = IecUnit.Gibibyte,
            fractionDigits = 1,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `binaryFormat with two fraction digits returns correct string`() {
        val expected = "5,32 GB"

        val size = 5.32.gibibytes
        val actual = formatter.binaryFormat(
            value = size,
            unit = IecUnit.Gibibyte,
            fractionDigits = 2,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `binaryFormat returns correct string on zero bytes`() {
        val expected = "0 B"

        val size = 0.bytes
        val actual = formatter.binaryFormat(value = size)

        assertEquals(expected, actual)
    }
}