package com.example.bookcrowded.ui.common

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.bookcrowded.ui.common.listener.ProgressUIInterface
import com.example.bookcrowded.util.GlobalValues
import kotlinx.coroutines.launch

/**
 * 기본 베이스 액티비티
 * 프로그레스 리스너와, 추가 밸류를 사용하고자 함
 * 단, 데이터 바인딩 유틸을 사용하지 않을 시 글로벌 스택은 그렇게 필요는 없음.
 */
abstract class BaseActivity : AppCompatActivity(), ProgressUIInterface {

    private var extraAny: MutableMap<String, Any?>? = null

    private var mFromComponentName: ComponentName? = null
    private var mProgressView: View? = null

    private val TAG = "BASE"

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initExtraAny(intent)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        GlobalValues.remove(intent.hashCode())
    }

    @CallSuper
    override fun startActivity(intent: Intent) {
        checkExtraAny(intent)
        intent.putExtra(EXTRA_FROM, componentName)
        Log.d(TAG,">> startActivity() $componentName")
        super.startActivity(intent)
    }

    @CallSuper
    override fun startActivity(intent: Intent, options: Bundle?) {
        checkExtraAny(intent)
        intent.putExtra(EXTRA_FROM, componentName)
        Log.d(TAG, ">> startActivity() $componentName")
        super.startActivity(intent, options)
    }

    @Suppress("DEPRECATION")
    @CallSuper
    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        checkExtraAny(intent)
        intent.putExtra(EXTRA_FROM, componentName)
        super.startActivityForResult(intent, requestCode)
    }

    private fun initExtraAny(intent: Intent?) {
        intent ?: return
        mFromComponentName = intent.getParcelableExtra(EXTRA_FROM)
        val extraCode = intent.getIntExtra(EXTRA_ANY, 0)
        if (extraCode != 0) {
            extraAny = GlobalValues.pop(extraCode)
        }
    }

    private fun checkExtraAny(intent: Intent) {
        val code = intent.hashCode()
        if (intent.hasExtra(EXTRA_ANY) && !GlobalValues.containsKey(code)) {
            GlobalValues[code] = extraAny
        }
    }

    fun Intent.getExtraAny(key: String): Any? {
        return extraAny?.get(key)
    }

    fun getFromComponentName(): ComponentName? {
        return mFromComponentName
    }

    @CallSuper
    override fun onNewIntent(intent: Intent?) {
        initExtraAny(intent)
        super.onNewIntent(intent)
    }

    override fun showProgressUI() {
        mProgressView?.let {
            //불필요한 CoroutineScope 를 사용하지 않기 위함
            if (it.parent != null) return
        } ?: let {
            mProgressView = ProgressBar(this)
        }
        lifecycleScope.launchWhenCreated {
            if (mProgressView?.parent != null) return@launchWhenCreated
            addContentView(
                mProgressView, FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
            )
        }
    }

    override fun hideProgressUI() {
        //불필요한 CoroutineScope 를 사용하지 않기 위함
        if (mProgressView == null || mProgressView?.parent == null) return
        lifecycleScope.launch {
            (mProgressView?.parent as? ViewGroup)?.removeView(mProgressView)
        }
    }

    final override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it is BaseFragment && it.isResumed && it.isVisible && it.onBackPressed()) return
        }
        if (handleBackPressed()) return
        super.onBackPressed()
    }

    open fun handleBackPressed(): Boolean {
        return false
    }

    companion object {
        private const val EXTRA_ANY = "extra_any"
        private const val EXTRA_FROM = "extra_from"

        fun Intent.putExtraAny(key: String, value: Any?): Intent {
            val code = hashCode()
            GlobalValues.put(code, key, value)
            putExtra(EXTRA_ANY, code)
            return this
        }
    }
}