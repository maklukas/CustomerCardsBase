package com.customercard.customercard.view;

import com.customercard.customercard.mapper.ColorMapper;
import com.customercard.customercard.model.Color;
import com.customercard.customercard.model.dto.ColorDto;
import com.customercard.customercard.service.ColorService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "/colors", layout = MainLayout.class)
@PageTitle("Colors | Lashes")
public class ColorsView extends VerticalLayout {
    private final ColorService colorService;
    private final ColorMapper colorMapper;
    private String findValue;
    private Grid<ColorDto> theGrid;

    public ColorsView(ColorService colorService, ColorMapper colorMapper) {
        this.colorService = colorService;
        this.colorMapper = colorMapper;
        add(getFindTextFieldComponent(), plusIconComponent());
        add(getColorsGrid());
    }

    private TextField getFindTextFieldComponent() {
        TextField textField = new TextField();
        textField.setPlaceholder("Search");
        Icon icon = VaadinIcon.SEARCH.create();
        textField.setPrefixComponent(icon);

        textField.addValueChangeListener(it -> {
            findValue = it.getValue();
            if (findValue.equals("")) {
                findValue = null;
            }
            getColorsGrid();
        });

        textField.setValueChangeMode(ValueChangeMode.EAGER);

        return textField;
    }

    public Icon plusIconComponent() {
        Icon icon = new Icon(VaadinIcon.PLUS_CIRCLE);
        String mouseOutColor = "#0006c7";
        icon.setColor(mouseOutColor);
        icon.addClickListener(it -> popupCreate());

        icon.getElement().addEventListener("mouseover", it -> icon.setColor("#0007f2"));
        icon.getElement().addEventListener("mouseout", it -> icon.setColor(mouseOutColor));

        return icon;
    }

    private Grid<ColorDto> getColorsGrid() {
        Grid<ColorDto> grid = new Grid<>(ColorDto.class);
        grid.setItems(colorMapper.mapModelListToDtoList(colorService.getColors(null, findValue)));

        grid.removeColumnByKey("id");
        grid.setColumns("name");
        grid.addComponentColumn(it -> getRemoveIcon(it.getId()));

        if (theGrid != null) {
            int oldId = indexOf(theGrid);
            remove(theGrid);
            addComponentAtIndex(oldId, grid);
        }

        theGrid = grid;
        return grid;
    }

    public Icon getRemoveIcon(String id) {
        Icon removeIcon = new Icon(VaadinIcon.MINUS_CIRCLE);
        String mouseOutColor = "#c70000";
        removeIcon.setColor(mouseOutColor);
        removeIcon.addClickListener(it -> removePopupCreate(id));

        removeIcon.getElement().getStyle().set("vertical-align", "right");
        removeIcon.getElement().addEventListener("mouseover", it -> removeIcon.setColor("#ff0303"));
        removeIcon.getElement().addEventListener("mouseout", it -> removeIcon.setColor(mouseOutColor));

        return removeIcon;
    }

    public void removePopupCreate(String id) {
        Dialog dialog = new Dialog();
        dialog.open();
        Text description = new Text("Are you sure you want to delete the color?");
        dialog.add(new Div(description));

        Notification deletedNote = new Notification("Deleted", 3000);

        Button confirmButton = new Button("Confirm", event -> {
            colorService.deleteColor(id);
            deletedNote.open();
            dialog.close();
            getColorsGrid();
        });

        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);
        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(new Div(confirmButton, cancelButton));
    }

    public void popupCreate() {
        Dialog dialog = new Dialog();
        dialog.open();

        Text description = new Text("Enter the name of the color you want to create.");
        dialog.add(new Div(description));

        Notification createdNote = new Notification("Created", 3000);

        TextField textField = new TextField();
        textField.setPlaceholder("Name");
        dialog.add(new Div(textField));

        Button confirmButton = new Button("Confirm", event -> {
            if (!textField.getValue().equals("")) {
                colorService.createColor(new Color(textField.getValue()));
                createdNote.open();
                dialog.close();
                getColorsGrid();
            }
        });

        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);
        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(new Div(confirmButton, cancelButton));
    }

}