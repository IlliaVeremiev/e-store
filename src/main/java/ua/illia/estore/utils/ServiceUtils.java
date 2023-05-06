package ua.illia.estore.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.illia.estore.configuration.exceptions.NotFoundException;

import java.util.function.Supplier;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceUtils {
    public static Supplier<RuntimeException> notFound(String entityName, String fieldKey, Object fieldValue) {
        return () -> new NotFoundException(entityName + " with " + fieldKey + ": " + fieldValue + " not found", entityName.toLowerCase() + "." + fieldKey);
    }

    public static <E> E makeSeveralAttempts(Supplier<E> function, int attempts, long waitMilliseconds){
        for (int i = 0; i < attempts; i++) {
            try{
                return function.get();
            }catch (Exception e){
                log.error("Can't perform action with attempt " + (i + 1), e);
                if (i + 1 < attempts){
                    log.debug("Next attempt in " + waitMilliseconds + " milliseconds");
                    try {
                        Thread.sleep(waitMilliseconds);
                    } catch (InterruptedException ex) {
                        log.error("Process was interrupted");
                        throw new IllegalStateException(ex);
                    }
                }
            }
        }
        throw new IllegalStateException("Can't perform action in " + attempts + " attempts");
    }
}
