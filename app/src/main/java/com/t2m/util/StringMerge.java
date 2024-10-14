package com.t2m.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StringMerge {
    private final Object mLock = new Object();
    private final ArrayList<String> mStringList = new ArrayList<>();

    public StringMerge append(String str) {
        synchronized (mLock) {
            mStringList.add(str);
        }
        return this;
    }

    public String getMerged() {
        synchronized (mLock) {
            String merged = String.join("", mStringList);
            mStringList.clear();
            mStringList.add(merged);
            return merged;
        }
    }
}
