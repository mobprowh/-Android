package com.bahisadam;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by atata on 21/12/2016.
 * Cache
 */

public class Cache {

    private static LruCache<String, Bitmap> mMemoryCache;
    private static volatile Cache mInstance;

    private Cache(){

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        final int cacheSize = maxMemory / 4;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
    }

    public static Cache getInstance(){

        if(mInstance==null){
            synchronized (Cache.class){
                mInstance= new Cache();
            }
        }
        return mInstance;
    }

    public static void addBitmap(String key,Bitmap bitmap){
        getInstance();
        mMemoryCache.put(key,bitmap);
    }
    public static Bitmap getBitmap(String key){
        getInstance();
        return mMemoryCache.get(key);
    }


}
