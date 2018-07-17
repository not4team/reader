package com.book.ireader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.book.ireader.App;
import com.book.ireader.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by newbiechen on 17-4-16.
 */

public class SharedPreUtils {
    private static final String SHARED_NAME = "IReader_pref";
    private static SharedPreUtils sInstance;
    private SharedPreferences sharedReadable;
    private SharedPreferences.Editor sharedWritable;
    public Map<String, String> bookSourcesMap;
    public Map<String, String> bookSourceSearchMap;

    private SharedPreUtils() {
        sharedReadable = App.getContext()
                .getSharedPreferences(SHARED_NAME, Context.MODE_MULTI_PROCESS);
        sharedWritable = sharedReadable.edit();
        bookSourcesMap = new HashMap<>();
        String[] bookSources = App.getContext().getResources().getStringArray(R.array.book_source);
        for (String source : bookSources) {
            String[] item = source.split("->");
            bookSourcesMap.put(item[0], item[1]);
        }
        bookSourceSearchMap = new HashMap<>();
        String[] bookSourceSearch = App.getContext().getResources().getStringArray(R.array.book_source_search);
        for (String source : bookSourceSearch) {
            String[] item = source.split("->");
            bookSourceSearchMap.put(item[0], item[1]);
        }
    }

    public static SharedPreUtils getInstance() {
        if (sInstance == null) {
            synchronized (SharedPreUtils.class) {
                if (sInstance == null) {
                    sInstance = new SharedPreUtils();
                }
            }
        }
        return sInstance;
    }

    public String getString(String key) {
        return sharedReadable.getString(key, "");
    }

    public void putString(String key, String value) {
        sharedWritable.putString(key, value);
        sharedWritable.commit();
    }

    public void putInt(String key, int value) {
        sharedWritable.putInt(key, value);
        sharedWritable.commit();
    }

    public void putBoolean(String key, boolean value) {
        sharedWritable.putBoolean(key, value);
        sharedWritable.commit();
    }

    public int getInt(String key, int def) {
        return sharedReadable.getInt(key, def);
    }

    public boolean getBoolean(String key, boolean def) {
        return sharedReadable.getBoolean(key, def);
    }
}
