package com.mengsoftstudio.android.onlinecontact.features.base

interface BaseView {

    fun onShowLoading()
    fun onHideLoading()
    fun onMessage(message: String?)

}