package kontopoulos.rest.services.common;

import java.time.LocalTime;
import java.util.Set;

public interface TimeIntervalService {
    Set<LocalTime> getTimeSlots();
}
