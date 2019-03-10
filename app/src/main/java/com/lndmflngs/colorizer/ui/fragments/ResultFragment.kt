package com.lndmflngs.colorizer.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.algorithmia.AlgoClient
import com.lndmflngs.colorizer.extensions.getBitmapUri
import com.lndmflngs.colorizer.extensions.toast
import com.lndmflngs.colorizer.utils.AppUtils
import kotlinx.android.synthetic.main.fragment_result.fab
import kotlinx.android.synthetic.main.fragment_result.progressBar
import kotlinx.android.synthetic.main.fragment_result.resultView
import kotlinx.android.synthetic.main.include_result.resultImageView

class ResultFragment : Fragment() {

  private lateinit var algoClient: AlgoClient
  private lateinit var resultImgSource: String

  private var isMenuActionsEnabled: Boolean = false
  private val menuItems = arrayOf(R.id.change, R.id.share)

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
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onPrepareOptionsMenu(menu: Menu) {
    super.onPrepareOptionsMenu(menu)
    menuItems.forEach { menu.findItem(it).isEnabled = isMenuActionsEnabled }
  }

  @SuppressLint("StaticFieldLeak")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // set fab listener to share result
    fab.setOnClickListener { shareResultImgFile() }
    if (savedInstanceState != null) {
      resultImgSource = savedInstanceState.getString(BUNDLE_RESULT_IMG_SOURCE)!!
      showColoredResult()
    } else {
      // load resultImgSource abd show it
      val byteArray = arguments?.getByteArray(ARGUMENT_PICKED_IMG)!!
      loadData(byteArray)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(BUNDLE_RESULT_IMG_SOURCE, resultImgSource)
    super.onSaveInstanceState(outState)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.change -> {
        AppUtils.startPickImage(activity!!)
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
    AppUtils.lockOrientation(activity)
    // The first call to this algorithm will take a bit longer than sequential calls to due algorithm initialization.
    // All following calls will be significantly faster.
    context?.toast(R.string.msg_warning, Toast.LENGTH_LONG)
    algoClient.loadData(byteArray) {
      resultImgSource = it
      showColoredResult()
      AppUtils.unlockOrientation(activity)
    }
  }

  fun loadNewData(byteArray: ByteArray) {
    showResultUI(false) // show load
    loadData(byteArray)
  }

  private fun showColoredResult() {
    showResultUI(true)
    Glide.with(this).load(resultImgSource).fitCenter().into(resultImageView)
  }

  private fun showResultUI(isShow: Boolean) {
    if (isShow) {
      progressBar.visibility = View.GONE
      resultView.visibility = View.VISIBLE
      fab.visibility = View.VISIBLE
      isMenuActionsEnabled = true
    } else {
      progressBar.visibility = View.VISIBLE
      resultView.visibility = View.GONE
      fab.visibility = View.GONE
      isMenuActionsEnabled = false
    }
    activity?.invalidateOptionsMenu()
  }

  private fun shareResultImgFile() {
    val bmpUri = context?.getBitmapUri(resultImageView)
    bmpUri?.let { AppUtils.shareResultImgFile(activity!!, it) }
  }

  companion object {
    private const val TAG = "ResultFragment"

    private const val ARGUMENT_PICKED_IMG = "ResultFragment:img"
    private const val BUNDLE_RESULT_IMG_SOURCE = "ResultFragment:resultSrc"

    fun newInstance(imgData: ByteArray): ResultFragment {
      val fragment = ResultFragment()
      val args = Bundle()
      args.putByteArray(ARGUMENT_PICKED_IMG, imgData)
      fragment.arguments = args
      return fragment
    }
  }
}