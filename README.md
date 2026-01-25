# Kotlin Data Size

[![License](https://img.shields.io/badge/license-Apache%20License%202.0-fedcba.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-2.3.0-blue.svg?logo=kotlin)](http://kotlinlang.org)

The `DataSize` value class represents a quantity of digital information and provides a rich set of utilities.
Use extension function to create the `DataSize` representation. It's important to know that all values are stored in
`bytes`.
To explore all available features and implementation details, check out the full class documentation.

All calculations are based
on [IBM Storage Insights](https://www.ibm.com/docs/en/storage-insights?topic=overview-units-measurement-storage-data).

## Table of Contents

* [Basics](#basics)
    * [Operations](#operations)
    * [Comparing](#comparing)
* [Formatting](#formatting)
* [Precision](#precision)
* [More to Explore](#more-to-explore)

## Basics

The `DataSize` has extensions available on numeric types like `Int`, `Long`, and `Double`.
Here is a small example.

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    // Supported units: bytes, kilobytes, megabytes, gigabytes, and terabytes.

    val kilobyteFromInt = 1.kilobytes       // 1024
    val kilobyteFromDouble = 1.0.kilobytes  // 1024
    val kilobyteFromLong = 1L.kilobytes     // 1024
}
```

> You can explore more samples [here](samples/src/main/kotlin).

### Operations

The `DataSize` class supports the following arithmetic operations

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    // Explore all overrides of the "operator fun" in DataSize class.

    val addition = 5.megabytes + 15.megabytes       // 20 MB
    val substraction = 105.megabytes - 5.megabytes  // 100 MB

    val multiplication = 5.megabytes * 2            // 10 MB
    val division = 15.megabytes / 2                 // 7,5 MB
    val remainder = 11.megabytes % 2.megabytes      // 1 MB
}
```

> ⚠️ Note<br/>
> The result of any arithmetic operation
> - Must not exceed `DataSize.Infinite`
> - Must not be less than `DataSize.Zero`

### Comparing

The `DataSize` class implements the `Comparable` interface, enabling direct comparison between instances using standard
operators, and allows to have natural ordering.

Also, it has some other useful methods like:

- `min` and `max` as standard comparison utilities to determine the smaller or larger of two `DataSize` instances.
- `isInfinite` and `isZero` to check boundaries of the `DataSize` instances.

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    val sortedList = listOf(
        1.kilobytes, 1.megabytes, 20.kilobytes  // [1024, 20480, 1048576]
    ).sorted()

    val gt = 15.kilobytes > 1.kilobytes         // true
    val lte = 15.kilobytes <= 14.kilobytes      // false
    val eq = 15.kilobytes == 15.kilobytes       // true
    val neq = 15.kilobytes != 5.kilobytes       // true

    val min = min(2.megabytes, 2.kilobytes)     // 2048
    val max = max(2.megabytes, 2.kilobytes)     // 2097152
}
```

For negative values or low-level operations, use the `inBytes` method to retrieve the raw byte representation on a
`DataSize` instance.
It may seem counterintuitive, but storage capacity is always a positive value because it represents the amount of data
that can be stored, and it's impossible to store a negative amount of data.
A negative size would imply the ability to somehow "un-store" data, which cannot be done. Digital data is represented by
bits (0s and 1s). You can have a certain number of bits, but you can't have a negative number of bits.

## Formatting

The `DataSize` class supports formatting in both Decimal (`1000`) as _experimental_ and Binary (`1024`) base
representations. By default, `DataSizeUnit` uses the **Binary** base.
Currently, there is no way to select the base for the `DataSize`. To convert `DataSize` Binary to Decimal, use
`DataSize.toDecimalString` helper function exclusively for display purposes.

To remove boilerplate and repeated code, you can use the utility class `DataSizeFormatter`. There are two types of
operations:

1. `format` – transforms a `DataSize` instance or raw bytes into a formatted `String`
2. `unitFrom` – identifies the appropriate `DataSizeUnit` to get the highest scale for `format`

### Precision

It's important to note that there's a difference between `decimal == 0` and `decimal > 0`. The `decimal` range is
clamped between 0 and 2. Any value greater than 2 will be coerced to 2 during evaluation.

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    val value = 55.563.kilobytes

    val defaultPrecision = DataSizeFormatter.format(value, decimals = 0)      // 56 KB, default
    val betterPrecision = DataSizeFormatter.format(value, decimals = 1)       // 55,6 KB
    val maxAvailablePrecision = DataSizeFormatter.format(value, decimals = 2) // 55,56 KB, max allowed
}
```

> ⚠️ Note<br/>
> The larger the number without a decimal point, the greater the rounding up. E.g. `format(1.6.terabytes, decimals = 0)`
> results in 2 TB output.

## More to Explore

For more examples and usage patterns, refer to `DataSizeTest`, `DataSizeFormatterTest`, and real-world usage of digital
data size utilities across features.
These resources provide practical insights into how data size is handled, formatted, and tested.