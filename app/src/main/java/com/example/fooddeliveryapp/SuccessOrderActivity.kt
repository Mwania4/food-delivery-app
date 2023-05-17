package com.example.fooddeliveryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import com.example.fooddeliveryapp.models.RestaurentModel

class SuccessOrderActivity : AppCompatActivity() {
    lateinit var buttonDone:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_order)

        val restaurentModel: RestaurentModel? = intent.getParcelableExtra("RestaurentModel")
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setTitle(restaurentModel?.name)
        actionBar?.setSubtitle(restaurentModel?.address)
        actionBar?.setDisplayHomeAsUpEnabled(false)


        buttonDone.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}