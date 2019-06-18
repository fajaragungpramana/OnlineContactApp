package com.mengsoftstudio.android.onlinecontact.models

class Contact(
    var id: String?,
    var photoId: String?,
    var photo: String?,
    var name: String?,
    var phone: String?
) {
    data class Builder(
        private var id: String? = null,
        private var photoId: String? = null,
        private var photo: String? = null,
        private var name: String? = null,
        private var phone: String? = null
    ) {
        fun setId(id: String?) = apply { this.id = id }
        fun setPhotoId(photoId: String?) = apply { this.photoId = photoId }
        fun setPhoto(photo: String?) = apply { this.photo = photo }
        fun setName(name: String?) = apply { this.name = name }
        fun setPhone(phone: String?) = apply { this.phone = phone }
        fun build() = Contact(id, photoId, photo, name, phone)
    }
}