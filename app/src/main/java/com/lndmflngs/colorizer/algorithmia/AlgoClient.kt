package com.lndmflngs.colorizer.algorithmia

import android.util.Log
import com.algorithmia.Algorithmia
import com.algorithmia.algo.AlgoResponse
import com.algorithmia.algo.AlgoSuccess
import com.lndmflngs.colorizer.extensions.SingletonHolder
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class AlgoClient private constructor(apiKey: String) {

  private var client = Algorithmia.client(apiKey)

  fun loadData(byteArray: ByteArray, onSuccess: (String) -> Unit) {
    getColoredImagePath(byteArray)
      .subscribeOn(Schedulers.single())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(object : SingleObserver<String> {
        override fun onSuccess(t: String) {
          onSuccess(t)
        }

        override fun onSubscribe(d: Disposable) {
          Log.d(TAG, " onSubscribe : " + d.isDisposed)
        }

        override fun onError(e: Throwable) {
          Log.d(TAG, " onError : " + e.message)
        }
      })
  }

  private fun getColoredImagePath(byteArray: ByteArray): Single<String> {
    return Single.create {
      try {
        val response = makeAlgorithmiaCall(byteArray)
        it.onSuccess(response!!)
      } catch (e: Exception) {
        it.onError(e)
      }
    }
  }

  private fun makeAlgorithmiaCall(byteArray: ByteArray): String? {
    try {
      val response = uploadImage(byteArray)
      if (response.isSuccess) {
        return fetchResultImagePath(response)
      } else {
        Log.e(TAG, "Error during get result")
      }
    } catch (e: Exception) {
      Log.e(TAG, e.toString())
    }
    return null
  }

  private fun uploadImage(byteArray: ByteArray): AlgoResponse {
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
  private fun fetchResultImagePath(response: AlgoResponse): String {
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
