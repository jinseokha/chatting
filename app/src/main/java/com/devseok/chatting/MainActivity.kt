package com.devseok.chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.devseok.chatting.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth// ...

    private val TAG: String = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()



        join_button.setOnClickListener {

            val email = email_area.text.toString()
            val password = password_area.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG,"성공@")

                        val uid = FirebaseAuth.getInstance().uid ?: ""

                        val user = User(uid, userName.text.toString())

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
                        Log.d(TAG, "실패")
                        // ...
                    }
                }
        }

        login_button_main.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent);
        }


    }
}
