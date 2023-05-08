package com.example.firebasedemoapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedemoapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var firebaseDatabase: FirebaseDatabase? = null
    private var googleSignInClient: GoogleSignInClient? = null
    var databaseReference: DatabaseReference? = null
    var employeeInfo: EmployeeInfo? = null
    var list = ArrayList<EmployeeInfo>()
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance();
        initView()

    }

    private fun initView() {

        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)

        if (account != null) {
            val personName = account.displayName
            val personGivenName = account.givenName
            val personEmail = account.email
            val personId = account.id

            binding.idEdtEmployeeName.setText(personEmail)
            binding.idEdtEmployeeAddress.setText(personName)
            binding.idEdtEmployeePhoneNumber.setText(personId)

        }

        databaseReference!!.child(mAuth.getUid()!!).push()

        binding.idBtnSendData.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val name: String = binding.idEdtEmployeeName.text.toString()
                val phone: String = binding.idEdtEmployeePhoneNumber.text.toString()
                val address: String = binding.idEdtEmployeeAddress.text.toString()

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(address)) {
                    Toast.makeText(this@MainActivity, "Please add some data.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    addDatatoFirebase(name, phone, address)
                }
            }
        })

        binding.btnMap.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.SignOut.setOnClickListener {
            val currentUser = mAuth.currentUser

            currentUser!!.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "OK! Works fine!")
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }.addOnFailureListener { e ->
                Log.e(
                    TAG,
                    "Ocurrio un error durante la eliminación del usuario",
                    e
                )
            }
        }

//        getDataFun()

    }

    private fun getDataFun() {
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

//                var name = snapshot.

            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(this@MainActivity, "Fail to get data.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addDatatoFirebase(name: String, phone: String, address: String) {

        var employeeInfo = EmployeeInfo(name, phone, address)

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                databaseReference!!.setValue(employeeInfo)

                Toast.makeText(this@MainActivity, "data added", Toast.LENGTH_SHORT).show()

                binding.idEdtEmployeeAddress.text.clear()
                binding.idEdtEmployeeName.text.clear()
                binding.idEdtEmployeePhoneNumber.text.clear()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Fail to add data $error", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}