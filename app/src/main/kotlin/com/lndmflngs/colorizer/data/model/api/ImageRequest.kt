package com.lndmflngs.colorizer.data.model.api

import org.json.JSONObject

data class ImageRequest(val encodedImage: String) {

    val imageJson: String
        get() {
            val jsonObject = JSONObject()
            jsonObject.put("image", "data:image/png;base64,$encodedImage")
            return jsonObject.toString()
        }
}
