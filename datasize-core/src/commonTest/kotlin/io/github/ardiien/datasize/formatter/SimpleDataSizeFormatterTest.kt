/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.formatter

import de.infix.testBalloon.framework.core.testSuite
import io.github.ardiien.datasize.DataSize.Companion.bytes
import io.github.ardiien.datasize.DataSize.Companion.gibibytes
import io.github.ardiien.datasize.DefaultDataSizeUnitFormatter
import io.github.ardiien.datasize.IecUnit
import kotlin.test.assertEquals


val SimpleDataSizeFormatterTestSuite by testSuite {
    testFixture { DefaultDataSizeUnitFormatter } asContextForEach {
        test("binaryFormat with zero fraction digits returns correct string") {
            val expected = "5 GB"

            val size = 5.32.gibibytes
            val actual = binaryFormat(
                value = size,
                unit = IecUnit.Gibibyte,
                fractionDigits = 0,
            )

            assertEquals(expected, actual)
        }

        test("binaryFormat with one fraction digit returns correct string") {
            val expected = "5,3 GB"

            val size = 5.32.gibibytes
            val actual = binaryFormat(
                value = size,
                unit = IecUnit.Gibibyte,
                fractionDigits = 1,
            )

            assertEquals(expected, actual)
        }

        test("binaryFormat with two fraction digits returns correct string") {
            val expected = "5,32 GB"

            val size = 5.32.gibibytes
            val actual = binaryFormat(
                value = size,
                unit = IecUnit.Gibibyte,
                fractionDigits = 2,
            )

            assertEquals(expected, actual)
        }

        test("binaryFormat returns correct string on zero bytes") {
            val expected = "0 B"

            val size = 0.bytes
            val actual = binaryFormat(value = size)

            assertEquals(expected, actual)
        }
    }
}
