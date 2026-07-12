/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.localizer

import de.infix.testBalloon.framework.core.testSuite
import io.github.ardiien.datasize.DataSizeTag
import io.github.ardiien.datasize.DataSizeUnit
import io.github.ardiien.datasize.IecUnit
import io.github.ardiien.datasize.SiUnit
import io.github.ardiien.datasize.util.castTo
import kotlinx.collections.immutable.ImmutableList
import kotlin.test.assertEquals


val SimpleDataSizeUnitLocalizerTestSuite by testSuite {
    fun createExpectedActualMaps(
        entries: ImmutableList<DataSizeUnit>,
        expectedString: (DataSizeTag) -> String,
        actualString: (DataSizeUnit) -> String,
    ): Pair<List<String>, List<String>> {
        val expected = entries.map { expectedString(it.tag()) }
        val actual = entries.map { actualString(it) }

        return expected to actual
    }

    testFixture { SimpleDataSizeUnitLocalizer() } asContextForEach {
        test("name returns correct string for BinaryUnit") {
            val (expected, actual) = createExpectedActualMaps(
                entries = IecUnit.entries().castTo(),
                expectedString = { SimpleDataSizeUnitLocalizer.Labels[it]?.first.orEmpty() },
                actualString = { name(it) },
            )

            assertEquals(expected, actual)
        }
        test("name returns correct string for DecimalUnit") {
            val (expected, actual) = createExpectedActualMaps(
                entries = SiUnit.entries().castTo(),
                expectedString = { SimpleDataSizeUnitLocalizer.Labels[it]?.first.orEmpty() },
                actualString = { name(it) },
            )

            assertEquals(expected, actual)
        }

        test("abbreviation returns correct string for DecimalUnit") {
            val (expected, actual) = createExpectedActualMaps(
                entries = SiUnit.entries().castTo(),
                expectedString = { SimpleDataSizeUnitLocalizer.Labels[it]?.second.orEmpty() },
                actualString = { abbreviation(it) },
            )

            assertEquals(expected, actual)
        }

        test("abbreviation returns correct string for BinaryUnit") {
            val (expected, actual) = createExpectedActualMaps(
                entries = IecUnit.entries().castTo(),
                expectedString = { SimpleDataSizeUnitLocalizer.Labels[it]?.second.orEmpty() },
                actualString = { abbreviation(it) },
            )

            assertEquals(expected, actual)
        }
    }
}
