package com.t2m.t2chat.fragment;

import com.t2m.t2chat.agent.Agent;
import com.t2m.t2chat.agent.CookbookAgent;

public class CookbookBotChatFragment extends ChatFragment {
    private Agent mAgent = null;

    @Override
    protected Agent getAgent() {
        if (mAgent == null) {
            mAgent = new CookbookAgent();
        }
        return mAgent;
    }

}