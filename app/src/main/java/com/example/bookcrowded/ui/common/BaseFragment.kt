package com.example.bookcrowded.ui.common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.bookcrowded.ui.common.listener.ProgressUIInterface

/**
 * onBackPressed() 이벤트가 구현된 BaseFragment
 */
abstract class BaseFragment : Fragment(), ProgressUIInterface {

    override fun showProgressUI() {
        (activity as? BaseActivity)?.showProgressUI()
    }

    override fun hideProgressUI() {
        (activity as? BaseActivity)?.hideProgressUI()
    }

    /**
     * Call when on back pressed in Resumed fragment.
     */
    open fun onBackPressed(): Boolean {
        return false
    }

    /**
     * open functions
     */
    open fun onNewExtra(bundle: Bundle) {
        //stub
    }
}