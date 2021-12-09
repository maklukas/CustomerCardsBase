package com.customercard.customercard.view;

import com.customercard.customercard.model.Dictionary;
import com.customercard.customercard.service.DictionaryService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;

public abstract class DictionaryView extends VerticalLayout {
    private final DictionaryService service;
    private final ModelMapper mapper;
    private String findValue;
    private Grid<Dictionary> theGrid;

    public DictionaryView(DictionaryService service, ModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
        initTheGrid();
        add(getFindTextFieldComponent(), getCreateButton());
        add(theGrid);
    }

    private TextField getFindTextFieldComponent() {
        TextField findTextField = new TextField();
        findTextField.setPlaceholder("Search");
        findTextField.setClearButtonVisible(true);
        Icon icon = VaadinIcon.SEARCH.create();
        findTextField.setPrefixComponent(icon);

        findTextField.addValueChangeListener(it -> {
            findValue = it.getValue();
            if (findValue.equals("")) {
                findValue = null;
            }
            setTheGridItems();
        });

        findTextField.setValueChangeMode(ValueChangeMode.EAGER);

        return findTextField;
    }

    public void initTheGrid() {
        theGrid = new Grid<>(Dictionary.class);
        setTheGridItems();
        theGrid.removeColumnByKey("id");
        theGrid.setColumns("name");
        Grid.Column<Dictionary> removeCol = theGrid.addComponentColumn(it -> getRemoveButton(it.getId())).setHeader("Remove");

        removeCol.setTextAlign(ColumnTextAlign.CENTER);

    }

    private void setTheGridItems() {
        List<Dictionary> dictionaries = mapper.map(service.getAll(null, findValue), new TypeToken<List<Dictionary>>() {
            }.getType());
        theGrid.setItems(dictionaries);
    }

    public void removePopupCreate(String id) {
        Dialog dialog = new Dialog();
        dialog.open();
        Text description = new Text("Are you sure you want to delete the Element?");
        dialog.add(new Div(description));

        Notification deletedNote = new Notification("Deleted", 3000);

        Button confirmButton = new Button("Confirm", event -> {
            service.delete(id);
            deletedNote.open();
            dialog.close();
            setTheGridItems();
        });

        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);
        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(new Div(confirmButton, cancelButton));
    }

    public void popupCreate() {
        Dialog dialog = new Dialog();
        dialog.open();

        Text description = new Text("Enter the name of the element you want to create.");
        dialog.add(new Div(description));

        Notification createdNote = new Notification("Created", 3000);

        TextField textField = new TextField();
        textField.setPlaceholder("Name");
        dialog.add(new Div(textField));

        Button confirmButton = new Button("Confirm", event -> {
            if (!textField.getValue().equals("")) {
                service.create(getObject(textField.getValue()));
                createdNote.open();
                dialog.close();
                setTheGridItems();
            }
        });

        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);
        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(new Div(confirmButton, cancelButton));
    }

    public Button getCreateButton() {
        Button icon = new Button();
        ComponentStyle.setCreateButtonStyle(icon);
        icon.addClickListener(it -> popupCreate());
        return icon;
    }

    public Button getRemoveButton(String id) {
        Button removeButton = new Button();
        ComponentStyle.setRemoveButtonStyle(removeButton);
        removeButton.addClickListener(it -> removePopupCreate(id));
        return removeButton;
    }

        public abstract Dictionary getObject(String name);
}

