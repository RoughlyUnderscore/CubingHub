package com.roughlyunderscore.cubinghub.api

import android.content.Context
import com.google.gson.Gson
import com.roughlyunderscore.cubinghub.data.Cacher
import com.roughlyunderscore.cubinghub.data.type.Algorithm
import com.roughlyunderscore.cubinghub.data.type.Identification
import com.roughlyunderscore.cubinghub.data.type.Subset
import com.roughlyunderscore.cubinghub.data.type.Token
import com.roughlyunderscore.cubinghub.util.Either
import com.roughlyunderscore.cubinghub.util.NetworkUtils
import org.apache.commons.io.FileUtils

class APIRepositoryImpl(private val api: BackendAPI, private val ctx: Context, private val gson: Gson) : APIRepository {
  private val fileDir = ctx.filesDir

  override suspend fun getSubsets(): MutableList<Subset> {
    val subsetsFile = fileDir.resolve("ch_subsets.json").apply {
      if (!exists()) createNewFile()
    }

    val loadedSubsets = gson.fromJson(FileUtils.readFileToString(subsetsFile, Charsets.UTF_8), Array<Subset>::class.java)?.toMutableList() ?: mutableListOf()

    return if (NetworkUtils.isNetworkAvailable(ctx)) {
      val subsets = mutableListOf<Subset>()

      api.getSubsets().body()?.forEach { serverLoadedSubset ->
        val localSubset = loadedSubsets.find { it.id == serverLoadedSubset.id }
        Cacher.cacheSubset(serverLoadedSubset, localSubset, ctx)?.let {
          subsets.add(it)
        }
      }

      FileUtils.writeStringToFile(subsetsFile, gson.toJson(subsets), Charsets.UTF_8, false)
      subsets
    } else {
      try {
        loadedSubsets
      } catch (ex: Exception) {
        ex.printStackTrace()
        mutableListOf()
      }
    }
  }

  override suspend fun getAlgorithms(subset: String): MutableList<Algorithm> {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return mutableListOf()

    val call = api.getAlgorithms(subset)
    if (!call.isSuccessful) return mutableListOf()
    return call.body()?.toMutableList() ?: mutableListOf()
  }

  override suspend fun register(email: String, password: String): Int {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return NO_CONNECTION

    val call = api.register(email, password)
    return call.code()
  }

  override suspend fun auth(email: String, password: String): Either<Token, Int> {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return Either.failure(NO_CONNECTION)

    val call = api.auth(email, password)
    if (!call.isSuccessful) return Either.failure(call.code())
    // get json token value
    return Either.success(call.body() ?: return Either.failure(-1))
  }

  override suspend fun logout(token: String) {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return
    api.logout(token)
  }

  override suspend fun changePassword(token: String, oldPassword: String, newPassword: String, endSessions: Boolean): Int {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return NO_CONNECTION

    val call = api.changePassword(token, oldPassword, newPassword, endSessions)
    return call.code()
  }

  override suspend fun like(variationId: Int, token: String): Int {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return NO_CONNECTION

    val call = api.like(variationId, token)
    return call.code()
  }

  override suspend fun unlike(variationId: Int, token: String): Int {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return NO_CONNECTION

    val call = api.unlike(variationId, token)
    return call.code()
  }

  override suspend fun dislike(variationId: Int, token: String): Int {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return NO_CONNECTION

    val call = api.dislike(variationId, token)
    return call.code()
  }

  override suspend fun undislike(variationId: Int, token: String): Int {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return NO_CONNECTION

    val call = api.undislike(variationId, token)
    return call.code()
  }

  override suspend fun delete(token: String, password: String): Int {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return NO_CONNECTION

    val call = api.delete(token, password)
    return call.code()
  }

  override suspend fun verifyToken(token: String): Identification {
    if (!NetworkUtils.isNetworkAvailable(ctx)) return Identification.UNIDENTIFIED

    val call = api.verifyToken(token)
    if (!call.isSuccessful) return Identification.UNIDENTIFIED
    // get json email value
    return call.body() ?: Identification.UNIDENTIFIED
  }

  companion object Codes {
    const val NOT_CHECKED_CONFIRMATION = -1
    const val PASSWORDS_DONT_MATCH = -2
    const val EMPTY_FIELDS = -3
    const val NO_CONNECTION = -10
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val NOT_FOUND = 404
    const val CONFLICT = 409
    const val INTERNAL_SERVER_ERROR = 500
    const val SUCCESS = 200
  }
}