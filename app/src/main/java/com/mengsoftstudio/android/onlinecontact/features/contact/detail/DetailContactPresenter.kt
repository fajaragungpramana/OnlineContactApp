package com.mengsoftstudio.android.onlinecontact.features.contact.detail

import android.util.Patterns
import com.google.firebase.firestore.SetOptions
import com.mengsoftstudio.android.onlinecontact.features.base.BasePresenter
import com.mengsoftstudio.android.onlinecontact.models.Contact
import com.mengsoftstudio.android.onlinecontact.models.constant.Constant
import java.util.*
import kotlin.collections.HashMap

class DetailContactPresenter(private val view: DetailContactContract.View)
    : BasePresenter(), DetailContactContract.Presenter {

    override fun getDisplayContact(contactId: String) {

        view.onShowLoading()
        db().collection("contacts").document(contactId).get()
            .addOnSuccessListener { docSnapshot ->

                view.onHideLoading()
                view.onDisplayContact(
                    Contact.Builder()
                        .setPhotoId(docSnapshot.getString("photo_id"))
                        .setPhoto(docSnapshot.getString("photo"))
                        .setName(docSnapshot.getString("name"))
                        .setPhone(docSnapshot.getString("phone"))
                        .build()
                )

            }
            .addOnFailureListener { e ->
                view.onHideLoading()
                view.onMessage(e.message)
            }

    }

    override fun doUpdateContactPhoto(photo: ByteArray?, contactId: String, photoId: String) {

        if(photo != null) {

            val photoRef = storage().child("contacts").child(photoId)

            view.onShowLoading()
            photoRef.putBytes(photo)
                .addOnSuccessListener {

                    photoRef.downloadUrl
                        .addOnSuccessListener { url ->

                            val map = HashMap<String, Any?>()
                            map["photo"] = url.toString()

                            db().collection("contacts").document(contactId).set(map, SetOptions.merge())
                                .addOnSuccessListener {
                                    view.onHideLoading()
                                    getDisplayContact(contactId)

                                    view.onMessage(Constant.MSG.SUCCESS.UPDATE_CONTACT)
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

    }

    override fun doUpdateContactName(newName: String?, contactId: String) {

        if(newName!!.length >= 4) {

            val map = HashMap<String, String?>()
            map["name"] = newName

            view.onShowLoading()
            db().collection("contacts").document(contactId).set(map, SetOptions.merge())
                    .addOnSuccessListener {
                        view.onHideLoading()
                        view.onMessage(Constant.MSG.SUCCESS.UPDATE_CONTACT)

                        getDisplayContact(contactId)
                    }
                    .addOnFailureListener { e ->
                        view.onHideLoading()
                        view.onMessage(e.message)
                    }

        } else
            view.onMessage(Constant.MSG.FAILURE.NAME)

    }

    override fun doUpdateContactPhone(newPhone: String?, contactId: String) {

        if(Patterns.PHONE.matcher(newPhone).matches() && newPhone!!.length >= 8) {

            val map = HashMap<String, String?>()
            map["phone"] = newPhone

            view.onShowLoading()
            db().collection("contacts").document(contactId).set(map, SetOptions.merge())
                    .addOnSuccessListener {
                        view.onHideLoading()
                        view.onMessage(Constant.MSG.SUCCESS.UPDATE_CONTACT)

                        getDisplayContact(contactId)
                    }
                    .addOnFailureListener { e ->
                        view.onHideLoading()
                        view.onMessage(e.message)
                    }

        } else
            view.onMessage(Constant.MSG.FAILURE.PHONE)

    }

    override fun doDeleteContact(contactId: String, photoId: String) {
        view.onShowLoading()
        storage().child("contacts").child(photoId).delete()
            .addOnSuccessListener {

                db().collection("contacts").document(contactId).delete()
                    .addOnSuccessListener {
                        view.onHideLoading()
                        view.onDeleteContactSuccess()

                        view.onMessage(Constant.MSG.SUCCESS.DELETE_CONTACT)
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

}