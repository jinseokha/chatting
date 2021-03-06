package com.devseok.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.devseok.chatting.Adapter.ChatLeftYou
import com.devseok.chatting.Adapter.ChatRightMe
import com.devseok.chatting.Model.ChatModel
import com.devseok.chatting.Model.ChatNewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.android.synthetic.main.message_list_row.*

class ChatRoomActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private val TAG: String = ChatRoomActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        auth = FirebaseAuth.getInstance()

        val myUid : String? = auth.uid
        val yourUid: String = intent.getStringExtra("yourUid")
        val name: String? = intent.getStringExtra("name")

        val adapter = GroupAdapter<GroupieViewHolder>()


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        val readRef : DatabaseReference = database.getReference("message").child(myUid.toString()).child(yourUid)

        val childEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {


            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                //Log.d(TAG, "p0 : " + p0)

                val model : ChatNewModel? = p0.getValue(ChatNewModel::class.java)

                val msg = model?.message.toString()
                val who = model?.who

                if (who == "me") {
                    adapter.add(ChatRightMe(msg))
                } else {
                    adapter.add(ChatLeftYou(msg))
                }




            }

            override fun onChildRemoved(p0: DataSnapshot) {


            }
        }

        recyclerView_chat.adapter  = adapter
        readRef.addChildEventListener(childEventListener)

        val myRef_list : DatabaseReference = database.getReference("meesage-user-list")

        button.setOnClickListener {

            val message = editText.text.toString()

            val chat = ChatNewModel(myUid.toString(), yourUid.toString(), message, System.currentTimeMillis(), "me")
            myRef.child(myUid.toString()).child(yourUid).push().setValue(chat)

            val chat_get = ChatNewModel(yourUid, myUid.toString(), message, System.currentTimeMillis(), "you")
            myRef.child(yourUid.toString()).child(myUid.toString()).push().setValue(chat_get)

            myRef_list.child(myUid.toString()).child(yourUid).setValue(chat)

            editText.setText("")


        }

    }
}
