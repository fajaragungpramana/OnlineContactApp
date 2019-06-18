package com.mengsoftstudio.android.onlinecontact.models.constant

object Constant {

    object KEY {
        const val CONTACT_ID = "ContactID"
    }

    object MSG {
        object SUCCESS {
            const val ADD_CONTACT = "Add Contact, Successfully!"
            const val UPDATE_CONTACT = "Update Successfully!"
            const val DELETE_CONTACT = "Delete Successfully!"
        }
        object FAILURE {
            const val NULL_PHOTO = "Please, select photo!"
            const val NAME = "Min 4 character!"
            const val PHONE = "Min 8 character!"
            const val NULL_CONTACT = "Empty contact!"
        }
    }

    object RC {
        const val GALLERY = 1
    }

}