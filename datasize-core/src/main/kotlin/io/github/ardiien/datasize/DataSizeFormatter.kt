/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.unit.DataSizeUnit


/** Provides utilities for formatting [DataSize] values into human-readable strings. */
public interface DataSizeFormatter {

    /**
     * Returns the most appropriate [DataSizeUnit] for the given [value] based on its magnitude.
     * The selected unit is the largest unit whose byte value does not exceed the given [value].
     *
     * @throws IllegalStateException if the value is not within the supported range
     */
    public fun unitFrom(value: DataSize): DataSizeUnit

    /**
     * Returns a string representation of the given [value] expressed in the specified [unit]
     * and formatted with the given number of fractional [fractionDigits].
     *
     * If [unit] is not provided, a suitable unit is selected automatically using [unitFrom].
     *
     * @param value the data size to format.
     * @param unit the unit to express the value in.
     * @param fractionDigits the number of digits to display after the decimal point.
     *
     * @throws IllegalStateException if the value is not within the supported range.
     * @throws IllegalArgumentException if [fractionDigits] is negative.
     */
    public fun format(
        value: DataSize,
        unit: DataSizeUnit = unitFrom(value),
        fractionDigits: Int = 0,
    ): String
}