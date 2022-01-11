package com.customercard.customercard.view;

import com.customercard.customercard.service.CalendarService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Route(value = "/plan", layout = MainLayout.class)
@PageTitle("Plan | Lashes")
public class PlanView extends VerticalLayout {

    private final CalendarService service;
    private final List<HorizontalLayout> horizontals;
    private final List<VerticalLayout> calendarFields;
    private LocalDate theDate;
    private Span monthName;

    public PlanView(CalendarService service) {
        this.service = service;
        theDate = service.getTheFirstDateOfTheMonth(LocalDate.now());
        horizontals = new ArrayList<>();
        calendarFields = new ArrayList<>();
        this.monthName = new Span();
        initMonthNav();
        createGrids();
        initHeaderFields();
        initCalendarFields();
        setHorizontalsStyles();
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
            setMonthNameContent();
            initCalendarFields();
        });
        return upArrowButton;
    }

    private Button getDownArrowButton() {

        Button downArrowButton = new Button();
        ComponentStyle.setDownArrowButtonStyle(downArrowButton);

        downArrowButton.addClickListener(it -> {
            theDate = theDate.plusMonths(1);
            setMonthNameContent();
            initCalendarFields();
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
        for (int i = 7; i < 49; i++) {
            ComponentStyle.setCalendarFieldsStyle(calendarFields.get(i));
            fillTheCalendarFields();
        }
    }

    private void fillTheCalendarFields() {
        LocalDate theDateOfFirstDayAtTheCalendar = service.getTheDateOfFirstDayAtTheCalendar(theDate);

        for (int i = 7; i < 49; i++) {
            removeTheLayoutContent(i);
            addTheLayoutContent(i, String.valueOf(theDateOfFirstDayAtTheCalendar.getDayOfMonth()));
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
}
