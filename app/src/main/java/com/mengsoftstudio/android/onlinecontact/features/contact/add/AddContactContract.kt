package com.mengsoftstudio.android.onlinecontact.features.contact.add

import com.mengsoftstudio.android.onlinecontact.features.base.BaseView

interface AddContactContract {

    interface View : BaseView {
        fun onAddContactSuccess()

        fun onFailureName(message: String?)
        fun onFailurePhone(message: String?)
    }

    interface Presenter {
        fun doAddingContact(photo: ByteArray?, name: String, phone: String)
    }

}