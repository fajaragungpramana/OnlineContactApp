package com.mengsoftstudio.android.onlinecontact.features.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.mengsoftstudio.android.onlinecontact.extensions.gone
import com.mengsoftstudio.android.onlinecontact.extensions.visible
import org.jetbrains.anko.toast

abstract class BaseActivity : AppCompatActivity(), BaseView {

    abstract fun getContentView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())

        onCreated(savedInstanceState)

    }

    abstract fun onCreated(savedInstanceState: Bundle?)
    abstract fun getProgressBar(): View?

    override fun onShowLoading() {
        getProgressBar()?.visible()
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onHideLoading() {
        getProgressBar()?.gone()
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun onMessage(message: String?) { toast(message.toString()) }

}