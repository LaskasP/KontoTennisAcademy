package kontopoulos.rest.services.attempt;

public interface AttemptService {

    void addAppUserToAttemptCache(String username);

    void evictAppUserFromAttemptCache(String username);

    boolean hasExceededMaxAttempts(String username);
}