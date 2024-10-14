package com.t2m.t2chat.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.noties.markwon.Markwon;

import com.t2m.t2chat.R;
import com.t2m.t2chat.agent.Agent;
import com.t2m.t2chat.view.TriangleView;


public class ConversationAdapter extends BaseAdapter implements Agent.Conversation.OnUpdateListener {
    private final Agent.Conversation mConversation;
    private final Handler mHandler = new Handler();
    private Markwon mMarkwon = null;

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
        LinearLayout itemView = (LinearLayout) view;
        TriangleView triangleView = view.findViewById(R.id.triangle);
        TextView messageView = view.findViewById(R.id.message);
        if (item.role.equals("user")) {
            itemView.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_RTL);
            triangleView.setColorRes(R.color.conversation_item_user_bg);
            triangleView.setMirror(true);
            messageView.setBackgroundResource(R.color.conversation_item_user_bg);
        } else {
            itemView.setLayoutDirection(LinearLayout.LAYOUT_DIRECTION_LTR);
            triangleView.setColorRes(R.color.conversation_item_bot_bg);
            triangleView.setMirror(false);
            messageView.setBackgroundResource(R.color.conversation_item_bot_bg);
        }

        setMarkdown(messageView, item.content);
    }

    private void setMarkdown(TextView view, String text) {
        if (mMarkwon == null) {
            mMarkwon = Markwon.create(view.getContext().getApplicationContext());
        }
        mMarkwon.setMarkdown(view, text);
    }
}
