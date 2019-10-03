package com.lndmflngs.colorizer.data.remote

import com.algorithmia.AlgorithmiaClient
import com.algorithmia.algo.Algorithm
import com.lndmflngs.colorizer.data.model.api.ImageResponse
import com.lndmflngs.colorizer.di.ApiKey
import com.lndmflngs.colorizer.di.ImageDefFormat
import io.reactivex.Single
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

interface ApiClientHelper {

  fun colorizeImageRequest(input: ByteArray): Single<ImageResponse>

  fun fetchResultImagePath(output: String): Single<String>
}

@Singleton
class ApiClient @Inject
constructor(
  private val algoClient: AlgorithmiaClient,
  private val colorizerAlgorithm: Algorithm,
  @ApiKey private val apiKey: String,
  @ImageDefFormat private val defFormat: String
) : ApiClientHelper {

  override fun colorizeImageRequest(input: ByteArray): Single<ImageResponse> {
    return Single.create {
      try {
        val inputPath = fetchInputImagePath(input)
        val algoResponse = colorizerAlgorithm.pipe(inputPath)
        val imageResponse = ImageResponse.convertFromJson(JSONObject(algoResponse.asJsonString()))
        it.onSuccess(imageResponse)
      } catch (e: Exception) {
        it.onError(e)
      }
    }
  }

  private fun fetchInputImagePath(input: ByteArray): String {
    val imageDir = algoClient.dir(ApiConstants.HOSTED_DATA_PATH)
    val fileName = "${System.currentTimeMillis()}.$defFormat"
    imageDir.file(fileName).put(input)
    return imageDir.file(fileName).toString()
  }

  override fun fetchResultImagePath(output: String): Single<String> {
    return Single.create {
      try {
        val imgFile = algoClient.file(output).file
        val path = imgFile.absolutePath
        it.onSuccess(path)
      } catch (e: Exception) {
        it.onError(e)
      }
    }
  }

}