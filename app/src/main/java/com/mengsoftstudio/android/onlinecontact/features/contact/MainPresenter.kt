package com.mengsoftstudio.android.onlinecontact.features.contact

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.mengsoftstudio.android.onlinecontact.features.base.BasePresenter
import com.mengsoftstudio.android.onlinecontact.models.Contact
import com.mengsoftstudio.android.onlinecontact.models.constant.Constant

class MainPresenter(private val view: MainContract.View)
    : BasePresenter(), MainContract.Presenter {

    private var listContact = mutableListOf<Contact>()

    override fun getDisplayContacts() {

        view.onShowLoading()
        db().collection("contacts")
                .orderBy("name", Query.Direction.ASCENDING)
                .addSnapshotListener(EventListener<QuerySnapshot>{ querySnapshot, e ->
                    if(e != null) return@EventListener

                    if(querySnapshot?.documentChanges!!.isNotEmpty()) {

                        querySnapshot.documentChanges.forEach { documentChange ->

                            listContact.add(
                                    Contact.Builder()
                                            .setId(documentChange.document.id)
                                            .setPhotoId(documentChange.document.getString("photo_id"))
                                            .setPhoto(documentChange.document.getString("photo"))
                                            .setName(documentChange.document.getString("name"))
                                            .setPhone(documentChange.document.getString("phone"))
                                            .build()
                            )

                        }

                        view.onHideLoading()
                        view.onDisplayContacts(listContact)

                    } else {
                        view.onHideLoading()
                        view.onMessage(Constant.MSG.FAILURE.NULL_CONTACT)
                    }

                })

    }

}