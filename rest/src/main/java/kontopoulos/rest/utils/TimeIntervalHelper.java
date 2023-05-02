package kontopoulos.rest.utils;

import kontopoulos.rest.models.common.entity.TimeIntervalEntity;
import kontopoulos.rest.repos.TimeIntervalsRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeIntervalHelper {
    private final TimeIntervalsRepository timeIntervalsRepository;
//    private final List<TimeIntervalEntity> timeSlotsList;

    public TimeIntervalHelper(TimeIntervalsRepository timeIntervalsRepository) {
        this.timeIntervalsRepository = timeIntervalsRepository;
//        this.timeSlotsList = timeIntervalsRepository.findAll();
    }

    public Boolean isTimeWithinPossibleValues(LocalTime localTime) {
        boolean result = false;
        List<LocalTime> timeSlotsList = getTimeSlots();
        if (timeSlotsList.contains(localTime)) result = true;
        return result;
    }

    @Cacheable("timeSlotsList")
    public List<LocalTime> getTimeSlots() {
        List<TimeIntervalEntity> timeIntervalEntityList = timeIntervalsRepository.findAll();
        try {
            Thread.sleep(5000);
        } catch (Exception ignored) {
        }
        List<LocalTime> timeSlotList = new ArrayList<>();
        for (TimeIntervalEntity timeIntervalEntity : timeIntervalEntityList) {
            timeSlotList.add(timeIntervalEntity.getTimeSlot());
        }
        return timeSlotList;
    }
}
