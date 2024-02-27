package com.roughlyunderscore.cubinghub.data.type

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Email {
  @SerializedName("mail")
  @Expose
  var mail: String? = null

  fun isUndeterminedEmail() = mail == UNDETERMINED_EMAIL.mail

  companion object {
    val UNDETERMINED_EMAIL = of("?...")
    fun of(mail: String?) = Email().apply { this.mail = mail }
  }
}
