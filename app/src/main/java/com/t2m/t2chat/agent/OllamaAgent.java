package com.t2m.t2chat.agent;

import android.util.Log;

import com.t2m.util.Http;
import com.t2m.util.OllamaMessageBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class OllamaAgent extends Agent {
    private final String mApiUrl;
    private final String mModel;
    private final String mSystemPrompt;

    public OllamaAgent(String apiUrl, String model, String systemPrompt) {
        super();

        mApiUrl = apiUrl;
        mModel = model;
        mSystemPrompt = systemPrompt;
    }

    public Conversation newConversation() {
        return new OllamaConversation(mApiUrl, mModel, mSystemPrompt);
    }

    public static class OllamaConversation extends Conversation {
        private static final String TAG = OllamaAgent.class.getSimpleName() + "." + OllamaConversation.class.getSimpleName();

        private final String mApiUrl;
        private final String mModel;
        private final String mSystemPrompt;

        private final Http mHttp = new Http();
        private final ArrayList<Item> mConversationItems = new ArrayList<>();

        public OllamaConversation(String apiUrl, String model, String systemPrompt) {
            mApiUrl = apiUrl;
            mModel = model;
            mSystemPrompt = systemPrompt;
        }

        public Future<String> talk(String prompt) {
            // insert user prompt
            Item userItem = new Item("user", prompt);
            mConversationItems.add(userItem);
            notifyUpdated(latestItemIndex(), userItem, true);

            // talk to server
            final Future<Boolean> future = mHttp.postAsync(
                    mApiUrl,
                    getOllamaRequestMessage(),
                    (line) -> {
                        try {
                            // parse done
                            JSONObject obj = new JSONObject(line);
                            boolean done = obj.optBoolean("done", false);

                            // parse message
                            JSONObject msg = obj.optJSONObject("message");
                            if (msg == null) {
                                return; // something wrong, nothing to update
                            }
                            String role = msg.optString("role");
                            String content = msg.optString("content");

                            // create or update item
                            Item item = latestItem();
                            if (item.role.equals(role)) {
                                item.content += content;
                            } else {
                                item = new Item(role, content);
                                mConversationItems.add(item);
                            }
                            notifyUpdated(latestItemIndex(), item, false);

                            // if done
                            if (done) {
                                notifyUpdated(latestItemIndex(), item, true);;
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "parse json line failed", e);
                        }
                    }
            );

            // wait for finish
            boolean[] result = new boolean[]{false};
            final FutureTask<String> futureTask
                    = new FutureTask<>(() -> result[0] ? latestItem().content : null);
            Thread waitForResult = new Thread(() -> {
                try {
                    result[0] = future.get();
                } catch (ExecutionException e) {
                    Log.e(TAG, "wait for result failed.", e);
                    result[0] = false;
                } catch (InterruptedException e) {
                    Log.e(TAG, "wait for result failed.", e);
                    result[0] = false;
                }

                // connection failed
                if (!result[0]) {
                    Item item = new Item("assistant", "Connection timeout! Please check your network!!");
                    mConversationItems.add(item);
                    notifyUpdated(latestItemIndex(), item, true);
                }

                futureTask.run();
            });
            waitForResult.start();

            return futureTask;
        }

        @Override
        public int size() {
            return mConversationItems.size();
        }

        @Override
        public Item getItem(int position) {
            return mConversationItems.get(position);
        }

        private int latestItemIndex() {
            return mConversationItems.size() - 1;
        }

        private Item latestItem() {
            return mConversationItems.get(latestItemIndex());
        }

        private String getOllamaRequestMessage() {
            OllamaMessageBuilder builder = new OllamaMessageBuilder();
            builder.setModel(mModel)
                    .setStream(true);
            if (mSystemPrompt != null) {
                builder.addChatItem("system", mSystemPrompt);
            }

            for (Item item : mConversationItems) {
                builder.addChatItem(item.role, item.content);
            }

            return builder.buildString();
        }
    }
}
