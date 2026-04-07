/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.formatter.SimpleDataSizeFormatter
import io.github.ardiien.datasize.unit.BinaryUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class SimpleDataSizeFormatterTest {

    private val formatter: SimpleDataSizeFormatter = SimpleDataSizeFormatter(SimpleDataSizeFormatter.createFormat())

    @Test
    fun `bytes returns Byte unit`() {
        val expected = BinaryUnit.Byte

        val subject = 500.binary.bytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `kibibytes returns Kibibyte unit`() {
        val expected = BinaryUnit.Kibibyte

        val subject = 500.binary.kibibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `mebibytes returns Mebibyte unit`() {
        val expected = BinaryUnit.Mebibyte

        val subject = 500.binary.mebibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `gibibytes returns Gibibyte unit`() {
        val expected = BinaryUnit.Gibibyte

        val subject = 500.binary.gibibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `tebibytes returns Tebibyte unit`() {
        val expected = BinaryUnit.Tebibyte

        val subject = 500.binary.tebibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `pebibytes returns Pebibyte unit`() {
        val expected = BinaryUnit.Pebibyte

        val subject = 500.binary.pebibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `format with zero fraction digits returns correct string`() {
        val expected = "5 GB"

        val size = 5.32.binary.gibibytes
        val actual = formatter.format(
            value = size,
            unit = BinaryUnit.Gibibyte,
            fractionDigits = 0,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `format with one fraction digit returns correct string`() {
        val expected = "5,3 GB"

        val size = 5.32.binary.gibibytes
        val actual = formatter.format(
            value = size,
            unit = BinaryUnit.Gibibyte,
            fractionDigits = 1,
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `format with two fraction digits returns correct string`() {
        val expected = "5,32 GB"

        val size = 5.32.binary.gibibytes
        val actual = formatter.format(
            value = size,
            unit = BinaryUnit.Gibibyte,
            fractionDigits = 2,
        )

        assertEquals(expected, actual)
    }
}