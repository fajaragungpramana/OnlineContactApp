package com.mengsoftstudio.android.onlinecontact.features.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mengsoftstudio.android.onlinecontact.R
import com.mengsoftstudio.android.onlinecontact.features.contact.detail.DetailContactActivity
import com.mengsoftstudio.android.onlinecontact.models.Contact
import com.mengsoftstudio.android.onlinecontact.models.constant.Constant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_contact.view.*
import org.jetbrains.anko.intentFor

class ContactAdapter(private val context: Context, private val listContact: List<Contact>)
    : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_contact, parent, false))

    override fun getItemCount(): Int = listContact.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) { viewHolder.bindContact(listContact[position]) }


    inner class ViewHolder(private val ui: View) : RecyclerView.ViewHolder(ui) {

        fun bindContact(contact: Contact) {

            Picasso.get()
                    .load(contact.photo)
                    .placeholder(context.resources.getDrawable(R.drawable.ic_person_white))
                    .error(context.resources.getDrawable(R.drawable.ic_person_white))
                    .into(ui.iv_contact_photo)
            ui.tv_contact_name.text = contact.name

            ui.setOnClickListener {
                context.startActivity(context.intentFor<DetailContactActivity>(Constant.KEY.CONTACT_ID to contact.id)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }

        }

    }

}