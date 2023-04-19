package kontopoulos.rest.models.common.rest;

import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TimeIntervalResponse {
    
    private final List<LocalTime> timeIntervals = new ArrayList<>();

    public void addTimeInterval(LocalTime timeInterval) {
        timeIntervals.add(timeInterval);
    }
}
