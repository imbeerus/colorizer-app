package com.lndmflngs.colorizer.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.lndmflngs.colorizer.BuildConfig
import com.lndmflngs.colorizer.R
import com.lndmflngs.colorizer.utils.AppUtils

class AboutDialog : DialogFragment() {

  @SuppressLint("InflateParams")
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val builder = AlertDialog.Builder(activity as FragmentActivity)
    val aboutView = activity?.layoutInflater?.inflate(R.layout.dilaog_about, null)
    val appVersion =
      "${getString(R.string.title_version)} ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    aboutView?.findViewById<TextView>(R.id.textVersion)?.text = appVersion
    builder.setView(aboutView)
      .setNegativeButton(R.string.action_open_source) { _, _ ->
        AppUtils.openBrowser(
          activity!!,
          REPO_URL
        )
      }
    return builder.create()
  }

  companion object {
    private const val REPO_URL = "https://github.com/lndmflngs/colorizer-app"
    const val TAG = "AboutDialog"
  }
}