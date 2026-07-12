/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.invocation
import de.infix.testBalloon.framework.core.testSuite
import io.github.ardiien.datasize.DataSize.Companion.MaxValue
import io.github.ardiien.datasize.DataSize.Companion.bytes
import io.github.ardiien.datasize.DataSize.Companion.gibibytes
import io.github.ardiien.datasize.DataSize.Companion.gigabytes
import io.github.ardiien.datasize.DataSize.Companion.kibibytes
import io.github.ardiien.datasize.DataSize.Companion.kilobytes
import io.github.ardiien.datasize.DataSize.Companion.mebibytes
import io.github.ardiien.datasize.DataSize.Companion.megabytes
import io.github.ardiien.datasize.DataSize.Companion.tebibytes
import io.github.ardiien.datasize.DataSize.Companion.terabytes
import io.github.ardiien.datasize.DataSize.Companion.toDataSize
import org.junit.jupiter.api.Assertions.assertThrows
import kotlin.test.assertEquals


val DataSizeTestSuite by testSuite {
    val concurrentTestConfig = TestConfig.invocation(TestConfig.Invocation.Concurrent)

    testSuite(name = "SI Unit") {
        test("throws on negative value") {
            assertThrows(IllegalArgumentException::class.java) {
                (-3).kilobytes
            }
            assertThrows(IllegalArgumentException::class.java) {
                (-3).toDataSize(SiUnit.Kilobyte)
            }
        }

        test("zero bytes return zero for all units") {
            val subject = DataSize.Zero

            val kilobytes = subject.inKilobytes
            val megabytes = subject.inMegabytes
            val gigabytes = subject.inGigabytes
            val terabytes = subject.inTerabytes
            val petabytes = subject.inPetabytes

            assertEquals(0.0, kilobytes, 0.01)
            assertEquals(0.0, megabytes, 0.01)
            assertEquals(0.0, gigabytes, 0.01)
            assertEquals(0.0, terabytes, 0.01)
            assertEquals(0.0, petabytes, 0.01)
        }

        test("ByteArray converts to DataSize correctly", testConfig = concurrentTestConfig) {
            val expected = "50 MB"
            val result = ByteArray(50 * 1000 * 1000).bytes.toString(SiUnit.Megabyte)

            assertEquals(expected, result)
        }

        test("toDataSize megabytes converts correctly") {
            val expectedBytes = 117500000L
            val expectedMegaBytes = 117.5
            val subject = expectedMegaBytes.toDataSize(SiUnit.Megabyte)

            val resultBytes = subject.inBytes
            assertEquals(expectedBytes, resultBytes)

            val resultMegabytes = subject.inMegabytes
            assertEquals(expectedMegaBytes, resultMegabytes, 0.01)
        }

        test("bytes toDouble kilobytes converts correctly") {
            val expectedBytes = 117500000L
            val expectedKilobytes = 117500.0 // 117.5 Mb

            val subject = expectedBytes.bytes
            val result = subject.toDouble(SiUnit.Kilobyte)

            assertEquals(expectedKilobytes, result, 0.01)
        }

        test("kilobytes toLong bytes converts correctly") {
            val expectedBytes = 117500000000L
            val expectedKilobytes = 117500000L

            val subject = expectedKilobytes.kilobytes
            val result = subject.toLong()

            assertEquals(expectedBytes, result)
        }

        test("plus adds kilobytes correctly") {
            val expectedBytes = 4000L
            val subject = 2.kilobytes + 2.kilobytes

            val result = subject.inBytes
            assertEquals(expectedBytes, result)
        }

        test("minus subtracts megabytes correctly") {
            val expectedBytes = 2000000L
            val subject = 4.megabytes - 2.megabytes

            val result = subject.inBytes
            assertEquals(expectedBytes, result)
        }

        test("times int multiplies megabytes") {
            val expectedMegabytes = 20.0
            val subject = 2.megabytes * 10

            val result = subject.inMegabytes
            assertEquals(expectedMegabytes, result, 0.01)
        }

        test("times int multiplies by zero returns zero") {
            val expected = DataSize.Zero
            val actual = 2.megabytes * 0

            assertEquals(expected, actual)
        }

        test("div int divides kilobytes") {
            val expectedKilobytes = 5.0
            val subject = 10.kilobytes / 2

            val result = subject.inKilobytes
            assertEquals(expectedKilobytes, result, 0.01)
        }

        test("div by zero scalar throws") {
            assertThrows(IllegalArgumentException::class.java) {
                1.kilobytes / 0
            }
        }

        test("compareTo orders data correctly") {
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

        test("orZero returns zero kilobytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inKilobytes
            assertEquals(expected, actual, 0.0)
        }

        test("orZero returns zero megabytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inMegabytes
            assertEquals(expected, actual, 0.0)
        }

        test("orZero returns zero gigabytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inGigabytes
            assertEquals(expected, actual, 0.0)
        }

        test("orZero returns zero terabytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inTerabytes
            assertEquals(expected, actual, 0.0)
        }

        test("orZero returns zero petabytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inPetabytes
            assertEquals(expected, actual, 0.0)
        }

        test("toSiString correctly formats bytes", testConfig = concurrentTestConfig) {
            val expected = "500 B"
            val result = 500.bytes.toSiString()
            assertEquals(expected, result)
        }

        test("toString megabytes no decimals", testConfig = concurrentTestConfig) {
            val expected = "100 MB"
            val result = 100.megabytes.toString(SiUnit.Megabyte)

            assertEquals(expected, result)
        }

        test("toString megabytes with decimals", testConfig = concurrentTestConfig) {
            val expected = "100,45 MB"
            val result = 100.45.megabytes.toString(SiUnit.Megabyte, fractionDigits = 2)

            assertEquals(expected, result)
        }

        test("toString megabytes as kilobytes", testConfig = concurrentTestConfig) {
            val expected = "100 000 KB"
            val result = 100.megabytes.toString(SiUnit.Kilobyte)

            assertEquals(expected, result)
        }

        test("toString terabytes as gigabytes", testConfig = concurrentTestConfig) {
            val expected = "1000 GB"
            val result = 1.terabytes.toString(SiUnit.Gigabyte)

            assertEquals(expected, result)
        }

        test("toString megabytes as decimal mebibytes", testConfig = concurrentTestConfig) {
            val expected = "477 MB"
            val result = 500.megabytes.toString(IecUnit.Mebibyte)

            assertEquals(expected, result)
        }

        test("toString megabytes as decimal mebibytes with decimals", testConfig = concurrentTestConfig) {
            val expected = "488,8 MB"
            val result = 512.5.megabytes.toString(IecUnit.Mebibyte, fractionDigits = 1)

            assertEquals(expected, result)
        }

        test("toString megabytes as decimal kibibytes", testConfig = concurrentTestConfig) {
            val expected = "977 KB"
            val result = 1.megabytes.toString(IecUnit.Kibibyte)

            assertEquals(expected, result)
        }

        test("toString terabytes as decimal gibibytes", testConfig = concurrentTestConfig) {
            val expected = "255 GB"
            val result = 0.249.tebibytes.toString(IecUnit.Gibibyte)

            assertEquals(expected, result)
        }

        test("decimal separator formats correctly", testConfig = concurrentTestConfig) {
            val expected = "1,1 MB"
            val result = 1.1.megabytes.toString(unit = SiUnit.Megabyte, fractionDigits = 1)

            assertEquals(expected, result)
        }

        test("grouping separator formats correctly", testConfig = concurrentTestConfig) {
            val expected = "10 000 MB"
            val result = 10_000.megabytes.toString(unit = SiUnit.Megabyte, fractionDigits = 1)

            assertEquals(expected, result)
        }

        test("grouping with decimals formats correctly", testConfig = concurrentTestConfig) {
            val expected = "100 000,5 MB"
            val result = 100_000.5.megabytes.toString(unit = SiUnit.Megabyte, fractionDigits = 1)

            assertEquals(expected, result)
        }

        test("no grouping when size less than three", testConfig = concurrentTestConfig) {
            val expected = "1000 MB"
            val result = 1000.megabytes.toString(unit = SiUnit.Megabyte, fractionDigits = 1)
            assertEquals(expected, result)
        }
    }

    testSuite(name = "IEC Unit") {
        test("throws on negative value") {
            assertThrows(IllegalArgumentException::class.java) {
                (-3).kibibytes
            }
            assertThrows(IllegalArgumentException::class.java) {
                (-3).toDataSize(IecUnit.Kibibyte)
            }
        }

        test("zero bytes return zero for all units") {
            val subject = DataSize.Zero

            val kibibytes = subject.inKibibytes
            val mebibytes = subject.inMebibytes
            val gibibytes = subject.inGibibytes
            val tebibytes = subject.inTebibytes
            val pebibytes = subject.inPebibytes

            assertEquals(0.0, kibibytes, 0.01)
            assertEquals(0.0, mebibytes, 0.01)
            assertEquals(0.0, gibibytes, 0.01)
            assertEquals(0.0, tebibytes, 0.01)
            assertEquals(0.0, pebibytes, 0.01)
        }

        test("ByteArray converts to DataSize correctly", testConfig = concurrentTestConfig) {
            val expected = "50 MB"
            val result = ByteArray(50 * 1024 * 1024).bytes.toString(IecUnit.Mebibyte)

            assertEquals(expected, result)
        }

        test("toDataSize mebibytes converts correctly") {
            val expectedBytes = 123207680L
            val expectedMegaBytes = 117.5
            val subject = expectedMegaBytes.toDataSize(IecUnit.Mebibyte)

            val resultBytes = subject.inBytes
            assertEquals(expectedBytes, resultBytes)

            val resultMegabytes = subject.inMebibytes
            assertEquals(expectedMegaBytes, resultMegabytes, 0.01)
        }

        test("bytes toDouble kibibytes converts correctly") {
            val expectedBytes = 123207680L
            val expectedKilobytes = 120320.0 // 117.5 Mb

            val subject = expectedBytes.bytes
            val result = subject.toDouble(IecUnit.Kibibyte)

            assertEquals(expectedKilobytes, result, 0.01)
        }

        test("kibibytes toLong bytes converts correctly") {
            val expectedBytes = 126164664320L
            val expectedKilobytes = 123207680L

            val subject = expectedKilobytes.kibibytes
            val result = subject.toLong()

            assertEquals(expectedBytes, result)
        }

        test("plus adds kibibytes correctly") {
            val expectedBytes = 4096L
            val subject = 2.kibibytes + 2.kibibytes

            val result = subject.inBytes
            assertEquals(expectedBytes, result)
        }

        test("minus subtracts mebibytes correctly") {
            val expectedBytes = 2097152L
            val subject = 4.mebibytes - 2.mebibytes

            val result = subject.inBytes
            assertEquals(expectedBytes, result)
        }

        test("times int multiplies mebibytes") {
            val expectedMegabytes = 20.0
            val subject = 2.mebibytes * 10

            val result = subject.inMebibytes
            assertEquals(expectedMegabytes, result, 0.01)
        }

        test("times int multiplies by zero returns zero") {
            val expected = DataSize.Zero
            val actual = 2.mebibytes * 0

            assertEquals(expected, actual)
        }

        test("div int divides kibibytes") {
            val expectedKilobytes = 5.0
            val subject = 10.kibibytes / 2

            val result = subject.inKibibytes
            assertEquals(expectedKilobytes, result, 0.01)
        }

        test("div by zero scalar throws") {
            assertThrows(IllegalArgumentException::class.java) {
                1.kibibytes / 0
            }
        }

        test("compareTo orders data correctly") {
            val expectedLess = -1
            val expectedEqual = 0
            val expectedGreater = 1

            val lessResult = 123.kibibytes.compareTo(123.gibibytes)
            val equalResult = 123.mebibytes.compareTo(123.mebibytes)
            val greaterResult = 123.mebibytes.compareTo(123.kibibytes)

            assertEquals(expectedLess, lessResult)
            assertEquals(expectedEqual, equalResult)
            assertEquals(expectedGreater, greaterResult)
        }

        test("orZero returns zero kibibytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inKibibytes
            assertEquals(expected, actual, 0.0)
        }

        test("orZero returns zero mebibytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inMebibytes
            assertEquals(expected, actual, 0.0)
        }

        test("orZero returns zero gibibytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inGibibytes
            assertEquals(expected, actual, 0.0)
        }

        test("orZero returns zero tebibytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inTebibytes
            assertEquals(expected, actual, 0.0)
        }

        test("orZero returns zero pebibytes when null") {
            val expected = 0.0
            val subject: DataSize? = null

            val actual = subject.orZero().inPebibytes
            assertEquals(expected, actual, 0.0)
        }

        test("toIecString correctly formats bytes", testConfig = concurrentTestConfig) {
            val expected = "500 B"
            val result = 500.bytes.toIecString()
            assertEquals(expected, result)
        }

        test("toString mebibytes no decimals", testConfig = concurrentTestConfig) {
            val expected = "100 MB"
            val result = 100.mebibytes.toString(IecUnit.Mebibyte)

            assertEquals(expected, result)
        }

        test("toString mebibytes with decimals", testConfig = concurrentTestConfig) {
            val expected = "100,55 MB"
            val result = 100.55.mebibytes.toString(IecUnit.Mebibyte, fractionDigits = 2)

            assertEquals(expected, result)
        }

        test("toString mebibytes as kibibytes", testConfig = concurrentTestConfig) {
            val expected = "102 400 KB"
            val result = 100.mebibytes.toString(IecUnit.Kibibyte)

            assertEquals(expected, result)
        }

        test("toString tebibytes as gibibytes", testConfig = concurrentTestConfig) {
            val expected = "1024 GB"
            val result = 1.tebibytes.toString(IecUnit.Gibibyte)

            assertEquals(expected, result)
        }

        test("toString mebibytes as decimal megabytes", testConfig = concurrentTestConfig) {
            val expected = "500 MB"
            val result = 476.84.mebibytes.toString(SiUnit.Megabyte)

            assertEquals(expected, result)
        }

        test("toString mebibytes as decimal megabytes with decimals", testConfig = concurrentTestConfig) {
            val expected = "512,5 MB"
            val result = 488.755.mebibytes.toString(SiUnit.Megabyte, fractionDigits = 1)

            assertEquals(expected, result)
        }

        test("toString mebibytes as decimal kilobytes", testConfig = concurrentTestConfig) {
            val expected = "1000 KB"
            val result = 0.954.mebibytes.toString(SiUnit.Kilobyte)

            assertEquals(expected, result)
        }

        test("toString tebibytes as decimal gigabytes", testConfig = concurrentTestConfig) {
            val expected = "255 GB"
            val result = 0.232.tebibytes.toString(SiUnit.Gigabyte)

            assertEquals(expected, result)
        }

        test("decimal separator formats correctly", testConfig = concurrentTestConfig) {
            val expected = "1,1 MB"
            val result = 1.1.mebibytes.toString(unit = IecUnit.Mebibyte, fractionDigits = 1)

            assertEquals(expected, result)
        }

        test("grouping separator formats correctly", testConfig = concurrentTestConfig) {
            val expected = "10 000 MB"
            val result = 10_000.mebibytes.toString(unit = IecUnit.Mebibyte, fractionDigits = 1)

            assertEquals(expected, result)
        }

        test("grouping with decimals formats correctly", testConfig = concurrentTestConfig) {
            val expected = "100 000,5 MB"
            val result = 100_000.5.mebibytes.toString(unit = IecUnit.Mebibyte, fractionDigits = 1)

            assertEquals(expected, result)
        }

        test(
            "no grouping when size less than three", testConfig = concurrentTestConfig
        ) {
            val expected = "1000 MB"
            val result = 1000.mebibytes.toString(unit = IecUnit.Mebibyte, fractionDigits = 1)
            assertEquals(expected, result)
        }
    }

    testSuite(name = "Unit Independent") {
        test("zero bytes return zero byte unit") {
            val subject = DataSize.Zero

            val actual = subject.inBytes
            assertEquals(0, actual)
        }

        test("toDataSize bytes keeps value") {
            val expectedBytes = 104857600L // 100 Mb
            val subject = expectedBytes.toDataSize(ByteUnit.Byte)

            val result = subject.inBytes
            assertEquals(expectedBytes, result)
        }

        test("plus adds max bytes correctly") {
            val expectedBytes = MaxValue.inBytes
            val subject = MaxValue + MaxValue

            val result = subject.inBytes
            assertEquals(expectedBytes, result)
        }

        test("minus subtracts max bytes correctly") {
            val expectedBytes = DataSize.Zero.inBytes
            val subject = Long.MAX_VALUE.bytes - Long.MAX_VALUE.bytes

            val result = subject.inBytes
            assertEquals(expectedBytes, result)
        }

        test("times int multiplies bytes") {
            val expectedBytes = MaxValue.inBytes
            val subject = MaxValue * 10

            val result = subject.inBytes
            assertEquals(expectedBytes, result)
        }

        test("toString correctly formats bytes", testConfig = concurrentTestConfig) {
            val expected = "10 000 B"

            val result = 10_000.bytes.toString(unit = ByteUnit.Byte, fractionDigits = 0)
            assertEquals(expected, result)
        }

        test("orZero returns zero bytes when null") {
            val expected = DataSize.Zero.inBytes
            val subject: DataSize? = null

            val actual = subject.orZero().inBytes
            assertEquals(expected, actual)
        }
    }
}
