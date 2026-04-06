/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.DataSize.Companion.binary
import io.github.ardiien.datasize.DataSize.Companion.decimal
import io.github.ardiien.datasize.unit.BinaryUnit
import io.github.ardiien.datasize.unit.DecimalUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.Test


class DataSizeTest {

    @Test
    fun `negative number in DataSizeUnit init throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) {
            (-3).decimal.kilobytes
        }
        assertThrows(IllegalStateException::class.java) {
            (-3).binary.kibibytes
        }
    }

    @Test
    fun `negative number in toDataSize init throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) {
            (-3).decimal.toDataSize(DecimalUnit.Kilobyte)
        }
        assertThrows(IllegalStateException::class.java) {
            (-3).binary.toDataSize(BinaryUnit.Kibibyte)
        }
    }

    @Test
    fun `zero byte object in number expression returns zero`() {
        val subjectBinary = DataSize.binary.Zero
        val subjectDecimal = DataSize.decimal.Zero

        val binaryBytes = subjectBinary.inBytes
        val decimalBytes = subjectDecimal.inBytes
        val kilobytes = subjectBinary.inKilobytes
        val kibibytes = subjectBinary.inKibibytes
        val megabytes = subjectBinary.inMegabytes
        val mebibytes = subjectBinary.inMebibytes
        val gigabytes = subjectBinary.inGigabytes
        val gibibytes = subjectBinary.inGibibytes
        val terabytes = subjectBinary.inTerabytes
        val tebibytes = subjectBinary.inTebibytes
        val petabytes = subjectBinary.inPetabytes
        val pebibytes = subjectBinary.inPebibytes

        assertEquals(0, binaryBytes)
        assertEquals(0, decimalBytes)
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
    fun `bytes convert to bytes`() {
        val expectedBytes = 104857600L // 100 Mb
        val subject = DataSize.convert(
            value = expectedBytes.binary.bytes,
            targetUnit = BinaryUnit.Byte,
        )

        val actual = subject.inBytes
        assertEquals(expectedBytes, actual)
    }

    @Test
    fun `bytes convert to megabytes`() {
        val expectedBytes = 104857600L // 100 Mb
        val subject = DataSize.convert(
            value = expectedBytes.binary.bytes,
            targetUnit = BinaryUnit.Mebibyte,
        )

        val actual = subject.inBytes
        assertEquals(expectedBytes, actual)
    }

    @Test
    fun `megabytes convert to bytes`() {
        val expectedBytes = 104857600L // 100 Mb

        val subject = DataSize.convert(
            100.binary.mebibytes,
            BinaryUnit.Byte,
        )

        val actual = subject.inBytes
        assertEquals(expectedBytes, actual)
    }

    @Test
    fun `bytes express with toDataSize as bytes`() {
        val expectedBytes = 104857600L // 100 Mb
        val subject = expectedBytes.binary.toDataSize(BinaryUnit.Byte)

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `megabytes express with toDataSize as bytes`() {
        val expectedBytes = 123207680L
        val expectedMegaBytes = 117.5
        val subject = expectedMegaBytes.binary.toDataSize(BinaryUnit.Mebibyte)

        val resultBytes = subject.inBytes
        assertEquals(expectedBytes, resultBytes)

        val resultMegabytes = subject.inMebibytes
        assertEquals(expectedMegaBytes, resultMegabytes, 0.01)
    }

    @Test
    fun `bytes convert with toDouble to kilobytes`() {
        val expectedBytes = 123207680L
        val expectedKilobytes = 120320.0 // 117.5 Mb

        val subject = expectedBytes.binary.bytes
        val result = subject.toDouble(BinaryUnit.Kibibyte)

        assertEquals(expectedKilobytes, result, 0.01)
    }

    @Test
    fun `bytes convert with toInt to megabytes`() {
        val expectedBytes = 123207680L
        val expectedMegabytes = 117

        val subject = expectedBytes.binary.bytes
        val result = subject.toInt(BinaryUnit.Mebibyte)

        assertEquals(expectedMegabytes, result)
    }

    @Test
    fun `kilobytes convert with toLong to bytes`() {
        val expectedBytes = 126164664320L
        val expectedKilobytes = 123207680L

        val subject = expectedKilobytes.binary.kibibytes
        val result = subject.toLong(BinaryUnit.Byte)

        assertEquals(expectedBytes, result)
    }

    @Test
    fun `operator plus adds kilobytes`() {
        val expectedBytes = 4096L
        val subject = 2.binary.kibibytes + 2.binary.kibibytes

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `operator minus subtracts megabytes`() {
        val expectedBytes = 2097152L
        val subject = 4.binary.mebibytes - 2.binary.mebibytes

        val result = subject.inBytes
        assertEquals(expectedBytes, result)
    }

    @Test
    fun `operator times whole number multiplication megabytes`() {
        val expectedMegabytes = 20.0
        val subject = 2.decimal.megabytes * 10

        val result = subject.inMegabytes
        assertEquals(expectedMegabytes, result, 0.01)
    }

    @Test
    fun `operator times decimal point multiplication kilobytes`() {
        val expectedKilobytes = 3.0
        val subject = 2.decimal.kilobytes * 1.5

        val result = subject.inKilobytes
        assertEquals(expectedKilobytes, result, 0.01)
    }

    @Test
    fun `operator div whole number divides kilobytes`() {
        val expectedKilobytes = 5.0
        val subject = 10.binary.kibibytes / 2

        val result = subject.inKibibytes
        assertEquals(expectedKilobytes, result, 0.01)
    }

    @Test
    fun `operator div decimal point divides megabytes`() {
        val expectedMegabytes = 7.33
        val subject = 11.decimal.megabytes / 1.5

        val result = subject.inMegabytes
        assertEquals(expectedMegabytes, result, 0.01)
    }

    @Test
    fun `operator div object divides bytes`() {
        val expectedBytes = 10L
        val result = 1.binary.kibibytes / 100.binary.bytes

        assertEquals(expectedBytes, result.inBytes)
    }

    @Test
    fun `operator div object by zero throws IllegalArgumentException`() {
        assertThrows(IllegalArgumentException::class.java) {
            1.binary.kibibytes / 0
        }
        assertThrows(IllegalArgumentException::class.java) {
            1.decimal.kilobytes / 0
        }

        assertThrows(IllegalArgumentException::class.java) {
            1.binary.kibibytes / 0.0
        }
        assertThrows(IllegalArgumentException::class.java) {
            1.decimal.kilobytes / 0.0
        }
    }

    @Test
    fun `operator div object by zero throws err if used ZERO`() {
        assertThrows(IllegalArgumentException::class.java) {
            1.binary.kibibytes / DataSize.binary.Zero
        }
        assertThrows(IllegalArgumentException::class.java) {
            1.decimal.kilobytes / DataSize.decimal.Zero
        }
    }

    @Test
    fun `operator div DataSizeUnit divided by DataSizeUnit`() {
        val expected = 10_485L
        val result = 1.binary.tebibytes / 100.binary.mebibytes

        assertEquals(expected, result.inBytes)
    }

    @Test
    fun `compareTo returns correct value`() {
        val expectedLess = -1
        val expectedEqual = 0
        val expectedGreater = 1

        val lessResult = 123.binary.kibibytes.compareTo(123.binary.gibibytes)
        val equalResult = 123.binary.mebibytes.compareTo(123.binary.mebibytes)
        val greaterResult = 123.decimal.megabytes.compareTo(123.decimal.kilobytes)

        assertEquals(expectedLess, lessResult)
        assertEquals(expectedEqual, equalResult)
        assertEquals(expectedGreater, greaterResult)
    }

    @Test
    fun `megabytes toString with DataSizeUnit no decimals`() {
        val expected = "100 MB"
        val result = 100.decimal.megabytes.toString(DecimalUnit.Megabyte)

        assertEquals(expected, result)
    }

    @Test
    fun `megabytes toString with DataSizeUnit and decimals`() {
        val expected = "100,55 MB"
        val result = 100.55.binary.mebibytes.toString(BinaryUnit.Mebibyte, fractionDigits = 2)

        assertEquals(expected, result)
    }

    @Test
    fun `megabytes toString with kilobytes DataSizeUnit no decimals`() {
        val expected = "102 400 KB"
        val result = 100.binary.mebibytes.toString(BinaryUnit.Kibibyte)

        assertEquals(expected, result)
    }

    @Test
    fun `terabytes toString with gigabytes DataSizeUnit no decimals`() {
        val expected = "1024 GB"
        val result = 1.binary.tebibytes.toString(BinaryUnit.Gibibyte)

        assertEquals(expected, result)
    }

    @Test
    fun `megabytes toString with DataSizeUnit swap no decimals`() {
        val expected = "500 MB"
        val result = 476.84.binary.mebibytes.toString(DecimalUnit.Megabyte)

        assertEquals(expected, result)
    }

    @Test
    fun `megabytes toString with DataSizeUnit swap and decimals`() {
        val expected = "512,5 MB"
        val result = 488.755.binary.mebibytes.toString(DecimalUnit.Megabyte, fractionDigits = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `megabytes toString with kilobytes DataSizeUnit swap no decimals`() {
        val expected = "1000 KB"
        val result = 0.954.binary.mebibytes.toString(DecimalUnit.Kilobyte)

        assertEquals(expected, result)
    }

    @Test
    fun `terabytes toString with gigabytes DataSizeUnit swap no decimals`() {
        val expected = "255 GB"
        val result = 0.232.binary.tebibytes.toString(DecimalUnit.Gigabyte)

        assertEquals(expected, result)
    }

    @Test
    fun `ByteArray converts to megabytes correctly`() {
        val expected = "50 MB"
        val result = ByteArray(50 * 1024 * 1024).binary.bytes.toString(BinaryUnit.Mebibyte)

        assertEquals(expected, result)
    }

    @Test
    fun `show correct decimalSeparator`() {
        val expected = "1,1 MB"
        val result = 1.1.binary.mebibytes.toString(unit = BinaryUnit.Mebibyte, fractionDigits = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `show correct groupingSeparator`() {
        val expected = "10 000 MB"
        val result = 10_000.binary.mebibytes.toString(unit = BinaryUnit.Mebibyte, fractionDigits = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `show correct groupingSeparator with floating point`() {
        val expected = "100 000,5 MB"
        val result = 100_000.5.binary.mebibytes.toString(unit = BinaryUnit.Mebibyte, fractionDigits = 1)

        assertEquals(expected, result)
    }

    @Test
    fun `hide groupingSeparator when groupingSize less then 3`() {
        val expected = "1000 MB"
        val result = 1000.binary.mebibytes.toString(unit = BinaryUnit.Mebibyte, fractionDigits = 1)
        assertEquals(expected, result)
    }

    @Test
    fun `orZero returns 0 bytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0, x.orBinaryZero().inBytes)
    }

    @Test
    fun `orZero returns 0 kilobytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orDecimalZero().inKilobytes, 0.0)
    }

    @Test
    fun `orZero returns 0 megabytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orBinaryZero().inMebibytes, 0.0)
    }

    @Test
    fun `orZero returns 0 gigabytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orDecimalZero().inGigabytes, 0.0)
    }

    @Test
    fun `orZero returns 0 terabytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orBinaryZero().inTerabytes, 0.0)
    }

    @Test
    fun `orZero returns 0 petabytes when value is null`() {
        val x: DataSize? = null
        assertEquals(0.0, x.orDecimalZero().inPetabytes, 0.0)
    }
}