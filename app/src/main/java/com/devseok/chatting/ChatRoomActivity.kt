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

        /*adapter.add(ChatLeftYou())
        adapter.add(ChatLeftYou())
        adapter.add(ChatRightMe())
        adapter.add(ChatLeftYou())
        adapter.add(ChatRightMe())
        adapter.add(ChatLeftYou())
        adapter.add(ChatRightMe())
        adapter.add(ChatLeftYou())
        adapter.add(ChatRightMe())
        adapter.add(ChatLeftYou())*/


        val db : FirebaseFirestore = FirebaseFirestore.getInstance()


        // 데이터 불러오기
        /*db.collection("message")
            .orderBy("time")
            .get()
            .addOnSuccessListener {result ->
                for (document in result) {
                    Log.d(TAG, document.toString())

                    val senderId: Any? = document.get("myUid")
                    val msg : String = document.get("message").toString()

                    if (senderId!!.equals(myUid)) {
                        adapter.add(ChatRightMe(msg))
                    } else {
                        adapter.add(ChatLeftYou(msg))
                    }

                    //만약 내가 보낸 메시지일 때

                    //만약 내가 보낸 메시지가 아닐 때
                }

                recyclerView_chat.adapter  = adapter
            }*/

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
                Log.d(TAG, "p0 : " + p0)

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

            /*val message = editText.text.toString()

            editText.setText("")

            val chat = ChatModel(myUid.toString(), yourUid.toString(), message, System.currentTimeMillis())

            db.collection("message")
                .add(chat)
                .addOnSuccessListener {
                    Log.d(TAG, "성공")
                }
                .addOnFailureListener {
                    Log.d(TAG, "실패")
                }*/
        }

    }
}
