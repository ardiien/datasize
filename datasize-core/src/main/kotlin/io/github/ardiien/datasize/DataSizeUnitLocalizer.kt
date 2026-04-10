/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize


/** Localization provider for data size units. */
@ExperimentalDataSizeApi
public interface DataSizeUnitLocalizer {

    /** Returns the full human-readable name of this unit. */
    public fun name(unit: DataSizeUnit): String

    /** Returns the abbreviated symbol of this unit. */
    public fun abbreviation(unit: DataSizeUnit): String
}