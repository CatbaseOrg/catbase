package dev.siea.catbase.components;

import java.util.*;
import java.util.concurrent.*;

public class Cache<T> {
    private final int maxCacheSize;
    private final long retentionPeriodMillis;
    private final Map<String, CacheItem<T>> cache = new LinkedHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public Cache(int maxCacheSize, long retentionPeriodMillis) {
        this.maxCacheSize = maxCacheSize;
        this.retentionPeriodMillis = retentionPeriodMillis;

        // Schedule periodic cache retention cleanup
        scheduleCacheCleanup();
    }

    // Add an item to the cache with a key
    public void put(String key, T value) {
        if (cache.size() >= maxCacheSize) {
            evictOldest();  // Remove the oldest cache entry if max size is reached
        }
        cache.put(key, new CacheItem<>(value, System.currentTimeMillis()));
    }

    // Retrieve an item from the cache
    public T get(String key) {
        CacheItem<T> cacheItem = cache.get(key);
        if (cacheItem != null && isValid(cacheItem)) {
            cacheItem.setLastAccessTime(System.currentTimeMillis());  // Update access time
            return cacheItem.getValue();
        }
        return null;
    }

    // Check if the cache item is still valid based on retention period
    private boolean isValid(CacheItem<T> cacheItem) {
        long currentTime = System.currentTimeMillis();
        return (currentTime - cacheItem.getLastAccessTime()) < retentionPeriodMillis;
    }

    // Periodically clean up the cache by removing expired or stale items
    private void scheduleCacheCleanup() {
        long initialDelay = 0;
        long period = 10;  // Cleanup every 10 minutes (adjustable)
        scheduler.scheduleAtFixedRate(this::retainCache, initialDelay, period, TimeUnit.MINUTES);
    }

    // Retain the cache by removing expired items
    private void retainCache() {
        long currentTime = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> (currentTime - entry.getValue().getLastAccessTime()) > retentionPeriodMillis);
        System.out.println("Cache retention executed. Remaining items: " + cache.size());
    }

    // Evict the oldest item in the cache
    private void evictOldest() {
        String oldestKey = cache.entrySet().iterator().next().getKey();
        cache.remove(oldestKey);
    }

    // Shutdown the scheduler when not needed anymore
    public void shutdown() {
        scheduler.shutdown();
    }

    // CacheItem class that holds the value and last access time
    private static class CacheItem<T> {
        private final T value;
        private long lastAccessTime;

        public CacheItem(T value, long lastAccessTime) {
            this.value = value;
            this.lastAccessTime = lastAccessTime;
        }

        public T getValue() {
            return value;
        }

        public long getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(long lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }
    }
}
