package com.t2m.t2chat.fragment;

import com.t2m.t2chat.agent.Agent;
import com.t2m.t2chat.agent.Cookbook3BAgent;

public class CookbookBotChat3BFragment extends ChatFragment {
    private Agent mAgent = null;

    @Override
    protected Agent getAgent() {
        if (mAgent == null) {
            mAgent = new Cookbook3BAgent();
        }
        return mAgent;
    }

}