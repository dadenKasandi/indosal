package com.kasandi.indosall

import android.content.Intent
//import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kasandi.indosall.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userList: ArrayList<Lapangan>

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    private lateinit var tvFirstName: TextView
    private lateinit var tvLastName: TextView
    private lateinit var imProfile: ImageView

    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvFirstName = findViewById(R.id.tv_first_name)
        tvLastName = findViewById(R.id.tv_last_name)
        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        val userId = FirebaseAuth.getInstance().currentUser!!.uid


        if(firebaseUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val ref = db.collection("pelanggan").document(userId)
        ref.get().addOnSuccessListener {
            if(it!=null){
                val fname = it.data?.get("firstname")?.toString()
                val lname = it.data?.get("lastname")?.toString()

                tvFirstName.text = fname
                tvLastName.text = lname
            }
        }


        imProfile = findViewById(R.id.im_profile_pict)

        val database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("userImage")
            .child(userId)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val url = snapshot.child("url").getValue(String::class.java)
                    if (url != null && url.isNotEmpty()) {
                        Glide.with(this@MainActivity)
                            .load(url)
                            .into(imProfile)
                    } else {
                        // Jika foto belum diupload, lakukan tindakan yang sesuai
                        // Misalnya, tampilkan foto profil default atau tampilkan pesan bahwa foto belum diupload.
                        startActivity(Intent(this@MainActivity, UserData::class.java))
                        finish()
                    }
                } else {
                    // Jika foto belum diupload, lakukan tindakan yang sesuai
                    // Misalnya, tampilkan foto profil default atau tampilkan pesan bahwa foto belum diupload.
                    startActivity(Intent(this@MainActivity, UserData::class.java))
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Penanganan jika terjadi error saat membaca data dari Firebase Database
            }
    })
        recyclerView = findViewById(R.id.rv_lapangan)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userList = arrayListOf()

        db = FirebaseFirestore.getInstance()
        db.collection("lapangan").get()
            .addOnSuccessListener{
                if(!it.isEmpty){
                    for(data in it.documents){
                        val lapangan: Lapangan? = data.toObject(Lapangan::class.java)
                        if(lapangan!=null){
                            userList.add(lapangan)
                        }
                    }
                    recyclerView.adapter = MyAdapter(userList)
                }
            }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun signOut() {
        auth.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}