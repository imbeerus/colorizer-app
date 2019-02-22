package com.lndmflngs.colorizer.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.algorithmia.AlgoClient
import com.lndmflngs.colorizer.extensions.pickPhoto
import com.lndmflngs.colorizer.ui.BaseActivity
import com.lndmflngs.colorizer.utils.GlideUtils
import kotlinx.android.synthetic.main.fragment_result.fab
import kotlinx.android.synthetic.main.fragment_result.progressBar
import kotlinx.android.synthetic.main.fragment_result.resultView
import kotlinx.android.synthetic.main.include_result.resultImageView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ResultFragment : Fragment() {

  private lateinit var algoClient: AlgoClient
  private lateinit var resultImgPath: String
  private lateinit var menu: Menu

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    algoClient = AlgoClient.getInstance(getString(R.string.algorithmia_api_key))
    setHasOptionsMenu(true)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_result, container, false)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_result, menu)
    this.menu = menu
    super.onCreateOptionsMenu(menu, inflater)
  }

  @SuppressLint("StaticFieldLeak")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // set fab listener to share result
    fab.setOnClickListener { shareResultImgFile() }
    if (savedInstanceState != null) {
      resultImgPath = savedInstanceState.getString(BUNDLE_RESULT_IMG_PATH)!!
      showColoredResult()
    } else {
      // load resultImgPath abd show it
      val byteArray = arguments?.getByteArray(ARGUMENT_PICKED_IMG)!!
      loadData(byteArray)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(BUNDLE_RESULT_IMG_PATH, resultImgPath)
    super.onSaveInstanceState(outState)
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

  private fun loadData(byteArray: ByteArray) {
    algoClient.loadData(byteArray) {
      resultImgPath = it
      showColoredResult()
    }
  }

  fun loadNewData(byteArray: ByteArray) {
    showResultUI(false) // show load
    loadData(byteArray)
  }

  private fun showColoredResult() {
    showResultUI(true)
    Glide.with(this).load(resultImgPath).fitCenter().into(resultImageView)
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

  private fun shareResultImgFile() {
    val bmpUri = getResultBitmapUri()
    // Construct share intent as described above based on bitmap
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      putExtra(Intent.EXTRA_STREAM, bmpUri)
      type = "image/*"
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
  }

  private fun getResultBitmapUri(): Uri? {
    // Extract Bitmap from ImageView drawable
    val drawable = resultImageView.drawable
    val bmp = if (drawable is BitmapDrawable) {
      (resultImageView.drawable as BitmapDrawable).bitmap
    } else {
      null
    }
    // Store image to default external storage directory
    try {
      val file = File(
        context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "${System.currentTimeMillis()}.png"
      )
      val out = FileOutputStream(file)
      bmp!!.compress(Bitmap.CompressFormat.PNG, 100, out)
      out.close()

      return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        FileProvider.getUriForFile(context!!, getString(R.string.file_provider_authority), file)
      } else {
        Uri.fromFile(file)
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
    return null
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