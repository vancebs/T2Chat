package com.t2m.t2chat.fragment;

import com.t2m.t2chat.agent.Agent;
import com.t2m.t2chat.agent.CookbookQwen0_5BAgent;

public class CookbookBotQwen0_5BChatFragment extends ChatFragment {
    private Agent mAgent = null;

    @Override
    protected Agent getAgent() {
        if (mAgent == null) {
            mAgent = new CookbookQwen0_5BAgent();
        }
        return mAgent;
    }
}
