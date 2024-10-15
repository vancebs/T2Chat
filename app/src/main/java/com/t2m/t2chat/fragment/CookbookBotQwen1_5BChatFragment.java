package com.t2m.t2chat.fragment;

import com.t2m.t2chat.agent.Agent;
import com.t2m.t2chat.agent.CookbookQwen1_5BAgent;

public class CookbookBotQwen1_5BChatFragment extends ChatFragment {
    private Agent mAgent = null;

    @Override
    protected Agent getAgent() {
        if (mAgent == null) {
            mAgent = new CookbookQwen1_5BAgent();
        }
        return mAgent;
    }
}
