package com.t2m.t2chat.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.t2m.t2chat.R;
import com.t2m.t2chat.agent.Agent;
import com.t2m.t2chat.view.ChatItemView;


public class ConversationAdapter extends BaseAdapter implements Agent.Conversation.OnUpdateListener {
    private final Agent.Conversation mConversation;
    private final Handler mHandler = new Handler();

    public ConversationAdapter(Agent.Conversation conversation) {
        mConversation = conversation;
        mConversation.setOnUpdateListener(this);
    }

    @Override
    public int getCount() {
        return mConversation.size();
    }

    @Override
    public Object getItem(int position) {
        return mConversation.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get view
        View view;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.conversation_item, null);
        } else {
            view = convertView;
        }

        // get item
        Agent.Conversation.Item item = (Agent.Conversation.Item)getItem(position);

        // bind view
        bindItemView(view, item);

        // return
        return view;
    }

    @Override
    public void onUpdate(int index, Agent.Conversation.Item item) {
        mHandler.post(this::notifyDataSetChanged);
    }

    private void bindItemView(View view, Agent.Conversation.Item item) {
        LinearLayout rootView = view.findViewById(R.id.root);
        ChatItemView messageView = view.findViewById(R.id.message);

        if (item.role.equals("user")) {
            rootView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            messageView.setItemBackgroundColorResource(R.color.conversation_item_user_bg);
        } else {
            rootView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            messageView.setItemBackgroundColorResource(R.color.conversation_item_bot_bg);
        }
        messageView.setMarkdownText(item.content);
    }
}
