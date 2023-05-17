package com.example.fooddeliveryapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class AddrestaurantActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var imagePreview: ImageView
    lateinit var btn_choose_image: Button
    lateinit var btn_upload_image: Button
    lateinit var progress: ProgressDialog
    lateinit var edtName: EditText
    lateinit var edtAddress: EditText
    lateinit var edtHours: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addrestaurant)
        btn_choose_image = findViewById(R.id.btn_choose_image)
        btn_upload_image = findViewById(R.id.btn_upload_image)
        imagePreview = findViewById(R.id.image_preview)
        edtName = findViewById(R.id.mEdtName)
        edtAddress = findViewById(R.id.mEdtAddress)
        edtHours = findViewById(R.id.mEdtRegHours)

        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        progress = ProgressDialog(this)
        progress.setTitle("Loading")
        progress.setMessage("Please wait...")

        btn_choose_image.setOnClickListener { launchGallery() }
        btn_upload_image.setOnClickListener { uploadImage() }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imagePreview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage(){
        var name = edtName.text.toString().trim()
        var address = edtAddress.text.toString().trim()
        var hours = edtHours.text.toString().trim()
        var id = System.currentTimeMillis().toString()
        if (name.isEmpty()){
            edtName.setError("Please fill this input")
            edtName.requestFocus()
        }else if (address.isEmpty()){
            edtAddress.setError("Please fill this input")
            edtAddress.requestFocus()
        }else if (hours.isEmpty()){
            edtHours.setError("Please fill this input")
            edtHours.requestFocus()
        }else{
            if(filePath != null){

                val ref = storageReference?.child("Restaurants/" + UUID.randomUUID().toString())
                progress.show()
                val uploadTask = ref?.putFile(filePath!!)!!.addOnCompleteListener{
                    progress.dismiss()
                    if (it.isSuccessful){
                        ref.downloadUrl.addOnSuccessListener {
                            var hotelData = Restaurant(name,address,hours,it.toString(),id)
                            var ref = FirebaseDatabase.getInstance().getReference().child("Restaurants/$id")
                            ref.setValue(hotelData)
                            Toast.makeText(this, "Restaurant submitted successfully", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this, "Restaurant submission failed", Toast.LENGTH_SHORT).show()
                    }
                }


            }else{
                Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            }
        }

    }

}

