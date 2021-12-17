package com.customercard.customercard.service;

import com.customercard.customercard.model.calendar.VaadinCalendar;
import com.customercard.customercard.model.calendar.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.customercard.customercard.model.calendar.VaadinCalendar.getFirstOfTheMonth;

@Service
public class CalendarService {

    private final CustomerService service;

    @Autowired
    public CalendarService(CustomerService service) {
        this.service = service;
    }

    private VaadinCalendar getTheWeek(LocalDate date) {
        date = VaadinCalendar.getMondayTheWeek(date);

        VaadinCalendar theWeek = new VaadinCalendar();
        theWeek.setMonday(new Task(service, date));
        theWeek.setTuesday(new Task(service, date.plusDays(1)));
        theWeek.setWednesday(new Task(service, date.plusDays(2)));
        theWeek.setThursday(new Task(service, date.plusDays(3)));
        theWeek.setFriday(new Task(service, date.plusDays(4)));
        theWeek.setSaturday(new Task(service, date.plusDays(5)));
        theWeek.setSunday(new Task(service, date.plusDays(6)));

        return theWeek;
    }

    public List<VaadinCalendar> getTheMonth(LocalDate date) {
        List<VaadinCalendar> theMonth = new ArrayList<>();
        LocalDate theFirst = getFirstOfTheMonth(date);
        theMonth.add(getTheWeek(theFirst));
        theMonth.add(getTheWeek(theFirst.plusWeeks(1)));
        theMonth.add(getTheWeek(theFirst.plusWeeks(2)));
        theMonth.add(getTheWeek(theFirst.plusWeeks(3)));
        theMonth.add(getTheWeek(theFirst.plusWeeks(4)));
        theMonth.add(getTheWeek(theFirst.plusWeeks(5)));
        return theMonth;
    }
}
