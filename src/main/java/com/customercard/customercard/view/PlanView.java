package com.customercard.customercard.view;

import com.customercard.customercard.model.dto.CustomerWork;
import com.customercard.customercard.service.CalendarService;
import com.customercard.customercard.service.CustomerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


@Route(value = "/plan", layout = MainLayout.class)
@PageTitle("Plan | Lashes")
public class PlanView extends VerticalLayout {

    private final CalendarService service;
    private final CustomerService customerService;
    private final List<HorizontalLayout> horizontals;
    private final List<VerticalLayout> calendarFields;
    private List<CustomerWork> works;
    private LocalDate theDate;
    private final Span monthName;

    public PlanView(CalendarService service, CustomerService customerService) {
        this.service = service;
        this.customerService = customerService;
        theDate = service.getTheFirstDateOfTheMonth(LocalDate.now());
        works = customerService.getWorksInCalendarMonth(theDate);
        horizontals = new ArrayList<>();
        calendarFields = new ArrayList<>();
        this.monthName = new Span();
        initComponents();
        setHorizontalsStyles();
    }

    private void initComponents() {
        initMonthNav();
        createGrids();
        initHeaderFields();
        initCalendarFields();
        splitWorks();
    }

    private void initMonthNav() {
        setMonthNameContent();
        Div monthNavigatorDiv = new Div(getUpArrowButton(), getDownArrowButton());
        HorizontalLayout naviLayout = new HorizontalLayout(monthName, monthNavigatorDiv);
        ComponentStyle.setDivAlignToRightStyle(monthNavigatorDiv);
        naviLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        naviLayout.setWidthFull();
        add(naviLayout);
    }

    private void createGrids() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

        List<HorizontalLayout> horizontalLayouts = new ArrayList<>();
        for (int i = 0; i < 7 ; i++) {
            horizontalLayouts.add(new HorizontalLayout());
        }

        horizontals.addAll(horizontalLayouts);

        for (HorizontalLayout hl: horizontalLayouts) {
            for (int i = 0; i < 7 ; i++) {
                VerticalLayout vl = new VerticalLayout();
                calendarFields.add(vl);
                hl.add(vl);
            }
            mainLayout.add(hl);
        }
        add(mainLayout);
    }

    private void addTheLayoutContent(int id, String text) {
        Span span = new Span(text);
        ComponentStyle.setCalendarFieldTextStyle(span);
        calendarFields.get(id).add(span);
    }

    private void removeTheLayoutContent(int id) {
        calendarFields.get(id).removeAll();
    }

    private Button getUpArrowButton() {

        Button upArrowButton = new Button();
        ComponentStyle.setUpArrowButtonStyle(upArrowButton);

        upArrowButton.addClickListener(it -> {
            theDate = theDate.minusMonths(1);
            works = customerService.getWorksInCalendarMonth(theDate);
            setMonthNameContent();
            initCalendarFields();
            splitWorks();
        });
        return upArrowButton;
    }

    private Button getDownArrowButton() {

        Button downArrowButton = new Button();
        ComponentStyle.setDownArrowButtonStyle(downArrowButton);

        downArrowButton.addClickListener(it -> {
            theDate = theDate.plusMonths(1);
            works = customerService.getWorksInCalendarMonth(theDate);
            setMonthNameContent();
            initCalendarFields();
            splitWorks();
        });
        return downArrowButton;
    }

    private void initHeaderFields() {
        String text;
        for (int i = 0; i < 7; i++) {
            Span day = new Span();
            text = DayOfWeek.of(i+1).getDisplayName(TextStyle.SHORT, Locale.getDefault());

            day.setText(text);
            calendarFields.get(i).add(day);
            ComponentStyle.setCalendarHeaderStyle(calendarFields.get(i));
            ComponentStyle.setCalendarHeaderTextStyle(day);
        }
    }

    private void initCalendarFields() {
        LocalDate theDateOfFirstDayAtTheCalendar = service.getTheDateOfFirstDayAtTheCalendar(theDate);

        for (int i = 7; i < 49; i++) {
            ComponentStyle.setCalendarFieldsStyle(calendarFields.get(i));
            removeTheLayoutContent(i);
            addTheLayoutContent(i, String.valueOf(theDateOfFirstDayAtTheCalendar.getDayOfMonth()));
            addTheLayoutOnClickEvent(i, theDateOfFirstDayAtTheCalendar);

            if (theDateOfFirstDayAtTheCalendar.equals(LocalDate.now())) {
                ComponentStyle.setCalendarFieldTodayStyle(calendarFields.get(i));
            } else {
                ComponentStyle.removeCalendarFieldTodayStyle(calendarFields.get(i));
            }
            theDateOfFirstDayAtTheCalendar = theDateOfFirstDayAtTheCalendar.plusDays(1);
        }
    }

    private void setHorizontalsStyles() {
        for (HorizontalLayout hl: horizontals) {
            ComponentStyle.setCalendarHorizontals(hl);
        }
    }

    private void setMonthNameContent() {
        monthName.setText(theDate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.getDefault()) + " " + theDate.getYear());
    }

    private void splitWorks() {

        works.stream()
                .filter(w -> w.getDate().isPresent())
                .forEach(w -> addTheLayoutContent(
                        service.computeFieldNumber(
                                theDate,
                                w.getDate().orElse(null).toLocalDate()),
                        w.toString()));
    }

    private void addTheLayoutOnClickEvent(int id, LocalDate date) {
        calendarFields.get(id).addClickListener(it ->
            openPopup(date)
        );
    }

    private void openPopup(LocalDate date) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withZone(ZoneId.systemDefault());

        Dialog dialog = new Dialog();
        dialog.open();
        dialog.setWidthFull();
        dialog.add(new Span(dateFormatter.format(date)));
        dialog.add(getWorks(date));

        Button closeButton = new Button("Close", event ->
                dialog.close()
        );
        dialog.add(closeButton);
    }

    private Grid<CustomerWork> getWorks(LocalDate date) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());

        Grid<CustomerWork> works = new Grid<>(CustomerWork.class);
        works.setItems(customerService.getWorksInTheDay(date));
        works.removeColumnByKey("id");
        works.removeColumnByKey("date");

        works.setColumns("name", "surname");
        works.addColumn(it ->
                dateTimeFormatter.format(Objects.requireNonNull(it.getDate().orElse(null)))
                )
                .setComparator(it -> it.getDate().orElse(null))
                .setHeader("Date");

        return works;

    }

}
