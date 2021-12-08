package com.customercard.customercard.view;

import com.customercard.customercard.model.Contact;
import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.model.dto.LashesDto;
import com.customercard.customercard.service.LashesService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
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

    private void openLashesPopup() {
        initTheInnerGrid();
        Text description = new Text("Lashes creations list for the " + customer.getName() + ".");
        add(new Div(description));
        add(new Div(getFindTextFieldComponent()));
        add(new Div(getCreateButton()));;
        add(new Div(theInnerGrid));

    }

    public void initTheInnerGrid() {
        theInnerGrid = new Grid<>(LashesDto.class);
        setTheGridItems();
        theInnerGrid.removeColumnByKey("id");

        theInnerGrid.setColumns("style", "method", "color", "comment", "date", "nextDate");

        theInnerGrid.addComponentColumn(it -> getEditButton(it.getId()));
        theInnerGrid.addComponentColumn(it -> getRemoveButton(it.getId()));
    }

    private void setTheGridItems() {
        List<LashesDto> lashesList = mapper.map(service.getAll(customer, findInnerValue), new TypeToken<List<LashesDto>>() {
        }.getType());
        theInnerGrid.setItems(lashesList);
    }

    private TextField getFindTextFieldComponent() {
        TextField findTextField = new TextField();
        findTextField.setPlaceholder("Search");
        findTextField.setClearButtonVisible(true);
        Icon icon = VaadinIcon.SEARCH.create();
        findTextField.setPrefixComponent(icon);

        findTextField.addValueChangeListener(it -> {
            findInnerValue = it.getValue();
            if (findInnerValue.equals("")) {
                findInnerValue = null;
            }
            setTheGridItems();
        });

        findTextField.setValueChangeMode(ValueChangeMode.EAGER);

        return findTextField;
    }

    public Button getCreateButton() {
        Button icon = new Button();
        icon.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        icon.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        icon.addClickListener(it -> popupCreate());

        return icon;
    }

    //TODO get buttons styles into separate class
    public Button getRemoveButton(String id) {
        Button removeButton = new Button();
        removeButton.setIcon(VaadinIcon.TRASH.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        removeButton.addClickListener(it -> removePopupCreate(id));
        return removeButton;
    }

    //TODO like above
    public Button getEditButton(String id) {
        Button editButton = new Button();
        editButton.setIcon(VaadinIcon.EDIT.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        editButton.addClickListener(it -> popupCreate(id));
        return editButton;
    }

    //TODO check if ok
    public void removePopupCreate(String id) {
        Dialog dialog = new Dialog();
        dialog.open();
        Text description = new Text("Are you sure you want to delete the lashes creation?");
        dialog.add(new Div(description));

        Notification deletedNote = new Notification("Deleted", 3000);
        Button confirmButton = new Button("Confirm", event -> {
            customer.getLashesList().remove(service.getById(id));
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
        popupCreate("");
    }

    //@TODO fix saving things into repository
    public void popupCreate(String id) {

        Dialog dialog = new Dialog();
        dialog.open();

        FormLayout formLayout = new FormLayout();
        TextField styleField = new TextField("Style");
        styleField.setMinLength(1);

        TextField methodField = new TextField("Method");
        methodField.setMinLength(1);

        TextField colorField = new TextField("Color");
        colorField.setMinLength(1);

        TextArea commentField = new TextArea("Comment");
        commentField.getStyle().set("maxHeight", "150px");

        DatePicker dateField = new DatePicker("Date");
        dateField.setValue(LocalDate.now());

        DatePicker nextDateField = new DatePicker("Next date");
        nextDateField.setValue(LocalDate.now().plusWeeks(2));

        if (!id.equals("")) {
            Lashes theLashes = service.getAll(id, "").get(0);

            styleField.setValue(CustomerView.getValueOrReturnEmpty(theLashes.getStyle()));
            methodField.setValue(CustomerView.getValueOrReturnEmpty(theLashes.getMethod()));
            colorField.setValue(CustomerView.getValueOrReturnEmpty(theLashes.getColor()));
            commentField.setValue(CustomerView.getValueOrReturnEmpty(theLashes.getComment()));
            dateField.setValue(theLashes.getDate());
            nextDateField.setValue(theLashes.getNextDate());

        }

        formLayout.add(
                styleField,
                methodField,
                colorField,
                commentField,
                dateField,
                nextDateField
        );


        Text description = new Text("Enter the lashes creation description.");
        dialog.add(new Div(description));

        Notification createdNote = new Notification("Saved", 3000);

        dialog.add(new Div(formLayout));

        Button confirmButton = new Button("Confirm", event -> {

            if (!styleField.isEmpty() || !methodField.isEmpty() || !colorField.isEmpty() || !commentField.isEmpty()) {

                Lashes lashes = new Lashes();
                lashes.setComment(commentField.getValue());

                if (id.equals("")) {
                    service.create(lashes);
                    customer.getLashesList().add(lashes);
                } else {
                    customer.setId(id);
                    service.update(lashes);
                    int listId = customer.getLashesList().indexOf(service.getAll(id, "").get(0));
                    customer.getLashesList().set(listId, lashes);
                }

                createdNote.open();
                dialog.close();
                setTheGridItems();
            }
        });

        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);
        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(new Div(confirmButton, cancelButton));

    }
}
