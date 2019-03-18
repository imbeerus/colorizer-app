package com.lndmflngs.colorizer.data.model.api

import com.google.gson.annotations.SerializedName

data class ImageResponse(
  @SerializedName("output")
  val output: String
)