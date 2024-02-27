package com.roughlyunderscore.cubinghub.data.type

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.roughlyunderscore.cubinghub.util.minus

class Algorithm {
  @SerializedName("id")
  @Expose
  var id: Int? = null

  @SerializedName("subset")
  @Expose
  var subset: String? = null

  @SerializedName("exhibit")
  @Expose
  var exhibit: String? = null

  @SerializedName("exhibitImageUrl")
  @Expose
  var exhibitImageUrl: String? = null

  @SerializedName("imageFileName")
  @Expose
  var imageFileName: String? = null

  @SerializedName("variations")
  @Expose
  var variations: List<Variation>? = null

  val sortedVariations: List<Variation>
    get() = variations?.sortedBy { it.disliked?.size - it.liked?.size } ?: emptyList()

  override fun toString(): String = "Algorithm(id=$id, subset=$subset, exhibit=$exhibit, exhibitImageUrl=$exhibitImageUrl, imageFileName=$imageFileName, variations=${variations?.map{ it.toString() }})"
}