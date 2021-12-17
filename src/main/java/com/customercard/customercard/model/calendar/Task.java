package com.customercard.customercard.model.calendar;

import com.customercard.customercard.model.dto.CustomerWork;
import com.customercard.customercard.service.CustomerService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Getter
@Setter
public class Task extends Div {

    private LocalDate date;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());

    private final CustomerService service;

    public Task(CustomerService service, LocalDate date) {
        this.service = service;
        this.date = date;
        add(new Div(getTheLabel()), new Div(getTheGrid()));
    }

    private Grid<CustomerWork> getTheGrid() {
        Grid<CustomerWork> grid = new Grid<>(CustomerWork.class, false);
        grid.addColumn(it -> it.getName() + " " + it.getSurname())
                .setHeader("Full name")
                .setComparator(it -> it.getName() + " " + it.getSurname());

        grid.addColumn(it ->
                        formatter.format(it.getDate())
                )
                .setHeader("Time")
                .setComparator(CustomerWork::getDate);

        grid.setItems(service.getWorksAtDate(date));
        setGridStyles(grid);

        return grid;
    }

    private Label getTheLabel() {
        return new Label(String.valueOf(date.getDayOfMonth()));
    }

    private static void setGridStyles(Grid<CustomerWork> grid) {
        grid.getStyle()
                .set("width", "220px")
                .set("height", "200px")
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
