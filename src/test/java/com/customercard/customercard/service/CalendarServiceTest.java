package com.customercard.customercard.service;

import com.customercard.customercard.model.Customer;
import com.customercard.customercard.repository.CustomerRepo;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CalendarServiceTest {

    private final CalendarService calendarService;
    private final CustomerRepo repo;

    @Autowired
    public CalendarServiceTest(CalendarService calendarService, CustomerRepo repo) {
        this.calendarService = calendarService;
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
        LocalDate theFirstDayAtTheCalendar = calendarService.getTheDateOfFirstDayAtTheCalendar(LocalDate.now());
        LocalDate theLastDayAtTheCalendar = theFirstDayAtTheCalendar.plusDays(42);

        List<Customer> allBetweenDates = repo.findAllBetweenDates(theFirstDayAtTheCalendar.atStartOfDay(), theLastDayAtTheCalendar.atStartOfDay());
        allBetweenDates.stream()
                .map(m -> m.getSurname() + m.getLashesList().get(0).getNextDate())
                .forEach(System.out::println);
    }
}