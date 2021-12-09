package com.customercard.customercard.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

public abstract class ComponentStyle {

    public static Button setRemoveButtonStyle(Button removeButton) {
        removeButton.setIcon(VaadinIcon.TRASH.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        return removeButton;
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

    public static Div setDivAliginToRightStyle(Div b) {
        b.getStyle().set("margin-left", "auto");
        b.getStyle().set("padding", "15px");
        return b;
    }
}
