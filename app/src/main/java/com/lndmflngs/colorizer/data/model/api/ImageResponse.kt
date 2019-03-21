package com.lndmflngs.colorizer.data.model.api

import org.json.JSONObject

data class ImageResponse(val output: String) {

  companion object {
    fun convertFromJson(jsonObject: JSONObject): ImageResponse =
      ImageResponse(jsonObject.getString("output"))
  }
}
