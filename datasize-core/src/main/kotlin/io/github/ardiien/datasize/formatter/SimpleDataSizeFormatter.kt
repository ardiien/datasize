/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.formatter

import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSizeFormatter
import io.github.ardiien.datasize.DataSizeUnitLocalizer
import io.github.ardiien.datasize.unit.DataSizeUnit
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


public class SimpleDataSizeFormatter(
    private val format: DecimalFormat,
    private val localizer: DataSizeUnitLocalizer,
) : DataSizeFormatter {

    public override fun unitFrom(value: DataSize): DataSizeUnit {
        val units = value.unit.entries()
        return units.firstOrNull { value.rawValue > it.value() } ?: units.last()
    }

    public override fun format(
        value: DataSize,
        unit: DataSizeUnit,
        fractionDigits: Int,
    ): String {
        require(fractionDigits >= 0) { "fractionDigits must not be negative, but was $fractionDigits" }

        val number = value.toDouble(unit)
        if (number.isInfinite()) return number.toString()

        val actualFormatter = extendFormat(format, number, fractionDigits)
        return "${actualFormatter.format(number)} ${localizer.abbreviation(unit)}"
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

        internal fun extendFormat(format: DecimalFormat, number: Double, fractionDigits: Int): DecimalFormat =
            format.apply {
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