package com.devseok.chatting.Adapter

import com.devseok.chatting.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_left_you.view.*
import kotlinx.android.synthetic.main.message_list_row.view.*

class ChatLeftYou(val msg : String) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_left_you
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.left_chat.text = msg;
    }
}