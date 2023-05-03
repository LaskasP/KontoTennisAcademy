package kontopoulos.rest.services.common.impl;

import kontopoulos.rest.models.common.entity.TimeIntervalEntity;
import kontopoulos.rest.repos.TimeIntervalsRepository;
import kontopoulos.rest.services.common.TimeIntervalService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kontopoulos.rest.models.common.TimeIntervalConstant.TIME_VALUE;

@Service
public class TimeIntervalServiceImpl implements TimeIntervalService {
    private final TimeIntervalsRepository timeIntervalsRepository;

    public TimeIntervalServiceImpl(TimeIntervalsRepository timeIntervalsRepository) {
        this.timeIntervalsRepository = timeIntervalsRepository;
    }

    @Override
    @Cacheable(TIME_VALUE)
    public Set<LocalTime> getTimeSlots() {
        List<TimeIntervalEntity> timeIntervalEntityList = timeIntervalsRepository.findAll();
        Set<LocalTime> timeSlotList = new HashSet<>();
        for (TimeIntervalEntity timeIntervalEntity : timeIntervalEntityList) {
            timeSlotList.add(timeIntervalEntity.getTimeSlot());
        }
        return timeSlotList;
    }
}
