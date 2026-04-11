/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.DataSize.Companion.bytes
import io.github.ardiien.datasize.DataSize.Companion.gibibytes
import io.github.ardiien.datasize.DataSize.Companion.kibibytes
import io.github.ardiien.datasize.DataSize.Companion.kilobytes
import io.github.ardiien.datasize.DataSize.Companion.mebibytes
import io.github.ardiien.datasize.DataSize.Companion.megabytes
import io.github.ardiien.datasize.DataSize.Companion.tebibytes
import io.github.ardiien.datasize.DataSize.Companion.toDataSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Ignore
import kotlin.test.Test


class DataSizeTest {

    @Test
    fun `negative value throws on DataSizeUnit init`() {
        assertThrows(IllegalArgumentException::class.java) {
            (-3).kilobytes
        }
        assertThrows(IllegalArgumentException::class.java) {
            (-3).kibibytes
        }
    }

    @Test
    fun `negative value throws on toDataSize`() {
        assertThrows(IllegalArgumentException::class.java) {
            (-3).toDataSize(SiUnit.Kilobyte)
        }
        assertThrows(IllegalArgumentException::class.java) {
            (-3).toDataSize(IecUnit.Kibibyte)
        }
    }

    @Test
    fun `zero DataSize returns zero for all units`() {
        val subject = DataSize.Zero

        val bytes = subject.inBytes
        val kilobytes = subject.inKilobytes
        val kibibytes = subject.inKibibytes
        val megabytes = subject.inMegabytes
        val mebibytes = subject.inMebibytes
        val gigabytes = subject.inGigabytes
        val gibibytes = subject.inGibibytes
        val terabytes = subject.inTerabytes
        val tebibytes = subject.inTebibytes
        val petabytes = subject.inPetabytes
        val pebibytes = subject.inPebibytes

        assertEquals(0, bytes)
        assertEquals(0.0, kilobytes, 0.01)
        assertEquals(0.0, kibibytes, 0.01)
        assertEquals(0.0, megabytes, 0.01)
        assertEquals(0.0, mebibytes, 0.01)
        assertEquals(0.0, gigabytes, 0.01)
        assertEquals(0.0, gibibytes, 0.01)
        assertEquals(0.0, terabytes, 0.01)
        assertEquals(0.0, tebibytes, 0.01)
        assertEquals(0.0, petabytes, 0.01)
        assertEquals(0.0, pebibytes, 0.01)
    }

