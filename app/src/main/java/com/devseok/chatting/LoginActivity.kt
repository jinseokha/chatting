package com.devseok.chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.devseok.chatting.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth// ...
// Initialize Firebase Auth

    private val TAG: String = LoginActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        login_button.setOnClickListener {
            val email = login_email.text.toString()
            val password = login_password.text.toString()


            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG,"성공@")

                        val uid = FirebaseAuth.getInstance().uid ?: ""

                        val user = User(uid, login_email.text.toString())

                        // DB에 유저 정보 넣어줘야 하는 곳
                        val db = FirebaseFirestore.getInstance().collection("users")
                        db.document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d(TAG,"DB Success")
                            }
                            .addOnFailureListener {
                                Log.d(TAG,"DB Fail")
                            }

                        val intent = Intent(this, ChatListActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)

                    } else {
                        Log.d(TAG,"실패@")

                    }


                }
        }
    }
}
