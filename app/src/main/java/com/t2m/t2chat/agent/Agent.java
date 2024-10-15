package com.t2m.t2chat.agent;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Future;

public abstract class Agent {
    public Agent() {
    }

    public abstract Conversation newConversation();

    public static abstract class Conversation {
        private OnUpdateListener mOnUpdateListener = null;

        private static final long UPDATE_INTERVAL_MS = 50;
        private long mLastUpdatedTimeMs = 0;

        public void setOnUpdateListener(OnUpdateListener listener) {
            mOnUpdateListener = listener;
        }

        public void notifyUpdated(int index, Item item, boolean done) {
            long currentTimeMs = System.currentTimeMillis();
            if (done || (mLastUpdatedTimeMs == 0) || ((currentTimeMs - mLastUpdatedTimeMs) > UPDATE_INTERVAL_MS)) {
                mLastUpdatedTimeMs = currentTimeMs;
                if (mOnUpdateListener != null) {
                    mOnUpdateListener.onUpdate(index, item);
                }
            }
            if (done) {
                mLastUpdatedTimeMs = 0;
            }
        }

        public abstract Future<String> talk(String prompt);

        public abstract int size();
        public abstract Item getItem(int position);

        public interface OnUpdateListener {
            void onUpdate(int index, Item item);
        }

        public static class Item {
            private static final SimpleDateFormat sFormat
                    = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.CHINESE);

            public String role;
            public String content;
            public Date timestamp;
            public String timestampString() {
                return sFormat.format(timestamp);
            }

            public Item(String role, String content, Date timestamp) {
                this.role = role;
                this.content = content;
                this.timestamp = timestamp;
            }

            public Item(String role, String content) {
                this(role, content, new Date());
            }

            @NonNull
            @Override
            public String toString() {
                return "[" + timestampString() + "]#" + role + "# " + content;
            }
        }
    }
}
