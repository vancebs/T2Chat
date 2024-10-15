package com.t2m.t2chat.fragment;

import com.t2m.t2chat.agent.Agent;
import com.t2m.t2chat.agent.CookbookLlama3BAgent;

public class CookbookBotLlama3BChatFragment extends ChatFragment {
    private Agent mAgent = null;

    @Override
    protected Agent getAgent() {
        if (mAgent == null) {
            mAgent = new CookbookLlama3BAgent();
        }
        return mAgent;
    }

}