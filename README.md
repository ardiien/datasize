# Kotlin Data Size

[![License](https://img.shields.io/badge/license-Apache%20License%202.0-fedcba.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-2.3.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Maven Central](https://img.shields.io/github/v/tag/ardiien/datasize?label=datasize&color=64748B)](https://central.sonatype.com/artifact/io.github.ardiien.datasize/datasize)
![Build](https://img.shields.io/github/actions/workflow/status/ardiien/datasize/datasize.yml?branch=main&style=flat&color=limegreen&logo=github)

The `DataSize` value class represents a quantity of digital information and provides a set of utilities for working with it.
Use extension properties to create `DataSize` instances. All values are stored internally as bytes, and all calculations
are performed using this canonical representation.  For full details and additional capabilities, refer to the class documentation.

## Table of Contents

* [Background](#background)
* [Setup](#setup)
* [Basics](#basics)
    * [Operations](#operations)
    * [Comparing](#comparing)
* [Formatting](#formatting)
* [Precision](#precision)
* [More to Explore](#more-to-explore)

## Background

Working with data sizes can be confusing due to inconsistent terminology.

In the [International System of Units](https://en.wikipedia.org/wiki/Byte#Multiple-byte_units) (SI), prefixes such as
`kilo` strictly mean `1000`. However, in many areas of software, "kilobyte" is often used to represent `1024` bytes. 
This inconsistency leads to ambiguity and incorrect assumptions.

To address this, more precise terms such as [kibibyte](https://en.wikipedia.org/wiki/Binary_prefix) (KiB = 1024 bytes),
[mebibyte](https://en.wikipedia.org/wiki/Binary_prefix) (MiB), and others were introduced.
Despite being technically correct, these terms are not always consistently used.

This library provides a clear and explicit way to represent and work with data sizes, removing ambiguity and ensuring correct calculations.

## Setup

Latest version:  
[![Maven Central](https://img.shields.io/github/v/tag/ardiien/datasize?label=datasize&color=64748B)](https://central.sonatype.com/artifact/io.github.ardiien.datasize/datasize)

Kotlin DSL:

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.ardiien.datasize:datasize:<version>")
}
```       

Groovy DSL:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.ardiien.datasize:datasize:<version>'
}
```

## Basics

`DataSize` provides extension properties for numeric types such as `Int`, `Long`, and `Double`, as well as for `ByteArray`.

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    val kilobyteFromInt: DataSize = 1.kibibytes
    val fromInt: Long = kilobyteFromInt.inBytes
    println(fromInt)    // 1024

    val kilobyteFromDouble: DataSize = 1.0.kilobytes
    val fromDouble: Long = kilobyteFromDouble.inBytes
    println(fromDouble) // 1000

    val kilobyteFromLong: DataSize = 1L.kibibytes
    val fromLong: Long = kilobyteFromLong.inBytes
    println(fromLong)   // 1024
}
```

> More examples are available in [samples/src/main/kotlin](samples/src/main/kotlin).

### Operations

The `DataSize` class supports the following arithmetic operations

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    val addition: DataSize = 5.mebibytes + 15.mebibytes
    val additionPreview: String = addition.toString(IecUnit.Mebibyte, fractionDigits = 1)
    println(additionPreview)        // 20 MB

    val substraction: DataSize = 105.megabytes - 5.megabytes
    val substractionPreview: String = substraction.toString(SiUnit.Megabyte, fractionDigits = 1)
    println(substractionPreview)    // 100 MB

    val multiplication: DataSize = 5.megabytes * 2
    val multiplicationPreview: String = multiplication.toString(SiUnit.Megabyte, fractionDigits = 1)
    println(multiplicationPreview)  // 10 MB

    val division: DataSize = 15.mebibytes / 2
    val divisionPreview: String = division.toString(IecUnit.Mebibyte, fractionDigits = 1)
    println(divisionPreview)        // 7,5 MB

    val remainder: DataSize = 11.megabytes % 2.megabytes
    val remainderPreview: String = remainder.toString(SiUnit.Megabyte, fractionDigits = 1)
    println(remainderPreview)       // 1 MB
}
```

> ⚠️ Note<br/>
> The result of any arithmetic operation:
> - must not be less than `DataSize.Zero`.
> - is normalized to avoid negative values or overflow.

### Comparing

`DataSize` implements `Comparable`, enabling direct comparison and natural ordering.

Utility functions:

- `min` and `max` return the smaller or larger of two `DataSize` instances.
- `isZero` checks whether the value equals `DataSize.Zero`.

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    val sortedList = listOf<DataSize>(1.kibibytes, 1.mebibytes, 20.kibibytes).sorted()
    println(sortedList)   // [1024, 20480, 1048576]

    val gt: Boolean = 15.kibibytes > 1.kibibytes
    println(gt)           // true

    val lte: Boolean = 15.kibibytes <= 14.kibibytes
    println(lte)          // false

    val eq: Boolean = 15.kibibytes == 15.kibibytes
    println(eq)           // true

    val neq: Boolean = 15.kibibytes != 5.kibibytes
    println(neq)          // true

    val min: DataSize = min(2.mebibytes, 2.kibibytes)
    println(min)          // 2048

    val max: DataSize = max(2.mebibytes, 2.kibibytes)
    println(max)          // 2097152
}
```

For low-level access, use inBytes to retrieve the raw value.

It may seem counterintuitive, but storage capacity is always a positive value because it represents the amount of data
that can be stored, and it's impossible to store a negative amount of data.
A negative size would imply the ability to somehow "un-store" data, which cannot be done. Digital data is represented by bits (0s and 1s).
You can have a certain number of bits, but you can't have a negative number of bits.

## Formatting

The `DataSize` class supports formatting in both Decimal (`1000`|`SI`) and Binary (`1024`|`IEC`) systems.
To remove boilerplate and repeated code, you can use the utility class `DataSizeFormatter`. There are two types of operations:

1. `binaryFormat` – transforms a `DataSize` instance into a formatted `String` using Binary (`1024`|`IEC`) system.
2. `decimalFormat` – transforms a `DataSize` instance into a formatted `String` using Decimal (`1000`|`SI`) system.

### Precision

Fractional precision is controlled via `fractionDigits` property. Values are clamped to a maximum of **2 fractional digits**.

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    val format: DecimalFormat = SimpleDataSizeFormatter.createFormat()
    val localizer = SimpleDataSizeUnitLocalizer()
    val formatter: DataSizeFormatter = SimpleDataSizeFormatter(format, localizer)
    val value: DataSize = 55.563.kibibytes

    val defaultPrecision: String = formatter.binaryFormat(value, fractionDigits = 0)
    println(defaultPrecision)       // 56 KB, default

    val betterPrecision: String = formatter.binaryFormat(value, fractionDigits = 1)
    println(betterPrecision)        // 55,6 KB

    val maxAvailablePrecision: String = formatter.binaryFormat(value, fractionDigits = 2)
    println(maxAvailablePrecision)  // 55,56 KB, max allowed
}
```

> ⚠️ Note<br/>
> Larger values without fractional digits may be rounded.<br/>
> For example, `1.6.terabytes` with `fractionDigits = 0` results in `2 TB`.

## More to Explore

For more examples and usage patterns, refer to `DataSizeTest`, `SimpleDataSizeFormatterTest`, and real-world usage of digital
data size utilities across features.