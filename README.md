# Kotlin Data Size

[![License](https://img.shields.io/badge/license-Apache%20License%202.0-fedcba.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-2.3.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Maven Central](https://img.shields.io/github/v/tag/ardiien/datasize?label=datasize&color=64748B)](https://central.sonatype.com/artifact/io.github.ardiien.datasize/datasize)
![Build](https://img.shields.io/github/actions/workflow/status/ardiien/datasize/datasize.yml?branch=main&style=flat&color=limegreen&logo=github)

The `DataSize` value class represents a quantity of digital information and provides a rich set of utilities.
Use extension function to create the `DataSize` representation. It's important to know that all values are stored as `bytes`. All calculations are performed on `bytes`.
To explore all available features and implementation details, check out the full class documentation.

## Table of Contents

* [Setup](#setup)
* [Basics](#basics)
    * [Operations](#operations)
    * [Comparing](#comparing)
* [Formatting](#formatting)
* [Precision](#precision)
* [More to Explore](#more-to-explore)

## Setup
The latest version of the library <br/>
[![Maven Central](https://img.shields.io/github/v/tag/ardiien/datasize?label=datasize&color=64748B)](https://central.sonatype.com/artifact/io.github.ardiien.datasize/datasize) <br/>

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

```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.ardiien.datasize:datasize:<version>'
}
```

## Basics

The `DataSize` has extensions available on numeric types like `Int`, `Long`, and `Double`. Also, supports `ByteArray`.
Here is a small example.

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    val kilobyteFromInt: DataSize = 1.binary.kibibytes
    val fromInt: Long = kilobyteFromInt.inBytes
    println(fromInt)    // 1024

    val kilobyteFromDouble: DataSize = 1.0.decimal.kilobytes
    val fromDouble: Long = kilobyteFromDouble.inBytes
    println(fromDouble) // 1000

    val kilobyteFromLong: DataSize = 1L.binary.kibibytes
    val fromLong: Long = kilobyteFromLong.inBytes
    println(fromLong)   // 1024
}
```

> You can explore more samples [here](samples/src/main/kotlin).

### Operations

The `DataSize` class supports the following arithmetic operations

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    val addition: DataSize = 5.binary.mebibytes + 15.binary.mebibytes
    val additionPreview: String = addition.toString(BinaryUnit.Mebibyte, fractionDigits = 1)
    println(additionPreview)        // 20 MB
  
    val substraction: DataSize = 105.decimal.megabytes - 5.decimal.megabytes
    val substractionPreview: String = substraction.toString(DecimalUnit.Megabyte, fractionDigits = 1)
    println(substractionPreview)    // 100 MB
  
    val multiplication: DataSize = 5.decimal.megabytes * 2
    val multiplicationPreview: String = multiplication.toString(DecimalUnit.Megabyte, fractionDigits = 1)
    println(multiplicationPreview)  // 10 MB
  
    val division: DataSize = 15.binary.mebibytes / 2
    val divisionPreview: String = division.toString(BinaryUnit.Mebibyte, fractionDigits = 1)
    println(divisionPreview)        // 7,5 MB
  
    val remainder: DataSize = 11.decimal.megabytes % 2.decimal.megabytes
    val remainderPreview: String = remainder.toString(DecimalUnit.Megabyte, fractionDigits = 1)
    println(remainderPreview)       // 1 MB
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
    val sortedList = listOf<DataSize>(1.binary.kibibytes, 1.binary.mebibytes, 20.binary.kibibytes).sorted()
    println(sortedList)   // [1024, 20480, 1048576]
  
    val gt: Boolean = 15.binary.kibibytes > 1.binary.kibibytes
    println(gt)           // true
  
    val lte: Boolean = 15.binary.kibibytes <= 14.binary.kibibytes
    println(lte)          // false
  
    val eq: Boolean = 15.binary.kibibytes == 15.binary.kibibytes
    println(eq)           // true
  
    val neq: Boolean = 15.binary.kibibytes != 5.binary.kibibytes
    println(neq)          // true
  
  
    val min: DataSize = min(2.binary.mebibytes, 2.binary.kibibytes)
    println(min)          // 2048
  
    val max: DataSize = max(2.binary.mebibytes, 2.binary.kibibytes)
    println(max)          // 2097152
}
```

For negative values or low-level operations, use the `inBytes` method to retrieve the raw byte representation on a
`DataSize` instance.
It may seem counterintuitive, but storage capacity is always a positive value because it represents the amount of data
that can be stored, and it's impossible to store a negative amount of data.
A negative size would imply the ability to somehow "un-store" data, which cannot be done. Digital data is represented by
bits (0s and 1s). You can have a certain number of bits, but you can't have a negative number of bits.

## Formatting

The `DataSize` class supports formatting in both Decimal (`1000`|`SI`) and Binary (`1024`|`IEC`) base
representations. To remove boilerplate and repeated code, you can use the utility class `DataSizeFormatter`. There are two types of
operations:

1. `format` – transforms a `DataSize` instance or raw bytes into a formatted `String`
2. `unitFrom` – identifies the appropriate `DataSizeUnit` to get the highest scale for `format`

### Precision

It's important to note that there's a difference between `decimal == 0` and `decimal > 0`. The `decimal` range is
clamped between 0 and 2. Any value greater than 2 will be coerced to 2 during evaluation.

```kotlin
import io.github.ardiien.datasize.*

fun main() {
    val format: DecimalFormat = SimpleDataSizeFormatter.createFormat()
    val formatter: DataSizeFormatter = SimpleDataSizeFormatter(format)
    val value: DataSize = 55.563.binary.kibibytes
  
    val defaultPrecision: String = formatter.format(value, fractionDigits = 0)
    println(defaultPrecision)       // 56 KB, default
  
    val betterPrecision: String = formatter.format(value, fractionDigits = 1)
    println(betterPrecision)        // 55,6 KB
  
    val maxAvailablePrecision: String = formatter.format(value, fractionDigits = 2)
    println(maxAvailablePrecision)  // 55,56 KB, max allowed
}
```

> ⚠️ Note<br/>
> The larger the number without a decimal point, the greater the rounding up. For instance, `format(1.6.terabytes, decimals = 0)`
> results in 2 TB output.

## More to Explore

For more examples and usage patterns, refer to `DataSizeTest`, `DataSizeFormatterTest`, and real-world usage of digital
data size utilities across features.
These resources provide practical insights into how data size is handled, formatted, and tested.