package com.afcas.utils;

import java.util.HashMap;
import java.util.Map;

public class ObjectCache {
    private Map<String, Object> cache;

    public ObjectCache() {
        cache = new HashMap<>();
    }

    public void addToCache(String key, Object value) {
        cache.put(key, value);
    }

    public Object getFromCache(String key) {
        return cache.get(key);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public void removeFromCache(String key) {
        cache.remove(key);
    }

    public void clearCache() {
        cache.clear();
    }

    public int getSize() {
        return cache.size();
    }
}