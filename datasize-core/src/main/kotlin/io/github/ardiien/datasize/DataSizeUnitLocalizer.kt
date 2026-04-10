/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize


/**
 * Provides localization for data size units.
 *
 * Implementations supply human-readable names and abbreviations
 * for [DataSizeUnit] values, typically based on locale or formatting rules.
 */
@ExperimentalDataSizeApi
public interface DataSizeUnitLocalizer {

    /**
     * Returns the full human-readable name of the given [unit].
     *
     * @param unit the unit to localize.
     */
    public fun name(unit: DataSizeUnit): String

    /**
     * Returns the abbreviated symbol of the given [unit].
     *
     * @param unit the unit to localize.
     */
    public fun abbreviation(unit: DataSizeUnit): String
}