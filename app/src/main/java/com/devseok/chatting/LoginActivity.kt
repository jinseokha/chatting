package com.devseok.chatting

import android.content.Intent
import android.content.SharedPreferences
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
    val PRIVATE_MODE = 0
    val PREF_NAME = "login"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val settings: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val editor: SharedPreferences.Editor = settings.edit()

        auth = FirebaseAuth.getInstance()

        val id = settings.getString("id","")
        val pw = settings.getString("pw","")
        Log.d("test", "id = " + id);
        Log.d("test", "pw = " + pw);

        login_email.setText("user17@naver.com")
        login_password.setText("123456")

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

                        if(signIn_idchk.isChecked) {
                            editor.putString("id", email);
                            editor.putString("pw", password);
                        }




                        val intent = Intent(this, ChatListActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)

                    } else {
                        Log.d(TAG,"실패@")

                    }


                }
        }

        signUp_text.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }


    }
}
