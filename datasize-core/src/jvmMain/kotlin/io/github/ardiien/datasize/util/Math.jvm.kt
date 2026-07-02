/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.ardiien.datasize.util


@Throws(ArithmeticException::class)
internal actual fun multiplyExact(x: Long, y: Long): Long = Math.multiplyExact(x, y)

@Throws(ArithmeticException::class)
internal actual fun addExact(x: Long, y: Long): Long = Math.addExact(x, y)

@Throws(ArithmeticException::class)
internal actual fun subtractExact(x: Long, y: Long): Long = Math.subtractExact(x, y)