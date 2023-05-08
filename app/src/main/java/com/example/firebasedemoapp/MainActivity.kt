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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var firebaseDatabase: FirebaseDatabase? = null
    private var googleSignInClient: GoogleSignInClient? = null
    var databaseReference: DatabaseReference? = null
    var employeeInfo: EmployeeInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance();
        initView()

    }

    private fun initView() {

        databaseReference = firebaseDatabase!!.getReference("EmployeeInfo");

        employeeInfo = EmployeeInfo()

        binding.idBtnSendData.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                val name: String = binding.idEdtEmployeeName.getText().toString()
                val phone: String = binding.idEdtEmployeePhoneNumber.getText().toString()
                val address: String = binding.idEdtEmployeeAddress.getText().toString()

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
            val firebaseAuth = FirebaseAuth.getInstance()
            val currentUser = firebaseAuth.currentUser

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


    }


    private fun addDatatoFirebase(name: String, phone: String, address: String) {

        employeeInfo!!.employeeName = name
        employeeInfo!!.employeeContactNumber = phone
        employeeInfo!!.employeeAddress = address

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