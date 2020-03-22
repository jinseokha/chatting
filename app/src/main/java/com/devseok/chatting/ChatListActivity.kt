package com.devseok.chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.devseok.chatting.Adapter.UserItem
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_list.*

class ChatListActivity : AppCompatActivity() {

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    private val TAG: String = ChatListActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        myChatList.setOnClickListener {
            val intent = Intent(this, MyRoomActivity::class.java)
            startActivity(intent)
        }

        val adapter = GroupAdapter<GroupieViewHolder>()

        recyclerView_list.adapter = adapter

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    adapter.add(
                        UserItem(
                            document.get(
                                "username"
                            ).toString(),
                            document.get("uid").toString()
                        )
                    )

                    Log.d(TAG, document.get("username").toString())
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
                recyclerView_list.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents.", exception)
            }

        adapter.setOnItemClickListener{ item, view ->


            val uid = (item as UserItem).uid
            val name = (item as UserItem).name

            val intent = Intent(this, ChatRoomActivity::class.java)
            intent.putExtra("yourUid", uid)
            intent.putExtra("yourName", name)
            startActivity(intent)

        }

    }
}
