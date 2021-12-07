package com.customercard.customercard.view;

import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.dto.LashesDto;
import com.customercard.customercard.service.LashesService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;

public class LashesView extends Div {

    private final LashesService service;
    private final ModelMapper mapper;

    private Customer customer;
    private Grid<LashesDto> theInnerGrid;
    private String findInnerValue;

    public LashesView(LashesService service, ModelMapper mapper, Customer customer) {
        this.service = service;
        this.mapper = mapper;
        this.customer = customer;
        openLashesPopup();
    }


    //TODO make openLashesPopup function - in the popup i should can see grid of lashes
    private void openLashesPopup() {

        Text description = new Text("Lashes creations list for the " + customer.getName() + ".");
        add(new Div(description));
        add(new Div(getFindTextFieldComponent()));
        add(new Div(getCreateButton()));
        add(new Div(getTheGrid()));

    }

    private Grid<LashesDto> getTheGrid() {
        Grid<LashesDto> grid = new Grid<>(LashesDto.class);
        List<LashesDto> lashesList = mapper.map(service.getAll(customer, findInnerValue), new TypeToken<List<LashesDto>>() {
        }.getType());

        grid.setItems(lashesList);
        grid.removeColumnByKey("id");

        grid.setColumns("style", "method", "color", "comment", "date", "nextDate");

        grid.addComponentColumn(it -> getEditButton(it.getId()));
        grid.addComponentColumn(it -> getRemoveButton(it.getId()));


        //TODO it does not work like it should to. first grid must to be deleted.
        if (theInnerGrid != null) {
//            int oldId = indexOf(theInnerGrid);
//            remove(theInnerGrid);
//            addComponentAtIndex(oldId, grid);
            replace(theInnerGrid, grid);
        }

        theInnerGrid = grid;
        return grid;
    }

    private TextField getFindTextFieldComponent() {
        TextField textField = new TextField();
        textField.setPlaceholder("Search");
        Icon icon = VaadinIcon.SEARCH.create();
        textField.setPrefixComponent(icon);

        textField.addValueChangeListener(it -> {
            findInnerValue = it.getValue();
            if (findInnerValue.equals("")) {
                findInnerValue = null;
            }
            getTheGrid();
        });

        textField.setValueChangeMode(ValueChangeMode.EAGER);

        return textField;
    }

    public Button getCreateButton() {
        Button icon = new Button();
        icon.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        icon.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        icon.addClickListener(it -> popupCreate());

        return icon;
    }

    //TODO
    public Button getRemoveButton(String id) {
        Button removeButton = new Button();
        removeButton.setIcon(VaadinIcon.TRASH.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        removeButton.addClickListener(it -> removePopupCreate(id));
        return removeButton;
    }

    //TODO
    public Button getEditButton(String id) {
        Button editButton = new Button();
        editButton.setIcon(VaadinIcon.EDIT.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        editButton.addClickListener(it -> popupCreate(id));
        return editButton;
    }

    //TODO
    public void removePopupCreate(String id) {
        Dialog dialog = new Dialog();
        dialog.open();
        Text description = new Text("Are you sure you want to delete the Customer?");
        dialog.add(new Div(description));

        Notification deletedNote = new Notification("Deleted", 3000);

        Button confirmButton = new Button("Confirm", event -> {
            service.delete(id);
            deletedNote.open();
            dialog.close();
            getTheGrid();
        });
        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(new Div(confirmButton, cancelButton));
    }

    //@TODO
    public void popupCreate() {
        popupCreate("");
    }

    //@TODO
    public void popupCreate(String id) {

        Dialog dialog = new Dialog();
        dialog.open();


    }
}
