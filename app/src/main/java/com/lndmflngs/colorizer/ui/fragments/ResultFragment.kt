package com.lndmflngs.colorizer.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
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
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.algorithmia.AlgoClient
import com.lndmflngs.colorizer.extensions.pickPhoto
import com.lndmflngs.colorizer.ui.BaseActivity
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_result.fab
import kotlinx.android.synthetic.main.fragment_result.progressBar
import kotlinx.android.synthetic.main.fragment_result.resultView
import kotlinx.android.synthetic.main.include_result.resultImageView
import java.io.File

class ResultFragment : Fragment() {

  private lateinit var algoClient: AlgoClient
  private lateinit var selectedImage: ByteArray
  private lateinit var resultImgPath: String
  private lateinit var menu: Menu

  private var resultImgFile: File? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    algoClient = AlgoClient.getInstance(getString(R.string.algorithmia_api_key))
    // fetch arguments
    selectedImage = arguments?.getByteArray(ARGUMENT_PICKED_IMG)!!
    setHasOptionsMenu(true)
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
    this.menu = menu
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
    if (savedInstanceState != null) {
      resultImgPath = savedInstanceState.getString(BUNDLE_RESULT_IMG_PATH)!!
      showColoredResult()
    } else {
      loadData()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(BUNDLE_RESULT_IMG_PATH, resultImgPath)
    super.onSaveInstanceState(outState)
  }

  private fun loadData() {
    getColoredImagePath()
      .firstOrError()
      .subscribeOn(Schedulers.single())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(object : SingleObserver<String> {
        override fun onSuccess(t: String) {
          resultImgPath = t
          showColoredResult()
//          saveResultImage()
        }

        override fun onSubscribe(d: Disposable) {
          Log.d(TAG, " onSubscribe : " + d.isDisposed)
        }

        override fun onError(e: Throwable) {
          Log.d(TAG, " onError : " + e.message)
        }
      })
  }

  fun loadNewData(byteArray: ByteArray) {
    showResultUI(false) // show load
    selectedImage = byteArray
    loadData()
  }

  private fun showColoredResult() {
    showResultUI(true)
    resultImageView.setImageBitmap(BitmapFactory.decodeFile(resultImgPath))
  }

  private fun showResultUI(isShow: Boolean) {
    if (isShow) {
      progressBar.visibility = View.GONE
      resultView.visibility = View.VISIBLE
      fab.visibility = View.VISIBLE
      menu.findItem(R.id.change).isEnabled = true
      menu.findItem(R.id.share).isEnabled = true
    } else {
      progressBar.visibility = View.VISIBLE
      resultView.visibility = View.GONE
      fab.visibility = View.GONE
      menu.findItem(R.id.change).isEnabled = false
      menu.findItem(R.id.share).isEnabled = false
    }
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
      val response = algoClient.uploadImage(byteArray)
      if (response.isSuccess) {
        return algoClient.fetchResultImagePath(response)
      } else {
        Log.e(TAG, "Error during get result")
      }
    } catch (e: Exception) {
      Log.e(TAG, e.toString())
    }
    return null
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.change -> {
        activity?.startActivityForResult(
          Intent.createChooser(pickPhoto, getString(R.string.title_select_picture)),
          BaseActivity.REQUEST_TAKE_IMAGE
        )
        true
      }
      R.id.share -> {
        shareResultImgFile()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun saveResultImage() {
//    resultImgFile = getOutputMediaFile()
//    try {
//      val fos = FileOutputStream(resultImgFile)
//      val byteArray = toByteArray(BitmapFactory.decodeFile(resultTempFilePath))
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
    private const val ARGUMENT_PICKED_IMG = "ResultFragment:img"
    private const val BUNDLE_RESULT_IMG_PATH = "ResultFragment:resultImg"

    fun newInstance(imgData: ByteArray): ResultFragment {
      val fragment = ResultFragment()
      val args = Bundle()
      args.putByteArray(ARGUMENT_PICKED_IMG, imgData)
      fragment.arguments = args
      return fragment
    }
  }
}