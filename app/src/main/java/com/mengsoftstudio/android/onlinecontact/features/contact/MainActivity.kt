package com.mengsoftstudio.android.onlinecontact.features.contact

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mengsoftstudio.android.onlinecontact.features.contact.add.AddContactActivity
import com.mengsoftstudio.android.onlinecontact.R
import com.mengsoftstudio.android.onlinecontact.features.adapter.ContactAdapter
import com.mengsoftstudio.android.onlinecontact.features.base.BaseActivity
import com.mengsoftstudio.android.onlinecontact.models.Contact
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.support_toolbar.*
import org.jetbrains.anko.intentFor

class MainActivity : BaseActivity(), MainContract.View {

    private lateinit var presenter: MainPresenter

    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreated(savedInstanceState: Bundle?) {

        setSupportActionBar(support_toolbar)

        btn_add_contact.setOnClickListener {
            startActivity(intentFor<AddContactActivity>()
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }

        presenter = MainPresenter(this@MainActivity)
        presenter.getDisplayContacts()

    }

    override fun getProgressBar(): View? = i_progress

    override fun onDisplayContacts(listContact: List<Contact>) {
        rv_contact.layoutManager = LinearLayoutManager(this@MainActivity)
        rv_contact.adapter = ContactAdapter(this@MainActivity, listContact)
        (rv_contact.adapter as ContactAdapter).notifyDataSetChanged()
    }

}