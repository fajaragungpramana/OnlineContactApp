package com.mengsoftstudio.android.onlinecontact.features.contact.add

import android.util.Patterns
import com.mengsoftstudio.android.onlinecontact.features.base.BasePresenter
import com.mengsoftstudio.android.onlinecontact.models.Contact
import com.mengsoftstudio.android.onlinecontact.models.constant.Constant
import java.util.*
import kotlin.collections.HashMap

class AddContactPresenter(private val view: AddContactContract.View)
    : BasePresenter(), AddContactContract.Presenter {

    override fun doAddingContact(photo: ByteArray?, name: String, phone: String) {
        val photoId = "${UUID.randomUUID()}.jpg"

        val contact = Contact.Builder()
                .setPhotoId(photoId)
                .setName(name)
                .setPhone(phone)
                .build()

        if(photo != null) {

            if(isVerified(contact)) {

                view.onShowLoading()
                val photoRef = storage().child("contacts").child(contact.photoId!!)
                photoRef.putBytes(photo)
                        .addOnSuccessListener {

                            photoRef.downloadUrl
                                    .addOnSuccessListener { url ->

                                        val map = HashMap<String, String?>()
                                        map["photo_id"] = contact.photoId
                                        map["photo"] = url.toString()
                                        map["name"] = contact.name
                                        map["phone"] = contact.phone

                                        db().collection("contacts").add(map)
                                                .addOnSuccessListener {
                                                    view.onHideLoading()
                                                    view.onAddContactSuccess()

                                                    view.onMessage(Constant.MSG.SUCCESS.ADD_CONTACT)
                                                }
                                                .addOnFailureListener { e ->
                                                    view.onHideLoading()
                                                    view.onMessage(e.message)
                                                }


                                    }
                                    .addOnFailureListener { e ->
                                        view.onHideLoading()
                                        view.onMessage(e.message)
                                    }

                        }
                        .addOnFailureListener { e ->
                            view.onHideLoading()
                            view.onMessage(e.message)
                        }

            }

        } else {
            view.onMessage(Constant.MSG.FAILURE.NULL_PHOTO)
        }

    }

    private fun isVerified(contact: Contact): Boolean {
        if(contact.name!!.length >= 4) {
            view.onFailureName(null)
            if(Patterns.PHONE.matcher(contact.phone).matches() && contact.phone!!.length >= 8) {
                view.onFailurePhone(null)
                return true
            } else
                view.onFailurePhone(Constant.MSG.FAILURE.PHONE)
        } else
            view.onFailureName(Constant.MSG.FAILURE.NAME)

        return false
    }

}