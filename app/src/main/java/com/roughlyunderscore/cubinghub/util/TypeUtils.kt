package com.roughlyunderscore.cubinghub.util

import org.jetbrains.annotations.Contract

object TypeUtils {
}

/**
 * Returns the enum constant of the specified enum type with the specified [name].
 * If not found, returns [fallback].
 */
@Contract("_, null -> null")
inline fun <reified T : Enum<T>> safeValueOf(name: String, fallback: T?): T? {
  return try {
    enumValueOf<T>(name)
  } catch (e: IllegalArgumentException) {
    fallback
  }
}