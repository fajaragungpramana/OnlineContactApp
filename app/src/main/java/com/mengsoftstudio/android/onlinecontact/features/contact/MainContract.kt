package com.mengsoftstudio.android.onlinecontact.features.contact

import com.mengsoftstudio.android.onlinecontact.features.base.BaseView
import com.mengsoftstudio.android.onlinecontact.models.Contact

interface MainContract {

    interface View : BaseView {
        fun onDisplayContacts(listContact: List<Contact>)
    }

    interface Presenter {
        fun getDisplayContacts()
    }

}