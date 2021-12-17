package com.customercard.customercard.view;

import com.customercard.customercard.model.calendar.VaadinCalendar;
import com.customercard.customercard.service.CalendarService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


@Route(value = "/plan", layout = MainLayout.class)
@PageTitle("Plan | Lashes")
public class PlanView extends VerticalLayout {

    private final CalendarService service;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());

    public PlanView(CalendarService service) {
        this.service = service;
        createGrids();
    }

    private void createGrids() {
        Grid<VaadinCalendar> grid = new Grid<>(VaadinCalendar.class, false);
        grid.addComponentColumn(VaadinCalendar::getMonday).setHeader("Monday");
        grid.addComponentColumn(VaadinCalendar::getTuesday).setHeader("Tuesday");
        grid.addComponentColumn(VaadinCalendar::getWednesday).setHeader("Wednesday");
        grid.addComponentColumn(VaadinCalendar::getThursday).setHeader("Thursday");
        grid.addComponentColumn(VaadinCalendar::getFriday).setHeader("Friday");
        grid.addComponentColumn(VaadinCalendar::getSaturday).setHeader("Saturday");
        grid.addComponentColumn(VaadinCalendar::getSunday).setHeader("Sunday");

        setGridStyles(grid);
        grid.setItems(service.getTheMonth(LocalDate.now()));
        add(grid);
    }


    private static void setGridStyles(Grid<VaadinCalendar> grid) {
        grid.getStyle()
                .set("width", "1500px")
                .set("height", "800px")
                .set("margin-left", "0.5rem")
                .set("margin-top", "0.5rem")
                .set("align-self", "unset")
                .set("font-size", "10px")
                .set("display", "flex");
    }

    private static void setContainerStyles(Div container) {
        container.getStyle().set("display", "flex").set("flex-direction", "row")
                .set("flex-wrap", "wrap");
    }

}
