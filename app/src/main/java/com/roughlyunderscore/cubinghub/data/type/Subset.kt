package com.roughlyunderscore.cubinghub.data.type

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Subset {
  @SerializedName("id")
  @Expose
  var id: Int? = null

  @SerializedName("name")
  @Expose
  var name: String? = null

  @SerializedName("imageUrl")
  @Expose
  var imageUrl: String? = null

  @SerializedName("imagePath")
  @Expose
  var imageFileName: String? = null

  var algorithms = mutableListOf<Algorithm>()

  override fun toString(): String {
    return "Subset(id=$id, name=$name, imageUrl=$imageUrl, imageFileName=$imageFileName, algorithms=$algorithms)"
  }
}