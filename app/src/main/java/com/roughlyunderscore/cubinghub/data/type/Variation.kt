package com.roughlyunderscore.cubinghub.data.type

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Variation {
  @SerializedName("id")
  @Expose
  var id: Int? = null

  @SerializedName("value")
  @Expose
  var value: String? = null

  @SerializedName("liked")
  @Expose
  var liked: List<String>? = null

  @SerializedName("disliked")
  var disliked: List<String>? = null

  override fun toString(): String = "Variation(id=$id, value=$value, liked=$liked, disliked=$disliked)"
}