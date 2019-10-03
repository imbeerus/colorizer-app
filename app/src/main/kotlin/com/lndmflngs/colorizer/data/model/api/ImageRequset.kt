package com.lndmflngs.colorizer.data.model.api

data class ImageRequset(val encodedImage: String) {

    val imageJson =
        "{\n" +
                "\"image\": \"data:image/png;base64$encodedImage\",\n" +
                "}"

}
