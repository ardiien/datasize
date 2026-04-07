/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.formatter

import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSizeFormatter
import io.github.ardiien.datasize.unit.DataSizeUnit
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


public class SimpleDataSizeFormatter(private val formatter: DecimalFormat) : DataSizeFormatter {

    /**
     * Returns the most appropriate [DataSizeUnit] for the given [value] based on its magnitude.
     * The selected unit is the largest unit whose byte value does not exceed the given [value].
     *
     * @throws IllegalStateException if the value is not within the supported range
     */
    public override fun unitFrom(value: DataSize): DataSizeUnit {
        val units = value.unit.entries()
        return units.firstOrNull { value.rawValue > it.value() } ?: units.last()
    }

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
    public override fun format(
        value: DataSize,
        unit: DataSizeUnit,
        fractionDigits: Int,
    ): String {
        require(fractionDigits >= 0) { "fractionDigits must not be negative, but was $fractionDigits" }

        val number = value.toDouble(unit)
        if (number.isInfinite()) return number.toString()

        val actualFormatter = extendFormat(formatter, number, fractionDigits)
        return "${actualFormatter.format(number)} ${unit.abbreviation()}"
    }

    public companion object {

        public fun createFormat(
            roundingMode: RoundingMode = RoundingMode.HALF_UP,
            groupingSize: Int = 3,
            isGroupingUsed: Boolean = true,
            decimalFormatSymbols: DecimalFormatSymbols = createFormatSymbols(),
        ): DecimalFormat = DecimalFormat("0")
            .apply {
                this.roundingMode = roundingMode
                this.isGroupingUsed = isGroupingUsed
                this.groupingSize = groupingSize
                this.decimalFormatSymbols = decimalFormatSymbols
            }

        internal fun extendFormat(formatter: DecimalFormat, number: Double, fractionDigits: Int): DecimalFormat =
            formatter.apply {
                maximumFractionDigits = fractionDigits.coerceAtMost(2)
                isGroupingUsed = number > 9999.999999999
            }

        public fun createFormatSymbols(
            decimalSeparator: Char = ',',
            groupingSeparator: Char = ' ',
        ): DecimalFormatSymbols = DecimalFormatSymbols()
            .apply {
                this.decimalSeparator = decimalSeparator
                this.groupingSeparator = groupingSeparator
            }
    }
}