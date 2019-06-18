package com.mengsoftstudio.android.onlinecontact.features.contact.add

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.View
import com.mengsoftstudio.android.onlinecontact.R
import com.mengsoftstudio.android.onlinecontact.features.base.BaseActivity
import com.mengsoftstudio.android.onlinecontact.features.contact.MainActivity
import com.mengsoftstudio.android.onlinecontact.models.constant.Constant
import com.mengsoftstudio.android.onlinecontact.utils.PermissionUtil
import kotlinx.android.synthetic.main.activity_contact_add.*
import kotlinx.android.synthetic.main.support_toolbar.*
import org.jetbrains.anko.intentFor
import java.io.ByteArrayOutputStream

class AddContactActivity : BaseActivity(), AddContactContract.View {

    private var photo: ByteArray? = null

    private lateinit var presenter: AddContactPresenter

    override fun getContentView(): Int = R.layout.activity_contact_add

    override fun onCreated(savedInstanceState: Bundle?) {

        setSupportActionBar(support_toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.title = getString(R.string.add_contact)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        cv_add_contact_photo.setOnClickListener { onGalleryPermissionGranted() }
        fab_add_contact_photo.setOnClickListener { onGalleryPermissionGranted() }

        presenter = AddContactPresenter(this@AddContactActivity)

        btn_done.setOnClickListener {
            val name = et_name.text.toString().trim()
            val phone = et_phone.text.toString().trim()

            presenter.doAddingContact(photo, name, phone)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {

            val uri = data?.data
            val photoStream = contentResolver.openInputStream(uri!!)
            val photoBitmap = BitmapFactory.decodeStream(photoStream)

            iv_contact_photo.setImageBitmap(photoBitmap)

            val byteArrayOutputStream = ByteArrayOutputStream()
            val photoScale = Bitmap.createScaledBitmap(photoBitmap, 500, 500, true)
            photoScale.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)

            photo = byteArrayOutputStream.toByteArray()

        } catch (e: KotlinNullPointerException) { e.printStackTrace() }

    }

    override fun getProgressBar(): View? = i_progress

    override fun onAddContactSuccess() {
        startActivity(intentFor<MainActivity>()
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    override fun onFailureName(message: String?) { til_name.error = message }
    override fun onFailurePhone(message: String?) { til_phone.error = message }

    private fun onGalleryPermissionGranted() {
        if(PermissionUtil.isExternalStorageGranted(this@AddContactActivity))
            startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Constant.RC.GALLERY)
        else
            ActivityCompat.requestPermissions(this@AddContactActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constant.RC.GALLERY)
    }

}