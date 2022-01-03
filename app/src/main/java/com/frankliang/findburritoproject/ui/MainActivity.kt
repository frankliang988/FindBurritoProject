package com.frankliang.findburritoproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.frankliang.findburritoproject.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun updateAppBar(title: String, showHomeButton: Boolean) {
        supportActionBar?.let {
            it.setHomeButtonEnabled(showHomeButton)
            it.setDisplayHomeAsUpEnabled(showHomeButton)
            it.title = title
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}