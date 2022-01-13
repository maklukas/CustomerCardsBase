package com.customercard.customercard.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.concurrent.atomic.AtomicInteger;

@CssImport(value = "./styles/myGridStyles.css")
public abstract class ComponentStyle {

    public static Button setRemoveButtonStyle(Button removeButton) {
        removeButton.setIcon(VaadinIcon.TRASH.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        return removeButton;
    }

    public static Button setUpArrowButtonStyle(Button upArrowButton) {
        upArrowButton.setIcon(VaadinIcon.CHEVRON_CIRCLE_UP.create());
        upArrowButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        return upArrowButton;
    }

    public static Button setDownArrowButtonStyle(Button downArrowButton) {
        downArrowButton.setIcon(VaadinIcon.CHEVRON_CIRCLE_DOWN.create());
        downArrowButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        return downArrowButton;
    }

    public static Button setEditButtonStyle(Button editButton) {
        editButton.setIcon(VaadinIcon.EDIT.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        return editButton;
    }

    public static Button setCreateButtonStyle(Button createButton) {
        createButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        createButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return createButton;
    }

    public static Div setDivAlignToRightStyle(Div b) {
        b.getStyle().set("margin-left", "auto");
        b.getStyle().set("padding", "15px");
        return b;
    }

    public static void setCalendarHeaderTextStyle(Span text) {
        text.setClassName("calendar-header-text");
    }

    public static void setCalendarFieldTextStyle(Span text) {
        text.setClassName("calendar-field-text");
    }

    public static void setCalendarHeaderStyle(VerticalLayout layout) {
        layout.addClassNames("calendar-header");
    }

    public static void setCalendarFieldsStyle(VerticalLayout layout) {
        layout.addClassName("calendar-field");
    }

    public static void setCalendarFieldTodayStyle(VerticalLayout layout) {
        layout.addClassName("calendar-field-today");
    }

    public static void removeCalendarFieldTodayStyle(VerticalLayout layout) {
        layout.removeClassName("calendar-field-today");
    }

    public static void setCalendarHorizontals(HorizontalLayout layout) {
        layout.addClassName("calendar-horizontal-fragment");
    }

    public static int getScreenWidth() {
        AtomicInteger screenWidth = new AtomicInteger();
        UI.getCurrent().getPage().retrieveExtendedClientDetails(receiver -> {
            screenWidth.set(receiver.getScreenWidth());
        });
        return screenWidth.get();
    }

}
