package com.customercard.customercard.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service("calendarService")
public class CalendarService {

    public LocalDate getTheFirstDateOfTheMonth(LocalDate date) {
        return LocalDate.of(date.getYear(), date.getMonth(), 1);
    }

    public int getTheDayNumber(LocalDate date) {
        return date.getDayOfWeek().getValue();
    }

    public int getTheFirstDayNumber(LocalDate date) {
        return getTheDayNumber(getTheFirstDateOfTheMonth(date));
    }

    public LocalDate getTheDateOfFirstDayAtTheCalendar(LocalDate date) {
        int theFirstDayNumber = getTheFirstDayNumber(date);
        return getTheFirstDateOfTheMonth(date).minusDays(theFirstDayNumber - 1);
    }
}
