/*
 * Copyright 2026 ardiien
 * Licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0
 */

package io.github.ardiien.datasize.util


/**
 * Returns the product of the arguments, throwing an exception if the result
 * overflows a [Long].
 *
 * @param x the first value
 * @param y the second value
 * @return the result
 * @throws ArithmeticException if the result overflows a long
 */
@Throws(ArithmeticException::class)
internal expect fun multiplyExact(x: Long, y: Long): Long

/**
 * Returns the sum of its arguments,
 * throwing an exception if the result overflows a [Long].
 *
 * @param x the first value
 * @param y the second value
 * @return the result
 * @throws ArithmeticException if the result overflows a long
 */
@Throws(ArithmeticException::class)
internal expect fun addExact(x: Long, y: Long): Long

/**
 * Returns the difference of the arguments,
 * throwing an exception if the result overflows a [Long].
 *
 * @param x the first value
 * @param y the second value to subtract from the first
 * @return the result
 * @throws ArithmeticException if the result overflows a long
 */
@Throws(ArithmeticException::class)
internal expect fun subtractExact(x: Long, y: Long): Long
