package com.example.firebasedemoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedemoapp.databinding.ActivityLoginActivityBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginActivityBinding
    private var googleSignInClient: GoogleSignInClient? = null
    private val TAG = "mainTag"
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SignInButton.setVisibility(View.VISIBLE)
        mAuth = FirebaseAuth.getInstance()

        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("581475788083-0sagr0ig5qqofo445bve8ukev3bc5f5a.apps.googleusercontent.com")
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.SignInButton.setOnClickListener {

            binding.progressBar.setVisibility(View.VISIBLE)

            signInM()
        }
    }

    private fun signInM() {
        val singInIntent = googleSignInClient!!.signInIntent
        startActivityForResult(singInIntent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            binding.SignInButton.setVisibility(View.INVISIBLE)
            val account = task.getResult(ApiException::class.java)
//            Toast.makeText(this, "Signed In successfully", Toast.LENGTH_LONG).show()
            FirebaseGoogleAuth(account)
        } catch (e: ApiException) {
            Log.e(TAG, "signInResult:failed code=" + task)
            Toast.makeText(this, "SignIn Failed!!!", Toast.LENGTH_LONG).show()
            FirebaseGoogleAuth(null)
        }
    }

    private fun FirebaseGoogleAuth(account: GoogleSignInAccount?) {
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            Toast.makeText(this, "successful", Toast.LENGTH_LONG).show()
            mAuth.signInWithCredential(credential).addOnCompleteListener(this)
            { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "successful", Toast.LENGTH_LONG).show()
                    val firebaseUser = mAuth.currentUser
                    if (firebaseUser != null) {
                        UpdateUI(firebaseUser)
                    }
                } else {
                    Toast.makeText(this, "Failed!", Toast.LENGTH_LONG).show()
                    UpdateUI(null)
                }
            }
        }
    }

    private fun UpdateUI(fUser: FirebaseUser?) {
//        binding.SignOut.setVisibility(View.VISIBLE)
        //getLastSignedInAccount returned the account
        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (account != null) {
            val personName = account.displayName
            val personGivenName = account.givenName
            val personEmail = account.email
            val personId = account.id
//            Toast.makeText(this, "$personName  $personEmail", Toast.LENGTH_LONG).show()
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("id", personId)
            startActivity(intent)
            finish()
            binding.progressBar.setVisibility(View.INVISIBLE)

        }
    }

}