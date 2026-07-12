/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.formatter

import io.github.ardiien.datasize.DataSize
import io.github.ardiien.datasize.DataSizeFormatter
import io.github.ardiien.datasize.DataSizeUnit
import io.github.ardiien.datasize.DataSizeUnitLocalizer
import io.github.ardiien.datasize.IecCompatibleUnit
import io.github.ardiien.datasize.IecUnit
import io.github.ardiien.datasize.SiCompatibleUnit
import io.github.ardiien.datasize.SiUnit
import io.github.ardiien.datasize.util.castTo
import kotlinx.collections.immutable.ImmutableList
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


/**
 * Default implementation of [DataSizeFormatter] that formats values
 * using a configurable [DecimalFormat] and [DataSizeUnitLocalizer].
 *
 * This formatter supports both decimal (SI) and binary (IEC) units and
 * automatically selects an appropriate unit when none is provided.
 */
public class SimpleDataSizeFormatter(
    private val format: DecimalFormat,
    private val localizer: DataSizeUnitLocalizer,
) : DataSizeFormatter {

    /** Selects the most appropriate unit for the given [value] from the provided [units]. */
    private fun unitFrom(value: DataSize, units: ImmutableList<DataSizeUnit>): DataSizeUnit =
        units.reversed().firstOrNull { value.rawValue >= it.value() } ?: units.first()

    /**
     * Formats the given [value] using the specified [unit] and [fractionDigits].
     * @throws IllegalArgumentException if [fractionDigits] is negative.
     */
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
        unit: SiCompatibleUnit?,
        fractionDigits: Int,
    ): String {
        val unit = unit?.castTo() ?: unitFrom(value, SiUnit.entries().castTo())
        return format(value, unit, fractionDigits)
    }

    public override fun binaryFormat(
        value: DataSize,
        unit: IecCompatibleUnit?,
        fractionDigits: Int,
    ): String {
        val unit = unit?.castTo() ?: unitFrom(value, IecUnit.entries().castTo())
        return format(value, unit, fractionDigits)
    }

    public companion object {

        /**
         * Creates a configured [DecimalFormat] instance for formatting data sizes.
         *
         * @param roundingMode rounding strategy to apply.
         * @param groupingSize number of digits in each group.
         * @param isGroupingUsed whether digit grouping is enabled.
         * @param decimalFormatSymbols symbols used for decimal and grouping separators.
         */
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

        /** Extends the given [format] with settings derived from the provided [number] and [fractionDigits]. */
        internal fun extendFormat(format: DecimalFormat, number: Double, fractionDigits: Int): DecimalFormat =
            format.apply {
                maximumFractionDigits = fractionDigits.coerceAtMost(2)
                isGroupingUsed = number > 9999.99999999999
            }

        /**
         * Creates [DecimalFormatSymbols] with the specified separators.
         *
         * @param decimalSeparator the character used as a decimal separator.
         * @param groupingSeparator the character used as a grouping separator.
         */
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