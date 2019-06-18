package com.mengsoftstudio.android.onlinecontact.features.contact.detail

import com.mengsoftstudio.android.onlinecontact.features.base.BaseView
import com.mengsoftstudio.android.onlinecontact.models.Contact

interface DetailContactContract {

    interface View : BaseView {
        fun onDisplayContact(contact: Contact)
        fun onDeleteContactSuccess()
    }

    interface Presenter {
        fun getDisplayContact(contactId: String)
        fun doUpdateContactPhoto(photo: ByteArray?, contactId: String, photoId: String)
        fun doUpdateContactName(newName: String?, contactId: String)
        fun doUpdateContactPhone(newPhone: String?, contactId: String)
        fun doDeleteContact(contactId: String, photoId: String)
    }

}