/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */

package io.github.ardiien.datasize

import io.github.ardiien.datasize.DataSize.Companion.bytes
import io.github.ardiien.datasize.DataSize.Companion.gigabytes
import io.github.ardiien.datasize.DataSize.Companion.kilobytes
import io.github.ardiien.datasize.DataSize.Companion.megabytes
import io.github.ardiien.datasize.DataSize.Companion.terabytes
import kotlin.test.Test
import kotlin.test.assertEquals


class DataSizeFormatterTest {

    @Test
    fun `Bytes is returned as data size unit when expected`() {
        val expected = DataSizeUnit.Bytes
        val size = 500.bytes.inBytes
        val actualFromDataSize = DataSizeFormatter.unitFrom(size.bytes)
        val actualFromRawValue = DataSizeFormatter.unitFrom(size)
        assertEquals(expected, actualFromDataSize)
        assertEquals(expected, actualFromRawValue)
    }

    @Test
    fun `Kilobytes is returned as data size unit when expected`() {
        val expected = DataSizeUnit.Kilobytes
        val size = 500.kilobytes.inBytes
        val actualFromDataSize = DataSizeFormatter.unitFrom(size.bytes)
        val actualFromRawValue = DataSizeFormatter.unitFrom(size)
        assertEquals(expected, actualFromDataSize)
        assertEquals(expected, actualFromRawValue)
    }

    @Test
    fun `Megabytes is returned as data size unit when expected`() {
        val expected = DataSizeUnit.Megabytes
        val size = 500.megabytes.inBytes
        val actualFromDataSize = DataSizeFormatter.unitFrom(size.bytes)
        val actualFromRawValue = DataSizeFormatter.unitFrom(size)
        assertEquals(expected, actualFromDataSize)
        assertEquals(expected, actualFromRawValue)
    }

    @Test
    fun `Gigabytes is returned as data size unit when expected`() {
        val expected = DataSizeUnit.Gigabytes
        val size = 500.gigabytes.inBytes
        val actualFromDataSize = DataSizeFormatter.unitFrom(size.bytes)
        val actualFromRawValue = DataSizeFormatter.unitFrom(size)
        assertEquals(expected, actualFromDataSize)
        assertEquals(expected, actualFromRawValue)
    }

    @Test
    fun `Terabytes is returned as data size unit when expected`() {
        val expected = DataSizeUnit.Terabytes
        val size = 500.terabytes.inBytes
        val actualFromDataSize = DataSizeFormatter.unitFrom(size.bytes)
        val actualFromRawValue = DataSizeFormatter.unitFrom(size)
        assertEquals(expected, actualFromDataSize)
        assertEquals(expected, actualFromRawValue)
    }

    @Test
    fun `Format method returns correct string with 0 decimals`() {
        val expected = "5 GB"
        val size = 5.32.gigabytes.inBytes
        val actualFromDataSize =
            DataSizeFormatter.format(
                value = size.bytes,
                unit = DataSizeUnit.Gigabytes,
                decimals = 0
            )
        val actualFromRawValue = DataSizeFormatter.format(
            value = size,
            unit = DataSizeUnit.Gigabytes,
            decimals = 0
        )
        assertEquals(expected, actualFromDataSize)
        assertEquals(expected, actualFromRawValue)
    }

    @Test
    fun `Format method returns correct string with 1 decimals`() {
        val expected = "5,3 GB"
        val size = 5.32.gigabytes.inBytes
        val actualFromDataSize =
            DataSizeFormatter.format(
                value = size.bytes,
                unit = DataSizeUnit.Gigabytes,
                decimals = 1
            )
        val actualFromRawValue = DataSizeFormatter.format(
            value = size,
            unit = DataSizeUnit.Gigabytes,
            decimals = 1
        )
        assertEquals(expected, actualFromDataSize)
        assertEquals(expected, actualFromRawValue)
    }

    @Test
    fun `Format method returns correct string with 2 decimals`() {
        val expected = "5,32 GB"
        val size = 5.32.gigabytes.inBytes
        val actualFromDataSize =
            DataSizeFormatter.format(
                value = size.bytes,
                unit = DataSizeUnit.Gigabytes,
                decimals = 2
            )
        val actualFromRawValue = DataSizeFormatter.format(
            value = size,
            unit = DataSizeUnit.Gigabytes,
            decimals = 2
        )
        assertEquals(expected, actualFromDataSize)
        assertEquals(expected, actualFromRawValue)
    }
}