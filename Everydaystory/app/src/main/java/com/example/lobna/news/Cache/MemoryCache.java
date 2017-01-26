package com.example.lobna.news.Cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by ahmedb on 10/28/16.
 */

public class MemoryCache {


    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

    // Use 1/16th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;

    private LruCache<String, Bitmap> mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            // The cache size will be measured in kilobytes rather than
            // number of items.
            return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
        }
    };

    public Bitmap get(String id){

        return mMemoryCache.get(id);
    }

    public void put(String id, Bitmap bitmap){

            mMemoryCache.put(id , bitmap);
    }

    public void clearCache(){

        mMemoryCache.evictAll();
    }

}
