/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.formatter.DefaultDataSizeFormatter
import io.github.ardiien.datasize.unit.BinaryUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class DefaultDataSizeFormatterTest {

    private val formatter: DefaultDataSizeFormatter = DefaultDataSizeFormatter(DefaultDataSizeFormatter.createFormat())

    @Test
    fun `Bytes is returned as data size unit when expected`() {
        val expected = BinaryUnit.Byte

        val subject = 500.binary.bytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `Kilobytes is returned as data size unit when expected`() {
        val expected = BinaryUnit.Kibibyte

        val subject = 500.binary.kibibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `Megabytes is returned as data size unit when expected`() {
        val expected = BinaryUnit.Mebibyte

        val subject = 500.binary.mebibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `Gigabytes is returned as data size unit when expected`() {
        val expected = BinaryUnit.Gibibyte

        val subject = 500.binary.gibibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `Terabytes is returned as data size unit when expected`() {
        val expected = BinaryUnit.Tebibyte

        val subject = 500.binary.tebibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `Petabytes is returned as data size unit when expected`() {
        val expected = BinaryUnit.Pebibyte

        val subject = 500.binary.pebibytes
        val actual = formatter.unitFrom(subject)

        assertEquals(expected, actual)
    }

    @Test
    fun `Format method returns correct string with 0 fraction digits`() {
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
    fun `Format method returns correct string with 1 fraction digit`() {
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
    fun `Format method returns correct string with 2 fraction digits`() {
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