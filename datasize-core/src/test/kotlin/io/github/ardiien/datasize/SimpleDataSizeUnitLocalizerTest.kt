/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.unit.BinaryUnit
import io.github.ardiien.datasize.unit.DataSizeUnit
import io.github.ardiien.datasize.unit.DecimalUnit
import io.github.ardiien.datasize.unit.localaizer.SimpleDataSizeUnitLocalizer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class SimpleDataSizeUnitLocalizerTest {

    private val localizer: SimpleDataSizeUnitLocalizer = SimpleDataSizeUnitLocalizer()
    private val strings = listOf(
        SimpleDataSizeUnitLocalizer.Petabyte,
        SimpleDataSizeUnitLocalizer.Terabyte,
        SimpleDataSizeUnitLocalizer.Gigabyte,
        SimpleDataSizeUnitLocalizer.Megabyte,
        SimpleDataSizeUnitLocalizer.Kilobyte,
        SimpleDataSizeUnitLocalizer.Byte,
    )

    @Test
    fun `name returns correct string for DataSizeUnit`() {
        val (expected, actual) = createExpectedActualMaps(
            expectedString = { strings[it].first },
            actualString = { localizer.name(it) },
        )

        assertEquals(expected, actual)
    }

    @Test
    fun `abbreviation returns correct string for DataSizeUnit`() {
        val (expected, actual) = createExpectedActualMaps(
            expectedString = { strings[it].second },
            actualString = { localizer.abbreviation(it) },
        )

        assertEquals(expected, actual)
    }

    private fun createExpectedActualMaps(
        expectedString: (Int) -> String,
        actualString: (DataSizeUnit) -> String,
    ): Pair<Map<Double, String>, Map<Double, String>> {
        val binaryStep = 10
        val decimalStep = 3

        // Should produce a map where key = (base + exponent), value = name; e.g., [2.0 = bytes, 10.0 = bytes]
        val expected = BinaryUnit.Byte.entries().associate {
            val index = (BinaryUnit.Pebibyte.exponent - it.exponent) / binaryStep
            (it.base + it.exponent) to expectedString(index)
        }.plus(
            DecimalUnit.Byte.entries().associate {
                val index = (DecimalUnit.Petabyte.exponent - it.exponent) / decimalStep
                (it.base + it.exponent) to expectedString(index)
            }
        )

        // Does the same as above but takes actual names for values from localizer.
        val actual = BinaryUnit.Byte.entries().associate {
            (it.base + it.exponent) to actualString(it)
        }.plus(
            DecimalUnit.Byte.entries().associate {
                (it.base + it.exponent) to actualString(it)
            }
        )

        return expected to actual
    }
}