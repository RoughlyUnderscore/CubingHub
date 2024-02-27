package com.roughlyunderscore.cubinghub.api

import com.roughlyunderscore.cubinghub.data.type.Algorithm
import com.roughlyunderscore.cubinghub.data.type.Identification
import com.roughlyunderscore.cubinghub.data.type.Subset
import com.roughlyunderscore.cubinghub.data.type.Token
import com.roughlyunderscore.cubinghub.util.Either

interface APIRepository {
  /**
   * Loads the subsets from the API, or, if the cached subsets
   * are not outdated, loads them from the disk.
   */
  suspend fun getSubsets(): MutableList<Subset>
  suspend fun getAlgorithms(subset: String): MutableList<Algorithm>
  suspend fun register(email: String, password: String): Int
  suspend fun auth(email: String, password: String): Either<Token, Int>
  suspend fun logout(token: String)
  suspend fun changePassword(token: String, oldPassword: String, newPassword: String, endSessions: Boolean): Int
  suspend fun like(variationId: Int, token: String): Int
  suspend fun unlike(variationId: Int, token: String): Int
  suspend fun dislike(variationId: Int, token: String): Int
  suspend fun undislike(variationId: Int, token: String): Int
  suspend fun delete(token: String, password: String): Int

  /**
   * Runs a "verify_token" query on the API. Returns null if the token
   * could not be verified, or the identification data of the user if it could.
   */
  suspend fun verifyToken(token: String): Identification?
}