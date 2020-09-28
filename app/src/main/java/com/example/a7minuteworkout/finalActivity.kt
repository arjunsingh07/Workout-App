package com.example.a7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.sqlitedemo.SqlLiteOpenHelper
import kotlinx.android.synthetic.main.activity_final.*
import java.text.SimpleDateFormat
import java.util.*

class finalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)


        setSupportActionBar(toolbar_finish_activity)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        toolbar_finish_activity.setNavigationOnClickListener {
            onBackPressed()
        }



        btn_finish.setOnClickListener {
            finish()

        }

        addDateToDatabase()
    }

    private fun addDateToDatabase(){
        val calendar = Calendar.getInstance()
        val dateTime = calendar.time
        Log.i("DATE:", ""+ dateTime)

        val sdf = SimpleDateFormat("dd MM yyyy HH:mm:ss")
        val date = sdf.format(dateTime)

        val dbHandler = SqlLiteOpenHelper(this, null)
        dbHandler.addDate(date)
        Log.i("DATE:", ""+date)
    }




}