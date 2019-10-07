package com.lndmflngs.colorizer.data.model.api

import org.json.JSONObject

data class ImageRequest(val encodedImage: String, val defFormat: String) {

    val imageJson: String
        get() {
            val jsonObject = JSONObject()
            jsonObject.put(
                "image",
                "data:image/$defFormat;base64,$encodedImage"
            )
            return jsonObject.toString()
        }
}
