/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import io.github.ardiien.datasize.DataSize.Companion.bytes


/** A utility class for formatting data size values in a human-readable form. */
public object DataSizeFormatter {

    /**
     * Returns [DataSizeUnit] from [value] depending on the threshold.
     *
     * @throws IllegalStateException value is not within 0 <= value <= [MAX_SIZE]
     */
    @JvmSynthetic
    public fun unitFrom(value: DataSize): DataSizeUnit =
        when {
            value.inBytes > TERA_SCALE -> DataSizeUnit.Terabytes
            value.inBytes > GIGA_SCALE -> DataSizeUnit.Gigabytes
            value.inBytes > MEGA_SCALE -> DataSizeUnit.Megabytes
            value.inBytes > KILO_SCALE -> DataSizeUnit.Kilobytes
            else -> DataSizeUnit.Bytes
        }

    /**
     * Returns a string representation of this data size [value] expressed in the given [unit]
     * and formatted with the specified [decimals] number of digits after decimal point.
     *
     * @return String representation of [value] in a custom format.
     * @throws IllegalStateException value is not within 0 <= value <= [MAX_SIZE]
     * @throws IllegalArgumentException if [decimals] is less than zero.
     * @see DataSize.toString
     */
    @JvmSynthetic
    public fun format(
        value: DataSize,
        unit: DataSizeUnit = unitFrom(value),
        decimals: Int = 0,
    ): String = value.toString(unit, decimals)

    /**
     * Java-use only.
     * For Kotlin, use unitFrom(value: DataSize) instead.
     *
     * @return [DataSizeUnit] from [value] depending on the threshold.
     * @throws IllegalStateException value is not within 0 <= value <= [MAX_SIZE]
     */
    @JvmStatic
    @Throws(IllegalStateException::class)
    public fun unitFrom(value: Long): DataSizeUnit = unitFrom(value.bytes)

    /**
     * Java-use only.
     * For Kotlin, use format(value: DataSize, unit: DataSizeUnit, decimals: Int) instead.
     *
     * @return String representation of [value] in a custom format.
     */
    @JvmStatic
    @Throws(IllegalStateException::class)
    public fun format(
        value: Long,
        unit: DataSizeUnit = unitFrom(value),
        decimals: Int = 0,
    ): String = format(value.bytes, unit, decimals)
}