package com.customercard.customercard.service;

import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.model.dto.CustomerWork;
import com.customercard.customercard.repository.CustomerRepo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalendarServiceTest {

    private final CalendarService calendarService;
    private final CustomerService customerService;
    private final CustomerRepo repo;

    @Autowired
    public CalendarServiceTest(CalendarService calendarService, CustomerService customerService, CustomerRepo repo) {
        this.calendarService = calendarService;
        this.customerService = customerService;
        this.repo = repo;
    }



    @Test
    void shouldGetTheFirstDayOfCurrentMonth() {
        assertEquals(6,
                calendarService.getTheFirstDayNumber(LocalDate.now()));
        assertEquals(3,
                calendarService.getTheFirstDayNumber(LocalDate.of(2021,12,25))
                );
        assertNotEquals(3,
                calendarService.getTheFirstDayNumber(LocalDate.of(2021,11,25)));
    }

    @Test
    void shouldGetTheDateOfFirstDayAtTheCalendar() {
        assertEquals(LocalDate.of(2021,12,27),
                calendarService.getTheDateOfFirstDayAtTheCalendar(LocalDate.now())
                );
        assertEquals(LocalDate.of(2021,11,29),
                calendarService.getTheDateOfFirstDayAtTheCalendar(LocalDate.of(2021,12,25))
                );
        assertNotEquals(LocalDate.of(2021,9,28),
                calendarService.getTheDateOfFirstDayAtTheCalendar(LocalDate.of(2021,10,25))
                );
    }

    @Test
    void shouldGetWorksForDate() {
        customerService.getWorksInCalendarMonth(LocalDate.now()).stream()
                .map(c -> c.getDate() + " " + c.getName() + " " + c.getSurname())
                .forEach(System.out::println);
    }

    @Test
    void shouldComputeFieldNo() {

        assertEquals(24, calendarService.computeFieldNumber(LocalDate.of(2022, 1, 13), LocalDate.of(2022, 1, 13)));
        assertEquals(8, calendarService.computeFieldNumber(LocalDate.of(2022, 2, 1), LocalDate.of(2022, 2, 1)));
        assertEquals(38, calendarService.computeFieldNumber(LocalDate.of(2022, 1, 27), LocalDate.of(2022, 1, 27)));
        assertEquals(7, calendarService.computeFieldNumber(LocalDate.of(2022, 1, 27), LocalDate.of(2021, 12, 27)));
        assertEquals(35, calendarService.computeFieldNumber(LocalDate.of(2021, 12, 27), LocalDate.of(2021, 12, 27)));

    }

    @Test
    void setCalendarSe() {

        LocalDate date = LocalDate.of(2022,1,27);
        LocalDate first = calendarService.getTheDateOfFirstDayAtTheCalendar(date);

        int between = (int) ChronoUnit.DAYS.between(first, date);

        System.out.println(between);


    }
}