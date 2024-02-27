package com.roughlyunderscore.cubinghub.data.type

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Identification {
  @SerializedName("email")
  @Expose
  var email: String? = null

  @SerializedName("id")
  @Expose
  var id: Int? = null

  fun identify(email: String?) = this.apply { this.email = email }
  fun identify(id: Int?) = this.apply { this.id = id }

  fun isUnidentified() = email == null || id == null

  companion object {
    val UNIDENTIFIED = Identification()
  }
}