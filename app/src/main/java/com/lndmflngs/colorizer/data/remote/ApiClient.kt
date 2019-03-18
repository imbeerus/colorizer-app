package com.lndmflngs.colorizer.data.remote

import com.algorithmia.Algorithmia
import com.google.gson.Gson
import com.lndmflngs.colorizer.data.model.api.ImageResponse
import com.lndmflngs.colorizer.di.ApiKey
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

interface ApiClientHelper {

  // TODO: add images (array) request
  fun doColorizeImageRequest(input: String): Single<ImageResponse>

  fun doColorizeImage(input: ByteArray): Single<ImageResponse>

  fun fetchResultImagePath(output: String): Single<String>
}

@Singleton
class ApiClient @Inject
constructor(
  private val gson: Gson,
  @ApiKey private val apiKey: String
) : ApiClientHelper {

  private val algoClient by lazy { Algorithmia.client(apiKey) }

  private val colorizerAlgorithm by lazy { algoClient.algo(ApiConstants.URL_IMAGE_COLORIZATION) }

  override fun doColorizeImageRequest(input: String): Single<ImageResponse> {
    return Single.create {
      try {
        val response = colorizerAlgorithm.pipe(input)
        val imageResponse = gson.fromJson(response.asJsonString(), ImageResponse::class.java)
        it.onSuccess(imageResponse)
      } catch (e: Exception) {
        it.onError(e)
      }
    }
  }

  override fun doColorizeImage(input: ByteArray): Single<ImageResponse> {
    return Single.create {
      try {
        val inputPath = fetchInputImagePath(input)
        val response = colorizerAlgorithm.pipe(inputPath)
        val imageResponse = gson.fromJson(response.asJsonString(), ImageResponse::class.java)
        it.onSuccess(imageResponse)
      } catch (e: Exception) {
        it.onError(e)
      }
    }
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

  private fun fetchInputImagePath(byteArray: ByteArray): String {
    val imageDir = algoClient.dir(ApiConstants.HOSTED_DATA_PATH)
    val fileName = "${System.currentTimeMillis()}.${ApiConstants.DEF_IMG_FORMAT}"
    imageDir.file(fileName).put(byteArray) //  Upload byteArray to Algorithmia's hosted data
    return imageDir.file(fileName).toString() // Fetch path of uploaded bw image
  }

}