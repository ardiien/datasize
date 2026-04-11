/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize


/** Provides formatting utilities for converting [DataSize] values into human-readable strings. */
public interface DataSizeFormatter {

    /**
     * Returns a formatted string representation of the given [value] using binary units.
     *
     * The value is expressed in the specified [unit] and formatted with the given number
     * of fractional [fractionDigits]. If [unit] is `null`, a suitable unit is selected automatically.
     *
     * @param value the data size to format.
     * @param unit the binary unit to express the value in, or `null` to select automatically.
     * @param fractionDigits number of digits to display after the decimal point (must be non-negative).
     *
     * @throws IllegalStateException if the value is not within the supported range.
     * @throws IllegalArgumentException if [fractionDigits] is negative.
     */
    public fun binaryFormat(
        value: DataSize,
        unit: IecCompatibleUnit? = null,
        fractionDigits: Int = 0,
    ): String

    /**
     * Returns a formatted string representation of the given [value] using decimal units.
     *
     * The value is expressed in the specified [unit] and formatted with the given number
     * of fractional [fractionDigits]. If [unit] is `null`, a suitable unit is selected automatically.
     *
     * @param value the data size to format.
     * @param unit the decimal unit to express the value in, or `null` to select automatically.
     * @param fractionDigits number of digits to display after the decimal point (must be non-negative).
     *
     * @throws IllegalStateException if the value is not within the supported range.
     * @throws IllegalArgumentException if [fractionDigits] is negative.
     */
    public fun decimalFormat(
        value: DataSize,
        unit: SiCompatibleUnit? = null,
        fractionDigits: Int = 0,
    ): String
}