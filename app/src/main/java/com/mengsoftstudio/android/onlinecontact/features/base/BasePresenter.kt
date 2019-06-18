package com.mengsoftstudio.android.onlinecontact.features.base

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

abstract class BasePresenter {

    protected fun db(): FirebaseFirestore = FirebaseFirestore.getInstance()
    protected fun storage(): StorageReference = FirebaseStorage.getInstance().reference

}