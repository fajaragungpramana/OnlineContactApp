package com.mengsoftstudio.android.onlinecontact.features.contact.detail

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.mengsoftstudio.android.onlinecontact.R
import com.mengsoftstudio.android.onlinecontact.features.base.BaseActivity
import com.mengsoftstudio.android.onlinecontact.features.contact.MainActivity
import com.mengsoftstudio.android.onlinecontact.models.Contact
import com.mengsoftstudio.android.onlinecontact.models.constant.Constant
import com.mengsoftstudio.android.onlinecontact.utils.PermissionUtil
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_contact_detail.*
import kotlinx.android.synthetic.main.support_edit_name.view.*
import kotlinx.android.synthetic.main.support_edit_phone.view.*
import kotlinx.android.synthetic.main.support_toolbar.*
import org.jetbrains.anko.intentFor
import java.io.ByteArrayOutputStream

class DetailContactActivity : BaseActivity(), DetailContactContract.View {

    private var photo: ByteArray? = null

    private lateinit var photoId: String
    private lateinit var contactId: String
    private lateinit var phone: String
    private lateinit var presenter: DetailContactPresenter

    override fun getContentView(): Int = R.layout.activity_contact_detail

    override fun onCreated(savedInstanceState: Bundle?) {

        setSupportActionBar(support_toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.title = getString(R.string.detail_contact)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        contactId = intent.getStringExtra(Constant.KEY.CONTACT_ID)

        presenter = DetailContactPresenter(this@DetailContactActivity)
        presenter.getDisplayContact(contactId)

        cv_contact_photo.setOnClickListener { onGalleryPermissionGranted() }
        cv_camera.setOnClickListener { onGalleryPermissionGranted() }

        iv_edit_contact_name.setOnClickListener {
            val view = LayoutInflater.from(this@DetailContactActivity).inflate(R.layout.support_edit_name, null)

            AlertDialog.Builder(this@DetailContactActivity)
                    .setTitle(getString(R.string.update_contact))
                    .setView(view)
                    .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                        val newName = view.et_edit_name.text.toString().trim()

                        presenter.doUpdateContactName(newName, contactId)
                        dialog.dismiss()
                    }
                    .show()
        }

        iv_edit_contact_phone.setOnClickListener {
            val view = LayoutInflater.from(this@DetailContactActivity).inflate(R.layout.support_edit_phone, null)

            AlertDialog.Builder(this@DetailContactActivity)
                    .setTitle(getString(R.string.update_contact))
                    .setView(view)
                    .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                        val newPhone = view.et_edit_phone.text.toString().trim()

                        presenter.doUpdateContactPhone(newPhone, contactId)
                        dialog.dismiss()
                    }
                    .show()
        }

    }

    override fun getProgressBar(): View? = i_progress

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_detail_contact, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean =
        when(item?.itemId) {

            R.id.action_delete -> {
                AlertDialog.Builder(this@DetailContactActivity)
                    .setTitle(getString(R.string.confirm_delete))
                    .setMessage(getString(R.string.delete_msg))
                    .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                        presenter.doDeleteContact(contactId, photoId)
                    }
                    .show()

                true
            }

            R.id.action_call -> {
                if(PermissionUtil.isPhoneCallGranted(this@DetailContactActivity))
                    startActivity(Intent(Intent.ACTION_CALL)
                        .setData(Uri.parse("tel:$phone")))
                else
                    ActivityCompat.requestPermissions(this@DetailContactActivity, arrayOf(Manifest.permission.CALL_PHONE), 0)

                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {

            val uri = data?.data
            val photoStream = contentResolver.openInputStream(uri!!)
            val photoBitmap = BitmapFactory.decodeStream(photoStream)

            val byteArrayOuputStream = ByteArrayOutputStream()
            val photoScale = Bitmap.createScaledBitmap(photoBitmap, 500, 500, true)
            photoScale.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOuputStream)

            photo = byteArrayOuputStream.toByteArray()
            presenter.doUpdateContactPhoto(photo, contactId, photoId)

        } catch (e: KotlinNullPointerException) { e.printStackTrace() }

    }

    override fun onDisplayContact(contact: Contact) {
        this.photoId = contact.photoId!!
        this.phone = contact.phone!!

        Picasso.get()
            .load(contact.photo)
            .placeholder(resources.getDrawable(R.drawable.ic_person_white))
            .error(resources.getDrawable(R.drawable.ic_person_white))
            .into(iv_contact_photo)
        tv_contact_name.text = contact.name
        tv_contact_phone.text = contact.phone
    }

    override fun onDeleteContactSuccess() {
        startActivity(intentFor<MainActivity>()
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        finish()
    }

    private fun onGalleryPermissionGranted() {
        if(PermissionUtil.isExternalStorageGranted(this@DetailContactActivity))
            startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), Constant.RC.GALLERY)
        else
            ActivityCompat.requestPermissions(this@DetailContactActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), Constant.RC.GALLERY)
    }

}