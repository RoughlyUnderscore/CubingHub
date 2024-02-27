package com.roughlyunderscore.cubinghub.util

class Either<S, F> private constructor(
  private val success: S?,
  private val failure: F?,
) {
  companion object {
    fun <S, F> success(value: S): Either<S, F> = Either(success = value, failure = null)
    fun <S, F> failure(value: F): Either<S, F> = Either(success = null, failure = value)
  }

  fun isSuccess(): Boolean = success != null
  fun isFailure(): Boolean = failure != null

  fun getSuccess(): S? = success
  fun getFailure(): F? = failure

  fun unwrapOrElse(f: (F) -> S): S = if (isSuccess()) getSuccess()!! else f(getFailure()!!)
  suspend fun unwrapOrElseSuspend(f: suspend (F) -> S): S = if (isSuccess()) getSuccess()!! else f(getFailure()!!)

  override fun toString(): String = "Either($success, $failure)"
}