/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf


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

/** Marker interface for data size units that follow the SI (International System of Units) standard. */
public sealed interface SiCompatibleUnit

/** Marker interface for data size units that follow the IEC (International Electrotechnical Commission) standard. */
public sealed interface IecCompatibleUnit

/**
 * Base implementation of [DataSizeUnit] representing units defined in bytes.
 *
 * @property value the number of bytes represented by a single unit.
 * @property tag the identifier associated with this unit.
 */
public class ByteUnit internal constructor(
    private val value: Long,
    private val tag: DataSizeTag,
) : DataSizeUnit, SiCompatibleUnit, IecCompatibleUnit {

    public override fun value(): Long = value

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
public sealed class SiUnit(
    private val value: Long,
    private val tag: DataSizeTag,
) : DataSizeUnit, SiCompatibleUnit {

    public data object Kilobyte : SiUnit(value = 1_000L, tag = DataSizeTag("ds_kb"))
    public data object Megabyte : SiUnit(value = 1_000_000L, tag = DataSizeTag("ds_mb"))
    public data object Gigabyte : SiUnit(value = 1_000_000_000L, tag = DataSizeTag("ds_gb"))
    public data object Terabyte : SiUnit(value = 1_000_000_000_000L, tag = DataSizeTag("ds_tb"))
    public data object Petabyte : SiUnit(value = 1_000_000_000_000_000L, tag = DataSizeTag("ds_pb"))

    public override fun value(): Long = value

    public override fun tag(): DataSizeTag = tag

    public companion object {

        /** Returns all units belonging to the decimal (SI) system. */
        public fun entries(): ImmutableList<SiCompatibleUnit> =
            persistentListOf(ByteUnit.Byte, Kilobyte, Megabyte, Gigabyte, Terabyte, Petabyte)
    }
}

/**
 * Represents binary (IEC) data size units based on powers of 2.
 * Units range from [Kibibyte] to [Pebibyte].
 */
public sealed class IecUnit(
    private val value: Long,
    private val tag: DataSizeTag,
) : DataSizeUnit, IecCompatibleUnit {

    public data object Kibibyte : IecUnit(value = 1L shl 10, tag = DataSizeTag("ds_kib"))
    public data object Mebibyte : IecUnit(value = 1L shl 20, tag = DataSizeTag("ds_mib"))
    public data object Gibibyte : IecUnit(value = 1L shl 30, tag = DataSizeTag("ds_gib"))
    public data object Tebibyte : IecUnit(value = 1L shl 40, tag = DataSizeTag("ds_tib"))
    public data object Pebibyte : IecUnit(value = 1L shl 50, tag = DataSizeTag("ds_pib"))

    public override fun value(): Long = value

    public override fun tag(): DataSizeTag = tag

    public companion object {

        /** Returns all units belonging to the binary (IEC) system. */
        public fun entries(): ImmutableList<IecCompatibleUnit> =
            persistentListOf(ByteUnit.Byte, Kibibyte, Mebibyte, Gibibyte, Tebibyte, Pebibyte)
    }
}
