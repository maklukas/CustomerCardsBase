package com.customercard.customercard.gui;

import com.customercard.customercard.mapper.ColorMapper;
import com.customercard.customercard.model.Color;
import com.customercard.customercard.model.dto.ColorDto;
import com.customercard.customercard.service.ColorService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("/colors")
public class ColorsGui extends VerticalLayout {

    private final ColorService colorService;
    private final ColorMapper colorMapper;
    private static String FIND_VALUE;
    private static Grid<ColorDto> COLORS_GRID;

    @Autowired
    public ColorsGui(ColorService colorService, ColorMapper colorMapper) {
        this.colorService = colorService;
        this.colorMapper = colorMapper;
        add(getFindTextFieldComponent(), getIconComponent());
        add(getColorsGrid());
    }

    private TextField getFindTextFieldComponent() {
        TextField textField = new TextField();
        textField.setPlaceholder("Search");
        Icon icon = VaadinIcon.SEARCH.create();
        textField.setPrefixComponent(icon);

        textField.addValueChangeListener(it -> {
            FIND_VALUE = it.getValue();
            if (FIND_VALUE.equals("")) {
                FIND_VALUE = null;
            }
            getColorsGrid();
        });
        return textField;
    }

    public Icon getIconComponent() {
        Icon icon = new Icon(VaadinIcon.PLUS_CIRCLE);
        icon.setColor("blue");
        icon.addClickListener(it -> {
            popupCreate();
        });
        return icon;
    }

    private Grid<ColorDto> getColorsGrid() {
        Grid<ColorDto> grid = new Grid<>(ColorDto.class);
        grid.setItems(colorMapper.mapModelListToDtoList(colorService.getColors(null, FIND_VALUE)));

        grid.removeColumnByKey("id");
        grid.setColumns("name");
        grid.addComponentColumn(it -> getRemoveIcon(it.getId()));

        if (COLORS_GRID != null) {
            int oldId = indexOf(COLORS_GRID);
            remove(COLORS_GRID);
            addComponentAtIndex(oldId, grid);
        }
        COLORS_GRID = grid;
        return grid;
    }

    public Icon getRemoveIcon(String id) {
        Icon removeIcon = new Icon(VaadinIcon.MINUS_CIRCLE);
        removeIcon.setColor("red");
        removeIcon.addClickListener(it -> removePopupCreate(id));
        return removeIcon;
    }

    public void removePopupCreate(String id) {
        Dialog dialog = new Dialog();
        dialog.open();
        Text description = new Text("Are you sure you want to delete the color?");
        dialog.add(new Div(description));

        Button confirmButton = new Button("Confirm", event -> {
            colorService.deleteColor(id);
            dialog.close();
            getColorsGrid();
        });

        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);

        Button cancelButton = new Button("Cancel", event -> {
            dialog.close();
        });

        dialog.add(new Div(confirmButton, cancelButton));
    }

    public void popupCreate() {
        Dialog dialog = new Dialog();
        dialog.open();

        Text description = new Text("Enter the name of the color you want to create.");
        dialog.add(new Div(description));

        TextField textField = new TextField();
        textField.setPlaceholder("Name");
        dialog.add(new Div(textField));

        Button confirmButton = new Button("Confirm", event -> {
            if (!textField.getValue().equals("")) {
                colorService.createColor(new Color(textField.getValue()));
                dialog.close();
                getColorsGrid();
            }
        });

        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);

        Button cancelButton = new Button("Cancel", event -> {
            dialog.close();
        });

        dialog.add(new Div(confirmButton, cancelButton));
    }

}
