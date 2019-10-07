package com.lndmflngs.colorizer.data.remote

import android.util.Base64
import com.algorithmia.AlgorithmiaClient
import com.algorithmia.algo.Algorithm
import com.lndmflngs.colorizer.data.model.api.ImageRequest
import com.lndmflngs.colorizer.data.model.api.ImageResponse
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
    @ImageDefFormat private val defFormat: String
    ) : ApiClientHelper {

    override fun colorizeImageRequest(input: ByteArray): Single<ImageResponse> {
        return Single.create {
            try {
                val request = ImageRequest(Base64.encodeToString(input, Base64.DEFAULT), defFormat)
                val algoResponse = colorizerAlgorithm.pipeJson(request.imageJson)
                val imageResponse =
                    ImageResponse.convertFromJson(JSONObject(algoResponse.asJsonString()))
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

}