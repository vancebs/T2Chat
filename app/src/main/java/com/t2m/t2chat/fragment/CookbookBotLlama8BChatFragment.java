package com.t2m.t2chat.fragment;

import com.t2m.t2chat.agent.Agent;
import com.t2m.t2chat.agent.CookbookLlama8BAgent;

public class CookbookBotLlama8BChatFragment extends ChatFragment {
    private Agent mAgent = null;

    @Override
    protected Agent getAgent() {
        if (mAgent == null) {
            mAgent = new CookbookLlama8BAgent();
        }
        return mAgent;
    }

}