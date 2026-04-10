/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize


/** Provides utilities for formatting [DataSize] values into human-readable strings. */
@ExperimentalDataSizeApi
public interface DataSizeFormatter {

    /**
     * Returns a string representation of the given [value] expressed in the specified [unit]
     * and formatted with the given number of fractional [fractionDigits].
     *
     * If [unit] is not provided, a suitable unit is selected automatically.
     *
     * @param value the data size to format.
     * @param unit the unit to express the value in.
     * @param fractionDigits the number of digits to display after the decimal point.
     *
     * @throws IllegalStateException if the value is not within the supported range.
     * @throws IllegalArgumentException if [fractionDigits] is negative.
     */
    public fun binaryFormat(
        value: DataSize,
        unit: BinaryUnit? = null,
        fractionDigits: Int = 0,
    ): String

    /**
     * Returns a string representation of the given [value] expressed in the specified [unit]
     * and formatted with the given number of fractional [fractionDigits].
     *
     * If [unit] is not provided, a suitable unit is selected automatically.
     *
     * @param value the data size to format.
     * @param unit the unit to express the value in.
     * @param fractionDigits the number of digits to display after the decimal point.
     *
     * @throws IllegalStateException if the value is not within the supported range.
     * @throws IllegalArgumentException if [fractionDigits] is negative.
     */
    public fun decimalFormat(
        value: DataSize,
        unit: DecimalUnit? = null,
        fractionDigits: Int = 0,
    ): String
}