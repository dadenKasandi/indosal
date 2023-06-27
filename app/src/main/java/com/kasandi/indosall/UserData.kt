package com.kasandi.indosall

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class UserData : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etAddress: EditText
    private lateinit var btSaveButton: Button
    private lateinit var btBrowse: Button

    private lateinit var uri: Uri

    private var storageRef = Firebase.storage
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)

        storageRef = FirebaseStorage.getInstance()

        etFirstName = findViewById(R.id.et_nama_depan)
        etLastName = findViewById(R.id.et_nama_belakang)
        etPhoneNumber = findViewById(R.id.et_phone)
        rgGender = findViewById(R.id.rg_gender)
        etAddress = findViewById(R.id.et_alamat)
        btSaveButton = findViewById(R.id.bt_save)
        btBrowse = findViewById(R.id.bt_browse_image)

        val galleryImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                if (it != null) {
                    uri = it
                }
            })

        btBrowse.setOnClickListener {
            galleryImage.launch("image/*")
        }

        btSaveButton.setOnClickListener {

            val sFirstName = etFirstName.text.toString().trim()
            val sLastName = etLastName.text.toString().trim()
            val sPhoneNumber = etPhoneNumber.text.toString().trim()
            var sGender = "male"
            val id = rgGender.checkedRadioButtonId
            when (id) {
                R.id.rb_male -> sGender = "Laki-laki"
                R.id.rb_female -> sGender = "Perempuan"
            }
            val sAddress = etAddress.text.toString().trim()

            val userMap = hashMapOf(
                "firstname" to sFirstName,
                "lastname" to sLastName,
                "phone" to sPhoneNumber,
                "gender" to sGender,
                "address" to sAddress
            )

            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            storageRef.getReference("images").child(System.currentTimeMillis().toString())
                .putFile(uri)
                .addOnSuccessListener { task->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            val databaseReference = FirebaseDatabase.getInstance().getReference("userImage")

                            val mapImage = mapOf(
                                "url" to it.toString()
                            )

                            databaseReference.child(userId).setValue(mapImage)
                        }
                }


            db.collection("pelanggan").document(userId).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()
                    etAddress.text.clear()
                    etFirstName.text.clear()
                    etLastName.text.clear()
                    etPhoneNumber.text.clear()

                    startActivity(Intent(this@UserData, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                }


        }
    }
}