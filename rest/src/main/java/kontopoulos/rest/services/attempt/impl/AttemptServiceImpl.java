package kontopoulos.rest.services.attempt.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import kontopoulos.rest.services.attempt.AttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MINUTES;

@Service
@Slf4j
public class AttemptServiceImpl implements AttemptService {
    public static final int MAXIMUM_USER_SIZE = 200;
    public static final int ATTEMPT_INIT = 0;
    private static final int MAX_ATTEMPTS = 5;
    private static final int ATTEMPT_INCREMENT = 1;
    private static final long EXPIRATION_TIME_IN_MINUTES = 10;
    private final LoadingCache<String, Integer> attemptCache;

    public AttemptServiceImpl() {
        super();
        this.attemptCache = CacheBuilder.newBuilder()
                .expireAfterWrite(EXPIRATION_TIME_IN_MINUTES, MINUTES)
                .maximumSize(MAXIMUM_USER_SIZE)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return ATTEMPT_INIT;
                    }
                });
    }

    public void addAppUserToAttemptCache(String username) {
        log.debug("Begin addAppUserToAttemptCache");
        try {
            attemptCache.put(username, attemptCache.get(username) + ATTEMPT_INCREMENT);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        log.debug("Username: " + username + " inserted to cache");
    }

    public void evictAppUserFromAttemptCache(String username) {
        attemptCache.invalidate(username);
        log.debug("Username: " + username + " evicted from cache");
    }

    public boolean hasExceededMaxAttempts(String username) {
        boolean hasExceededMaxAttempts = false;
        try {
            hasExceededMaxAttempts = attemptCache.get(username) >= MAX_ATTEMPTS;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return hasExceededMaxAttempts;
    }
}