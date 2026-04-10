/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.formatter

import io.github.ardiien.datasize.BinaryUnit
import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSizeFormatter
import io.github.ardiien.datasize.DataSizeUnit
import io.github.ardiien.datasize.DataSizeUnitLocalizer
import io.github.ardiien.datasize.DecimalUnit
import kotlinx.collections.immutable.ImmutableList
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


public class SimpleDataSizeFormatter(
    private val format: DecimalFormat,
    private val localizer: DataSizeUnitLocalizer,
) : DataSizeFormatter {

    private fun unitFrom(value: DataSize, units: ImmutableList<DataSizeUnit>): DataSizeUnit =
        units.reversed().firstOrNull { value.rawValue > it.value() } ?: units.first()

    private fun format(
        value: DataSize,
        unit: DataSizeUnit,
        fractionDigits: Int,
    ): String {
        require(fractionDigits >= 0) { "fractionDigits must not be negative, but was $fractionDigits" }

        val number = value.toDouble(unit)
        val actualFormatter = extendFormat(format, number, fractionDigits)
        return "${actualFormatter.format(number)} ${localizer.abbreviation(unit)}"
    }


    public override fun decimalFormat(
        value: DataSize,
        unit: DecimalUnit?,
        fractionDigits: Int,
    ): String {
        val unit = unit ?: unitFrom(value, DecimalUnit.entries())
        return format(value, unit, fractionDigits)
    }

    public override fun binaryFormat(
        value: DataSize,
        unit: BinaryUnit?,
        fractionDigits: Int,
    ): String {
        val unit = unit ?: unitFrom(value, BinaryUnit.entries())
        return format(value, unit, fractionDigits)
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