    @Test
    fun `toDataSize bytes keeps value`() {
        val expectedBytes = 104857600L // 100 Mb
        val subject = expectedBytes.toDataSize(ByteUnit.Byte)

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `toDataSize mebibytes converts correctly`() {
        val expectedBytes = 123207680L
        val expectedMegaBytes = 117.5
        val subject = expectedMegaBytes.toDataSize(IecUnit.Mebibyte)

        val resultBytes = subject.inBytes
        assertEquals(expectedBytes, resultBytes)

        val resultMegabytes = subject.inMebibytes
        assertEquals(expectedMegaBytes, resultMegabytes, 0.01)
    }

    @Test
    fun `bytes toDouble kibibytes converts correctly`() {
        val expectedBytes = 123207680L
        val expectedKilobytes = 120320.0 // 117.5 Mb

        val subject = expectedBytes.bytes
        val result = subject.toDouble(IecUnit.Kibibyte)

        assertEquals(expectedKilobytes, result, 0.01)
    }

    @Test
    fun `kibibytes toLong bytes converts correctly`() {
        val expectedBytes = 126164664320L
        val expectedKilobytes = 123207680L

        val subject = expectedKilobytes.kibibytes
        val result = subject.toLong()

        assertEquals(expectedBytes, result)
    }

    @Test
    fun `plus adds kibibytes correctly`() {
        val expectedBytes = 4096L
        val subject = 2.kibibytes + 2.kibibytes

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `plus adds max bytes correctly`() {
        val expectedBytes = Long.MAX_VALUE
        val subject = Long.MAX_VALUE.bytes + Long.MAX_VALUE.bytes

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `minus subtracts mebibytes correctly`() {
        val expectedBytes = 2097152L
        val subject = 4.mebibytes - 2.mebibytes

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `minus subtracts max bytes correctly`() {
        val expectedBytes = DataSize.Zero.inBytes
        val subject = Long.MAX_VALUE.bytes - Long.MAX_VALUE.bytes

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `times int multiplies bytes`() {
        val expectedBytes = Long.MAX_VALUE
        val subject = Long.MAX_VALUE.bytes * 10

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `times int multiplies megabytes`() {
        val expectedMegabytes = 20.0
        val subject = 2.megabytes * 10

        val result = subject.inMegabytes
        assertEquals(expectedMegabytes, result, 0.01)
    }

    @Test
    @Ignore("Unsupported operation")
    fun `times decimal multiplies kilobytes`() {
        //val expectedKilobytes = 3.0
        //val subject = 2.kilobytes * 1.5

        //val result = subject.inKilobytes
        //assertEquals(expectedKilobytes, result, 0.01)
    }

    @Test
    fun `div int divides kibibytes`() {
        val expectedKilobytes = 5.0
        val subject = 10.kibibytes / 2

        val result = subject.inKibibytes
        assertEquals(expectedKilobytes, result, 0.01)
    }

    @Test
    @Ignore("Unsupported operation")
    fun `div decimal divides megabytes`() {
        //val expectedMegabytes = 7.33
        //val subject = 11.decimal.megabytes / 1.5

        //val result = subject.inMegabytes
        //assertEquals(expectedMegabytes, result, 0.01)
    }

    @Test
    fun `div by zero scalar throws`() {
        assertThrows(IllegalArgumentException::class.java) {
            1.kibibytes / 0
        }
        assertThrows(IllegalArgumentException::class.java) {
            1.kilobytes / 0
        }
    }

    @Test
    fun `compareTo orders correctly`() {
        val expectedLess = -1
        val expectedEqual = 0
        val expectedGreater = 1

        val lessResult = 123.kibibytes.compareTo(123.gibibytes)
        val equalResult = 123.mebibytes.compareTo(123.mebibytes)
        val greaterResult = 123.megabytes.compareTo(123.kilobytes)

        assertEquals(expectedLess, lessResult)
        assertEquals(expectedEqual, equalResult)
        assertEquals(expectedGreater, greaterResult)
    }

    @Test
    fun `toString megabytes no decimals`() {
        val expected = "100 MB"
        val result = 100.megabytes.toString(SiUnit.Megabyte)

        assertEquals(expected, result)
    }

    @Test
    fun `toString mebibytes with decimals`() {
        val expected = "100,55 MB"
        val result = 100.55.mebibytes.toString(IecUnit.Mebibyte, fractionDigits = 2)

        assertEquals(expected, result)
    }

    @Test
    fun `toString mebibytes as kibibytes`() {
        val expected = "102 400 KB"
        val result = 100.mebibytes.toString(IecUnit.Kibibyte)

        assertEquals(expected, result)
    }

    @Test
    fun `toString tebibytes as gibibytes`() {
        val expected = "1024 GB"
        val result = 1.tebibytes.toString(IecUnit.Gibibyte)

        assertEquals(expected, result)
    }

    @Test
    fun `toString mebibytes as decimal megabytes`() {
        val expected = "500 MB"
        val result = 476.84.mebibytes.toString(SiUnit.Megabyte)

        assertEquals(expected, result)
    }

    @Test
    fun `toString mebibytes as decimal megabytes with decimals`() {
        val expected = "512,5 MB"
        val result = 488.755.mebibytes.toString(SiUnit.Megabyte, fractionDigits = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `toString mebibytes as decimal kilobytes`() {
        val expected = "1000 KB"
        val result = 0.954.mebibytes.toString(SiUnit.Kilobyte)

        assertEquals(expected, result)
    }

    @Test
    fun `toString tebibytes as decimal gigabytes`() {
        val expected = "255 GB"
        val result = 0.232.tebibytes.toString(SiUnit.Gigabyte)

        assertEquals(expected, result)
    }

    @Test
    fun `ByteArray converts to DataSize correctly`() {
        val expected = "50 MB"
        val result = ByteArray(50 * 1024 * 1024).bytes.toString(IecUnit.Mebibyte)

        assertEquals(expected, result)
    }

    @Test
    fun `decimal separator formats correctly`() {
        val expected = "1,1 MB"
        val result = 1.1.mebibytes.toString(unit = IecUnit.Mebibyte, fractionDigits = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `grouping separator formats correctly`() {
        val expected = "10 000 MB"
        val result = 10_000.mebibytes.toString(unit = IecUnit.Mebibyte, fractionDigits = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `grouping with decimals formats correctly`() {
        val expected = "100 000,5 MB"
        val result = 100_000.5.mebibytes.toString(unit = IecUnit.Mebibyte, fractionDigits = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `no grouping when size less than three`() {
        val expected = "1000 MB"
        val result = 1000.mebibytes.toString(unit = IecUnit.Mebibyte, fractionDigits = 1)
        assertEquals(expected, result)
    }

    @Test
    fun `toString correctly formats bytes`() {
        val expected = "10 000 B"
        val result = 10_000.bytes.toString(unit = ByteUnit.Byte, fractionDigits = 0)
        assertEquals(expected, result)
    }

    @Test
    fun `toBinaryString correctly formats bytes`() {
        val expected = "500 B"
        val result = 500.bytes.toIecString()
        assertEquals(expected, result)
    }

    @Test
    fun `toDecimalString correctly formats bytes`() {
        val expected = "500 B"
        val result = 500.bytes.toSiString()
        assertEquals(expected, result)
    }

    @Test
    fun `orBinaryZero returns zero bytes for null`() {
        val x: DataSize? = null
        assertEquals(0, x.orZero().inBytes)
    }

    @Test
    fun `orDecimalZero returns zero kilobytes for null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orZero().inKilobytes, 0.0)
    }

    @Test
    fun `orBinaryZero returns zero mebibytes for null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orZero().inMebibytes, 0.0)
    }

    @Test
    fun `orDecimalZero returns zero gigabytes for null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orZero().inGigabytes, 0.0)
    }

    @Test
    fun `orBinaryZero returns zero terabytes for null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orZero().inTerabytes, 0.0)
    }

    @Test
    fun `orDecimalZero returns zero petabytes for null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orZero().inPetabytes, 0.0)
    }
}