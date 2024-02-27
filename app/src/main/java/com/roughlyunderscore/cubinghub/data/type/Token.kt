package com.roughlyunderscore.cubinghub.data.type

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Token {
  @SerializedName("token")
  @Expose
  var token: String? = null
}