/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


/**
 * Represents a unit of data size.
 *
 * Each unit defines a fixed number of bytes and belongs to a specific unit system
 * (decimal or binary). Implementations are immutable and provide a type-safe way
 * to work with data size units.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Binary_prefix">Binary prefix</a>
 * @see <a href="https://en.wikipedia.org/wiki/File_size">File size</a>
 */
public interface DataSizeUnit {

    /** Returns the exact number of bytes represented by a single unit. */
    public fun value(): Long

    /** Returns the tag identifying this unit. */
    public fun tag(): DataSizeTag
}

/**
 * Base implementation of [DataSizeUnit] representing units defined in bytes.
 *
 * @property value the number of bytes represented by a single unit.
 * @property tag the identifier associated with this unit.
 */
@ExperimentalDataSizeApi
public open class ByteUnit internal constructor(
    private val value: Long,
    private val tag: DataSizeTag,
) : DataSizeUnit {

    /** Returns the exact number of bytes represented by this unit. */
    public override fun value(): Long = value

    /** Returns the tag identifying this unit. */
    public override fun tag(): DataSizeTag = tag

    public companion object {

        /** The base unit representing exactly one byte. */
        public val Byte: ByteUnit = ByteUnit(value = 1L, tag = DataSizeTag("ds_b"))
    }
}

/**
 * Represents decimal (SI) data size units based on powers of 10.
 * Units range from [Kilobyte] to [Petabyte].
 */
@ExperimentalDataSizeApi
public sealed class DecimalUnit(value: Long, tag: DataSizeTag) : ByteUnit(value, tag) {

    public data object Kilobyte : DecimalUnit(value = 1_000L, tag = DataSizeTag("ds_kb"))
    public data object Megabyte : DecimalUnit(value = 1_000_000L, tag = DataSizeTag("ds_mb"))
    public data object Gigabyte : DecimalUnit(value = 1_000_000_000L, tag = DataSizeTag("ds_gb"))
    public data object Terabyte : DecimalUnit(value = 1_000_000_000_000L, tag = DataSizeTag("ds_tb"))
    public data object Petabyte : DecimalUnit(value = 1_000_000_000_000_000L, tag = DataSizeTag("ds_pb"))

    public companion object {

        /** Returns all units belonging to the decimal (SI) system. */
        public fun entries(): ImmutableList<DataSizeUnit> =
            persistentListOf(Byte, Kilobyte, Megabyte, Gigabyte, Terabyte, Petabyte)
    }
}

/**
 * Represents binary (IEC) data size units based on powers of 2.
 * Units range from [Kibibyte] to [Pebibyte].
 */
@ExperimentalDataSizeApi
public sealed class BinaryUnit(value: Long, tag: DataSizeTag) : ByteUnit(value, tag) {

    public data object Kibibyte : BinaryUnit(value = 1L shl 10, tag = DataSizeTag("ds_kib"))
    public data object Mebibyte : BinaryUnit(value = 1L shl 20, tag = DataSizeTag("ds_mib"))
    public data object Gibibyte : BinaryUnit(value = 1L shl 30, tag = DataSizeTag("ds_gib"))
    public data object Tebibyte : BinaryUnit(value = 1L shl 40, tag = DataSizeTag("ds_tib"))
    public data object Pebibyte : BinaryUnit(value = 1L shl 50, tag = DataSizeTag("ds_pib"))

    public companion object {

        /** Returns all units belonging to the binary (IEC) system. */
        public fun entries(): ImmutableList<DataSizeUnit> =
            persistentListOf(Byte, Kibibyte, Mebibyte, Gibibyte, Tebibyte, Pebibyte)
    }
}

/** Returns `true` if this unit belongs to the decimal (SI) system. */
@ExperimentalDataSizeApi
@OptIn(ExperimentalContracts::class)
public fun DataSizeUnit.isDecimalUnit(): Boolean {
    contract { returns(true) implies (this@isDecimalUnit is DecimalUnit) }
    return this is DecimalUnit
}

/** Returns `true` if this unit belongs to the binary (IEC) system. */
@ExperimentalDataSizeApi
@OptIn(ExperimentalContracts::class)
public fun DataSizeUnit.isBinaryUnit(): Boolean {
    contract { returns(true) implies (this@isBinaryUnit is BinaryUnit) }
    return this is BinaryUnit
}