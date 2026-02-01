/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */

package io.github.ardiien.datasize

import org.junit.jupiter.api.Assertions.assertThrows
import io.github.ardiien.datasize.DataSize.Companion.bytes
import io.github.ardiien.datasize.DataSize.Companion.gigabytes
import io.github.ardiien.datasize.DataSize.Companion.kilobytes
import io.github.ardiien.datasize.DataSize.Companion.megabytes
import io.github.ardiien.datasize.DataSize.Companion.terabytes
import kotlin.test.Test
import kotlin.test.assertEquals


class DataSizeTest {

    @Test
    fun `negative number in DataSizeUnit init throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) {
            (-3).kilobytes
        }
    }

    @Test
    fun `negative number in toDataSize init throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) {
            (-3).toDataSize(DataSizeUnit.Kilobytes)
        }
    }

    @Test
    fun `zero byte object in number expression returns zero`() {
        val subject = DataSize.Zero

        val bytes = subject.inBytes
        val kilobytes = subject.inKilobytes
        val megabytes = subject.inMegabytes
        val gigabytes = subject.inGigabytes
        val terabytes = subject.inTerabytes

        assertEquals(0, bytes)
        assertEquals(0.0, kilobytes, 0.01)
        assertEquals(0.0, megabytes, 0.01)
        assertEquals(0.0, gigabytes, 0.01)
        assertEquals(0.0, terabytes, 0.01)
    }

    @Test
    fun `bytes convert to bytes`() {
        val expectedBytes = 104857600L // 100 Mb
        val subject = DataSize.convert(
            expectedBytes.toDouble(),
            DataSizeUnit.Bytes,
            DataSizeUnit.Bytes,
        )

        val result = subject.bytes.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `bytes convert to megabytes`() {
        val expectedBytes = 104857600L // 100 Mb
        val subject = DataSize.convert(
            expectedBytes.toDouble(),
            DataSizeUnit.Bytes,
            DataSizeUnit.Megabytes,
        )

        val result = subject.megabytes.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `megabytes convert to bytes`() {
        val expectedBytes = 104857600L
        val expectedBytesInMegabytes = 100.0
        val subject = DataSize.convert(
            expectedBytesInMegabytes,
            DataSizeUnit.Megabytes,
            DataSizeUnit.Bytes,
        )

        val result = subject.bytes.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `bytes express with toDataSize as bytes`() {
        val expectedBytes = 104857600L // 100 Mb
        val subject = expectedBytes.toDataSize(DataSizeUnit.Bytes)

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `megabytes express with toDataSize as bytes`() {
        val expectedBytes = 123207680L
        val expectedMegaBytes = 117.5
        val subject = expectedMegaBytes.toDataSize(DataSizeUnit.Megabytes)

        val resultBytes = subject.inBytes
        assertEquals(expectedBytes, resultBytes)

        val resultMegabytes = subject.inMegabytes
        assertEquals(expectedMegaBytes, resultMegabytes, 0.01)
    }

    @Test
    fun `bytes convert with toDouble to kilobytes`() {
        val expectedBytes = 123207680L
        val expectedKilobytes = 120320.0 // 117.5 Mb

        val subject = expectedBytes.bytes
        val result = subject.toDouble(DataSizeUnit.Kilobytes)

        assertEquals(expectedKilobytes, result, 0.01)
    }

    @Test
    fun `bytes convert with toInt to megabytes`() {
        val expectedBytes = 123207680L
        val expectedMegabytes = 117

        val subject = expectedBytes.bytes
        val result = subject.toInt(DataSizeUnit.Megabytes)

        assertEquals(expectedMegabytes, result)
    }

    @Test
    fun `kilobytes convert with toLong to bytes`() {
        val expectedBytes = 126164664320L
        val expectedKilobytes = 123207680L

        val subject = expectedKilobytes.kilobytes
        val result = subject.toLong(DataSizeUnit.Bytes)

        assertEquals(expectedBytes, result)
    }

    @Test
    fun `operator plus adds kilobytes`() {
        val expectedBytes = 4096L
        val subject = 2.kilobytes + 2.kilobytes

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `operator minus subtracts megabytes`() {
        val expectedBytes = 2097152L
        val subject = 4.megabytes - 2.megabytes

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `operator times whole number multiplication megabytes`() {
        val expectedMegabytes = 20.0
        val subject = 2.megabytes * 10

        val result = subject.inMegabytes
        assertEquals(expectedMegabytes, result, 0.01)
    }

    @Test
    fun `operator times decimal point multiplication kilobytes`() {
        val expectedKilobytes = 3.0
        val subject = 2.kilobytes * 1.5

        val result = subject.inKilobytes
        assertEquals(expectedKilobytes, result, 0.01)
    }

    @Test
    fun `operator div whole number divides kilobytes`() {
        val expectedKilobytes = 5.0
        val subject = 10.kilobytes / 2

        val result = subject.inKilobytes
        assertEquals(expectedKilobytes, result, 0.01)
    }

    @Test
    fun `operator div decimal point divides megabytes`() {
        val expectedMegabytes = 7.33
        val subject = 11.megabytes / 1.5

        val result = subject.inMegabytes
        assertEquals(expectedMegabytes, result, 0.01)
    }

    @Test
    fun `operator div object divides bytes`() {
        val expectedBytes = 10.24
        val result = 1.kilobytes / 100.bytes

        assertEquals(expectedBytes, result, 0.01)
    }

    @Test
    fun `operator div object by zero throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            1.kilobytes / 0
        }
        assertThrows(IllegalArgumentException::class.java) {
            1.kilobytes / 0.0
        }
    }

    @Test
    fun `operator div object by zero throws err if used ZERO`() {
        assertThrows(IllegalArgumentException::class.java) {
            1.kilobytes / DataSize.Zero
        }
    }

    @Test
    fun `operator div DataSizeUnit divided by DataSizeUnit`() {
        val expected = 10_485.76
        val result = 1.terabytes / 100.megabytes

        assertEquals(expected, result, 0.01)
    }

    @Test
    fun `megabytes toString with DataSizeUnit no decimals`() {
        val expected = "100 MB"
        val result = 100.megabytes.toString(DataSizeUnit.Megabytes)

        assertEquals(expected, result)
    }

    @Test
    fun `megabytes toString with DataSizeUnit and decimals`() {
        val expected = "100,55 MB"
        val result = 100.55.megabytes.toString(DataSizeUnit.Megabytes, decimals = 2)

        assertEquals(expected, result)
    }

    @Test
    fun `megabytes toString with kilobytes DataSizeUnit no decimals`() {
        val expected = "102.400 KB"
        val result = 100.megabytes.toString(DataSizeUnit.Kilobytes)

        assertEquals(expected, result)
    }

    @Test
    fun `terabytes toString with gigabytes DataSizeUnit no decimals`() {
        val expected = "1024 GB"
        val result = 1.terabytes.toString(DataSizeUnit.Gigabytes)

        assertEquals(expected, result)
    }

    @Test
    @OptIn(ExperimentalDataSizeApi::class)
    fun `megabytes toDecimalString with DataSizeUnit no decimals`() {
        val expected = "500 MB"
        val result = 476.84.megabytes.toDecimalString(DataSizeUnit.Megabytes)

        assertEquals(expected, result)
    }

    @Test
    @OptIn(ExperimentalDataSizeApi::class)
    fun `megabytes toDecimalString with DataSizeUnit and decimals`() {
        val expected = "512,5 MB"
        val result = 488.755.megabytes.toDecimalString(DataSizeUnit.Megabytes, decimals = 1)

        assertEquals(expected, result)
    }

    @Test
    @OptIn(ExperimentalDataSizeApi::class)
    fun `megabytes toDecimalString with kilobytes DataSizeUnit no decimals`() {
        val expected = "1000 KB"
        val result = 0.954.megabytes.toDecimalString(DataSizeUnit.Kilobytes)

        assertEquals(expected, result)
    }

    @Test
    @OptIn(ExperimentalDataSizeApi::class)
    fun `terabytes toDecimalString with gigabytes DataSizeUnit no decimals`() {
        val expected = "255 GB"
        val result = 0.232.terabytes.toDecimalString(DataSizeUnit.Gigabytes)

        assertEquals(expected, result)
    }

    @Test
    fun `megabytes toBinaryUnit of megabytes`() {
        val expected = 476.84
        val result = toBinaryUnit(500.0, DataSizeUnit.Megabytes)

        assertEquals(expected, result, 0.01)
    }

    @Test
    fun `gigabytes toBinaryUnit of terabytes`() {
        val expected = 0.232
        val subject = toBinaryUnit(255, DataSizeUnit.Gigabytes).gigabytes
        val result = subject.inTerabytes

        assertEquals(expected, result, 0.01)
    }

    @Test
    @OptIn(ExperimentalDataSizeApi::class)
    fun `kilobytes toDecimalUnit plus megabytes toBinaryUnit`() {
        val expected = 2_000_000L
        val subjectKilobytes =
            toBinaryUnit(
                // technically possible, but requires more precision.
                toDecimalUnit(976.5621, DataSizeUnit.Kilobytes),
                DataSizeUnit.Kilobytes,
            ).kilobytes
        val subjectMegabytes = toBinaryUnit(1, DataSizeUnit.Megabytes).megabytes
        val result = subjectKilobytes + subjectMegabytes

        assertEquals(expected, result.inBytes)
    }

    @Test
    fun `compareTo returns correct value`() {
        val expectedLess = -1
        val expectedEqual = 0
        val expectedGreater = 1

        val lessResult = 123.kilobytes.compareTo(123.gigabytes)
        val equalResult = 123.megabytes.compareTo(123.megabytes)
        val greaterResult = 123.megabytes.compareTo(123.kilobytes)

        assertEquals(expectedLess, lessResult)
        assertEquals(expectedEqual, equalResult)
        assertEquals(expectedGreater, greaterResult)
    }

    @Test
    fun `show correct decimalSeparator`() {
        val expected = "1,1 MB"
        val result = 1.1.megabytes.toString(unit = DataSizeUnit.Megabytes, decimals = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `show correct groupingSeparator`() {
        val expected = "10.000 MB"
        val result = 10_000.megabytes.toString(unit = DataSizeUnit.Megabytes, decimals = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `show correct groupingSeparator with floating point`() {
        val expected = "100.000,5 MB"
        val result = 100_000.5.megabytes.toString(unit = DataSizeUnit.Megabytes, decimals = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `hide groupingSeparator when groupingSize less then 3`() {
        val expected = "1000 MB"
        val result = 1000.megabytes.toString(unit = DataSizeUnit.Megabytes, decimals = 1)
        assertEquals(expected, result)
    }

    @Test
    fun `orZero returns 0 bytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0, x.orZero().inBytes)
    }

    @Test
    fun `orZero returns 0 kilobytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orZero().inKilobytes, 0.0)
    }

    @Test
    fun `orZero returns 0 megabytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orZero().inMegabytes, 0.0)
    }

    @Test
    fun `orZero returns 0 gigabytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orZero().inGigabytes, 0.0)
    }

    @Test
    fun `orZero returns 0 terabytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orZero().inTerabytes, 0.0)
    }
}