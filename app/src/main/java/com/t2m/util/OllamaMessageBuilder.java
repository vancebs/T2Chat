package com.t2m.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OllamaMessageBuilder {
    public static final String TAG = OllamaMessageBuilder.class.getSimpleName();

    private JSONObject mMessage;

    public OllamaMessageBuilder() {
        mMessage = new JSONObject();
    }

    public OllamaMessageBuilder setModel(String model) {
        try {
            mMessage.put("model", model);
        } catch (JSONException e) {
            Log.e(TAG, "setModel()# failed.", e);
        }
        return this;
    }

    public OllamaMessageBuilder setStream(boolean isStream) {
        try {
            mMessage.put("stream", isStream);
        } catch (JSONException e) {
            Log.e(TAG, "setStream()# failed.", e);
        }
        return this;
    }

    public OllamaMessageBuilder addChatItem(String role, String content) {
        try {
            // create item
            JSONObject item = new JSONObject();
            item.put("role", role);
            item.put("content", content);

            // append item to messages
            Object messages = mMessage.opt("messages");
            if (messages == null) {
                mMessage.put("messages", item);
            } else if (messages instanceof JSONObject) {
                JSONArray array = new JSONArray();
                array.put(messages);
                array.put(item);
                mMessage.put("messages", array);
            } else if (messages instanceof JSONArray) {
                JSONArray array = (JSONArray) messages;
                array.put(item);
            } else {
                Log.e(TAG, "addChatItem()# invalid type of messages.");
            }
        } catch (JSONException e) {
            Log.e(TAG, "addChatItem()# failed.", e);
        }
        return this;
    }

    public JSONObject buildJson() {
        return mMessage;
    }

    public String buildString() {
        return mMessage.toString();
    }
}
