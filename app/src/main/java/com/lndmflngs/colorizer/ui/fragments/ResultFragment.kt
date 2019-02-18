package com.lndmflngs.colorizer.ui.fragments

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.algorithmia.Algorithmia
import com.algorithmia.AlgorithmiaClient
import com.algorithmia.algo.AlgoSuccess
import com.lndmflngs.colorizer.R
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_result.fab
import kotlinx.android.synthetic.main.fragment_result.progressBar
import kotlinx.android.synthetic.main.fragment_result.resultView
import kotlinx.android.synthetic.main.include_result.resultImageView
import org.json.JSONObject
import java.io.File

class ResultFragment : Fragment() {

  private lateinit var client: AlgorithmiaClient
  private lateinit var selectedImage: ByteArray
  private lateinit var shareMenuItem: MenuItem

  private var resultImgFile: File? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // fetch arguments
    selectedImage = arguments?.getByteArray(ARGUMENT_IMG_BYTE_ARRAY)!!
    setHasOptionsMenu(true)
    // setup Algorithmia client
    client = Algorithmia.client("simPov43OY7x0I6egDS8AMM8Xlh1")
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_result, container, false)
  }

  override fun onCreateOptionsMenu(
    menu: Menu,
    inflater: MenuInflater
  ) {
    inflater.inflate(R.menu.menu_result, menu)
    shareMenuItem = menu.findItem(R.id.share)
    super.onCreateOptionsMenu(menu, inflater)
  }

  @SuppressLint("StaticFieldLeak")
  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    // set fab listener to share result
    fab.setOnClickListener { shareResultImgFile() }
    getColoredImagePath()
      .firstOrError()
      .subscribeOn(Schedulers.single())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(getColorzieObserver())
  }

  private fun getColoredImagePath(): Observable<String> {
    return Observable.create {
      try {
        val response = makeAlgorithmiaCall(selectedImage)
        it.onNext(response!!)
        it.onComplete()
      } catch (e: Exception) {
        it.onError(e)
      }
    }
  }

  private fun makeAlgorithmiaCall(byteArray: ByteArray): String? {
    try {
      val imageDir = client.dir(HOSTED_DATA_PATH)
      val fileName = "${System.currentTimeMillis()}.jpg"
      //  Upload byteArray to Algorithmia's hosted data
      imageDir.file(fileName).put(byteArray)
      val bwImage = imageDir.file(fileName)
      val imageString = bwImage.toString()
      val algorithm = client.algo(CLIENT_COLORFUL_IMAGE_COLORIZATION)
      val result = algorithm.pipe(imageString)
      //  Downloading Result Data from a Data Collection
      if (result.isSuccess) {
        val jsonResult = JSONObject((result as AlgoSuccess).asJsonString())
        val imgUri = jsonResult.getString("output")
        Log.i(TAG, "Result image uri: $imgUri")
        val imgFile = client.file(imgUri)
        if (imgFile.exists()) {
          return imgFile.file.absolutePath
        } else {
          Log.e(TAG, "File don't exist")
        }
      } else {
        Log.e(TAG, "Error during get result")
      }
    } catch (e: Exception) {
      Log.e(TAG, e.toString())
    }
    return null
  }

  private fun getColorzieObserver(): SingleObserver<String> {
    return object : SingleObserver<String> {
      override fun onSuccess(t: String) {
        showColoredResult(t)
//          saveResultImage()
      }

      override fun onSubscribe(d: Disposable) {
        Log.d(TAG, " onSubscribe : " + d.isDisposed)
      }

      override fun onError(e: Throwable) {
        Log.d(TAG, " onError : " + e.message)
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.share -> {
        shareResultImgFile()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun showColoredResult(resultTempFilePath: String) {
    progressBar.visibility = View.GONE
    resultView.visibility = View.VISIBLE
    fab.visibility = View.VISIBLE
    shareMenuItem.isEnabled = true
    resultImageView.setImageBitmap(BitmapFactory.decodeFile(resultTempFilePath))
  }

  private fun saveResultImage() {
//    resultImgFile = getOutputMediaFile()
//    try {
//      val fos = FileOutputStream(resultImgFile)
//      val byteArray = bitmapToByteArray(BitmapFactory.decodeFile(resultTempFilePath))
//      fos.write(byteArray)
//      fos.close()
//      activity?.galleryAddPic(resultImgFile!!)
//    } catch (e: FileNotFoundException) {
//      Log.d(TAG, "File not found: ${e.message}")
//    } catch (e: IOException) {
//      Log.d(TAG, "Error accessing file: ${e.message}")
//    }
  }

  private fun shareResultImgFile() {
//    val authority = getString(R.string.file_provider_authority)
//    val uri = resultImgFile?.let { FileProvider.getUriForFile(activity!!, authority, it) }
//    val shareIntent: Intent = Intent().apply {
//      action = Intent.ACTION_SEND
//      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//      putExtra(Intent.EXTRA_STREAM, uri)
//      type = "image/png"
//    }
//    startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.title_share_img)))
  }

  companion object {
    private const val TAG = "ResultFragment"
    private const val ARGUMENT_IMG_BYTE_ARRAY = "ResultFragment:img"

    private const val CLIENT_COLORFUL_IMAGE_COLORIZATION =
      "deeplearning/ColorfulImageColorization/1.1.13"
    private const val HOSTED_DATA_PATH = "data://.my/colorize"

    fun newInstance(imageByteArray: ByteArray): ResultFragment {
      val fragment = ResultFragment()
      val args = Bundle()
      args.putByteArray(ARGUMENT_IMG_BYTE_ARRAY, imageByteArray)
      fragment.arguments = args
      return fragment
    }
  }
}