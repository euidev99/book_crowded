package com.example.bookcrowded.ui.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.bookcrowded.R
import com.example.bookcrowded.databinding.ActivityMainBinding
import com.example.bookcrowded.databinding.ViewProgressBinding


class LoadingProgress {
    lateinit var mDialog: Dialog

//    lateinit var mProgressBar: ProgressBar
    private var mnCount = 0

    fun init(context: Context){
        val view:View = LayoutInflater.from(context).inflate(R.layout.view_progress,null)
        val builder:AlertDialog.Builder = AlertDialog.Builder(context)

//        mProgressBar = view.findViewById(R.id.vp_progress)

        builder.setView(view)

        mDialog = builder.create()
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        mDialog.window?.setDimAmount(0.0f)
        mDialog.setCancelable(false)
    }

    fun setDialog(show: Boolean) {
        if (show) mnCount++ else mnCount--

        if (mnCount > 0) {
//            try{
                mDialog.show()
//            }
//            catch (e:WindowManager.BadTokenException){
//                e.printStackTrace()
//                init(ApplicationProxy.getInstance())
//            }

        } else {
            try {
                mDialog.dismiss()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
    }

    fun isShowing() : Boolean{
        return mDialog.isShowing
    }
}