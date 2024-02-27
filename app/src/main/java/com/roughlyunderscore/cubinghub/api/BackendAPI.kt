package com.roughlyunderscore.cubinghub.api

import com.roughlyunderscore.cubinghub.data.type.Algorithm
import com.roughlyunderscore.cubinghub.data.type.Identification
import com.roughlyunderscore.cubinghub.data.type.Subset
import com.roughlyunderscore.cubinghub.data.type.Token
import retrofit2.Response
import retrofit2.http.Query
import retrofit2.http.GET

interface BackendAPI {
  @GET("api/v1/subsets")
  suspend fun getSubsets(): Response<List<Subset>>

  @GET("api/v1/algorithms?")
  suspend fun getAlgorithms(
    @Query(value = "subset") subset: String
  ): Response<List<Algorithm>>

  @GET("api/v1/register?")
  suspend fun register(
    @Query(value = "email", encoded = true) email: String,
    @Query(value = "password", encoded = true) password: String,
    @Query(value = "agree_to_privacy_policy") agreeToPrivacyPolicy: Boolean = true,
    @Query(value = "agree_to_terms_of_service") agreeToTermsOfService: Boolean = true
  ): Response<Void>

  @GET("api/v1/auth?")
  suspend fun auth(
    @Query(value = "email") email: String,
    @Query(value = "password", encoded = true) password: String
  ): Response<Token>

  @GET("api/v1/logout?")
  suspend fun logout(
    @Query(value = "token") token: String
  ): Response<Void>

  @GET("api/v1/change_password?")
  suspend fun changePassword(
    @Query(value = "token") token: String,
    @Query(value = "oldpassword", encoded = true) oldPassword: String,
    @Query(value = "newpassword", encoded = true) newPassword: String,
    @Query(value = "endsessions") endSessions: Boolean,
  ): Response<Void>

  @GET("api/v1/like?")
  suspend fun like(
    @Query(value = "variationId") variationId: Int,
    @Query(value = "token") token: String
  ): Response<Void>

  @GET("api/v1/unlike?")
  suspend fun unlike(
    @Query(value = "variationId") variationId: Int,
    @Query(value = "token") token: String
  ): Response<Void>

  @GET("api/v1/dislike?")
  suspend fun dislike(
    @Query(value = "variationId") variationId: Int,
    @Query(value = "token") token: String
  ): Response<Void>

  @GET("api/v1/undislike?")
  suspend fun undislike(
    @Query(value = "variationId") variationId: Int,
    @Query(value = "token") token: String
  ): Response<Void>

  @GET("api/v1/delete?")
  suspend fun delete(
    @Query(value = "token") token: String,
    @Query(value = "password", encoded = true) password: String
  ): Response<Void>

  @GET("api/v1/verify_token?")
  suspend fun verifyToken(
    @Query(value = "token") token: String
  ): Response<Identification>
}