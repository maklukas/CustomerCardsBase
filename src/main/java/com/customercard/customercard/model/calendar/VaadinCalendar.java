package com.customercard.customercard.model.calendar;

import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Getter
@Setter
public final class VaadinCalendar {
    private Task monday;
    private Task tuesday;
    private Task wednesday;
    private Task thursday;
    private Task friday;
    private Task saturday;
    private Task sunday;

    public static LocalDate getFirstOfTheMonth(LocalDate date) {
        return date.minusDays(date.getDayOfMonth() - 1);
    }

    public static DayOfWeek getFirstDayOfTheMonth(LocalDate date) {
        return getFirstOfTheMonth(date).getDayOfWeek();
    }

    public static LocalDate getMondayTheWeek(LocalDate date) {
        if (date.getDayOfWeek().getValue() != 1) {
            date = date.minusDays(date.getDayOfWeek().getValue() - 1);
        }

        return date;
    }


}
