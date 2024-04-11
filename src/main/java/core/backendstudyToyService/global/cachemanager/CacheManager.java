package core.backendstudyToyService.global.cachemanager;

public interface CacheManager {
    void getCache(String key);
    void put(String key, Object value);
    void get(String key);
    void evict(String key);
}