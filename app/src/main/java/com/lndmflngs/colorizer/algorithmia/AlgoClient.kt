package com.lndmflngs.colorizer.algorithmia

import android.util.Log
import com.algorithmia.Algorithmia
import com.algorithmia.algo.AlgoResponse
import com.algorithmia.algo.AlgoSuccess
import com.lndmflngs.colorizer.extensions.SingletonHolder
import org.json.JSONObject

class AlgoClient private constructor(apiKey: String) {

  private var client = Algorithmia.client(apiKey)

  fun uploadImage(byteArray: ByteArray): AlgoResponse {
    val imageDir = client.dir(HOSTED_DATA_PATH)
    val fileName = "${System.currentTimeMillis()}.jpg"
    //  Upload byteArray to Algorithmia's hosted data
    imageDir.file(fileName).put(byteArray)
    val bwImage = imageDir.file(fileName)
    val imageString = bwImage.toString()
    val algorithm = client.algo(CLIENT_IMAGE_COLORIZATION)
    return algorithm.pipe(imageString)
  }

  //  Downloading Result Data from a Data Collection
  fun fetchResultImagePath(response: AlgoResponse): String {
    val jsonResult = JSONObject((response as AlgoSuccess).asJsonString())
    val imgUri = jsonResult.getString("output")
    val imgFile = client.file(imgUri).file
    Log.i(TAG, "Result image uri: $imgUri")
    return imgFile.absolutePath
  }

  companion object : SingletonHolder<AlgoClient, String>(::AlgoClient) {
    private const val TAG = "AlgoClient"

    private const val CLIENT_IMAGE_COLORIZATION = "deeplearning/ColorfulImageColorization/1.1.13"
    private const val HOSTED_DATA_PATH = "data://.my/colorize"
  }
}
