package com.customercard.customercard.view;

import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.Dictionary;
import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.model.dto.LashesDto;
import com.customercard.customercard.service.*;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LashesView extends Div {

    private final LashesService service;
    private final ModelMapper mapper;
    private final CustomerService customerService;
    private final Customer customer;
    private final StyleService styleService;
    private final MethodService methodService;
    private final ColorService colorService;

    private Grid<LashesDto> theInnerGrid;
    private String findInnerValue;

    public LashesView(LashesService service,
                      ModelMapper mapper,
                      Customer customer,
                      CustomerService customerService,
                      StyleService styleService,
                      MethodService methodService,
                      ColorService colorService) {
        this.service = service;
        this.mapper = mapper;
        this.customer = customer;
        this.customerService = customerService;
        this.styleService = styleService;
        this.methodService = methodService;
        this.colorService = colorService;
        openLashesPopup();
    }

    private void openLashesPopup() {
        initTheInnerGrid();
        Text description = new Text("Lashes creations list for the " + customer.getName() + ".");
        add(new Div(description));
        add(new Div(getFindTextFieldComponent()));
        add(new Div(getCreateButton()));
        add(new Div(theInnerGrid));

    }

    public void initTheInnerGrid() {
        theInnerGrid = new Grid<>(LashesDto.class);
        setTheGridItems();
        theInnerGrid.removeColumnByKey("id");
        theInnerGrid.removeColumnByKey("date");
        theInnerGrid.removeColumnByKey("nextDate");
        theInnerGrid.setColumns("style", "method", "color", "comment");

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT).withZone(ZoneId.systemDefault());

        theInnerGrid.addColumn(it ->
                        setNextDateColumnValue(formatter, it.getDate())
                )
                .setComparator(LashesDto::getDate)
                .setHeader("Date");

        theInnerGrid.addColumn(it ->
                        setNextDateColumnValue(formatter, it.getNextDate())
                )
                .setComparator(LashesDto::getNextDate)
                .setHeader("Next Date");

        theInnerGrid.addComponentColumn(it -> getEditButton(it.getId())).setHeader("Edit");
        theInnerGrid.addComponentColumn(it -> getRemoveButton(it.getId())).setHeader("Remove");
    }

    private String setNextDateColumnValue(DateTimeFormatter formatter, LocalDateTime date) {
        if (date != null) {
            return formatter.format(date);
        } else {
            return "";
        }
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

    public Button getEditButton(String id) {
        Button editButton = new Button();
        ComponentStyle.setEditButtonStyle(editButton);
        editButton.addClickListener(it -> popupCreate(id));
        return editButton;
    }

    public void removePopupCreate(String id) {
        Dialog dialog = new Dialog();
        dialog.open();
        Text description = new Text("Are you sure you want to delete the lashes creation?");
        dialog.add(new Div(description));

        Notification deletedNote = new Notification("Deleted", 3000);
        Button confirmButton = new Button("Confirm", event -> {
            customer.getLashesList().remove(service.getById(id));
            updateCustomerRepo();
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


    private void setUpComboBox(ComboBox<String> comboBox, List<String> items) {
        comboBox.setAllowCustomValue(true);
        setComboBoxItems(comboBox, items);
    }

    private void setComboBoxItems(ComboBox<String> comboBox, List<String> items) {
        comboBox.addCustomValueSetListener(e -> {
            String customValue = e.getDetail();
            if (items.contains(customValue)) return;
            items.add(customValue);
            comboBox.setItems(items);
            comboBox.setValue(customValue);
        });
        comboBox.setItems(items);
    }

    private List<String> getStylesList() {
        return styleService.getAll().stream()
                .map(Dictionary::getName)
                .collect(Collectors.toList());
    }

    private List<String> getMethodsList() {
        return methodService.getAll().stream()
                .map(Dictionary::getName)
                .collect(Collectors.toList());
    }

    private List<String> getColorsList() {
        return colorService.getAll().stream()
                .map(Dictionary::getName)
                .collect(Collectors.toList());
    }

    public void popupCreate(String id) {

        Dialog dialog = new Dialog();
        dialog.open();

        FormLayout formLayout = new FormLayout();

        List<String> stylesList = getStylesList();
        ComboBox<String> styleComboBox = new ComboBox<>("Style");
        setUpComboBox(styleComboBox, stylesList);

        List<String> methodList = getMethodsList();
        ComboBox<String> methodComboBox = new ComboBox<>("Method");
        setUpComboBox(methodComboBox, methodList);

        List<String> colorList = getColorsList();
        ComboBox<String> colorComboBox = new ComboBox<>("Color");
        setUpComboBox(colorComboBox, colorList);

        TextArea commentField = new TextArea("Comment");
        commentField.getStyle().set("maxHeight", "150px");

        DateTimePicker dateField = new DateTimePicker("Date");
        dateField.setValue(LocalDateTime.now());
        dateField.setDatePlaceholder("Date");
        dateField.setTimePlaceholder("Time");

        Checkbox nextDateCheckbox = new Checkbox();
        nextDateCheckbox.setLabel("Next visit planned?");

        DateTimePicker nextDateField = new DateTimePicker("Next date");
        nextDateField.setDatePlaceholder("Date");
        nextDateField.setTimePlaceholder("Time");
        nextDateField.setReadOnly(true);

        nextDateCheckbox.addValueChangeListener(it -> {
            if (it.getValue()) {
                nextDateField.setReadOnly(false);
                nextDateField.setValue(LocalDateTime.now().plusWeeks(2));
            } else {
                nextDateField.setReadOnly(true);
                nextDateField.setValue(null);
            }
        });

        if (!id.equals("")) {
            Lashes theLashes = service.getAll(id, "").get(0);

            styleComboBox.setValue(CustomerView.getValueOrReturnEmpty(theLashes.getStyle()));
            methodComboBox.setValue(CustomerView.getValueOrReturnEmpty(theLashes.getMethod()));
            colorComboBox.setValue(CustomerView.getValueOrReturnEmpty(theLashes.getColor()));

            commentField.setValue(CustomerView.getValueOrReturnEmpty(theLashes.getComment()));
            dateField.setValue(theLashes.getDate());
            nextDateField.setValue(theLashes.getNextDate().orElse(null));
        }

        Div nextDateDiv = new Div();
        nextDateDiv.add(nextDateCheckbox, nextDateField);

        formLayout.add(
                styleComboBox,
                methodComboBox,
                colorComboBox,
                commentField,
                dateField,
                nextDateDiv
        );

        Text description = new Text("Enter the lashes creation description.");
        dialog.add(new Div(description));

        Notification createdNote = new Notification("Saved", 3000);

        dialog.add(new Div(formLayout));

        Button confirmButton = new Button("Confirm", event -> {

            if (!styleComboBox.isEmpty() || !methodComboBox.isEmpty() || !colorComboBox.isEmpty() || !commentField.isEmpty()) {

                Lashes lashes = new Lashes();
                lashes.setStyle(styleComboBox.getValue());
                lashes.setMethod(methodComboBox.getValue());
                lashes.setColor(colorComboBox.getValue());
                lashes.setComment(commentField.getValue());
                lashes.setDate(dateField.getValue());
                lashes.setNextDate(nextDateField.getValue());

                if (id.equals("")) {
                    service.create(lashes);
                    customer.getLashesList().add(lashes);

                } else {
                    int listId = customer.getLashesList().indexOf(service.getAll(id, "").get(0));
                    lashes.setId(id);
                    service.update(lashes);
                    customer.getLashesList().set(listId, lashes);
                }

                updateCustomerRepo();
                createdNote.open();
                dialog.close();
                setTheGridItems();
            }
        });

        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);
        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(new Div(confirmButton, cancelButton));

    }

    private void updateCustomerRepo() {
        Map<String, Object> partialUpdateMap = new HashMap<>();
        partialUpdateMap.put("lashesList", customer.getLashesList());
        customerService.partialUpdate(customer, partialUpdateMap);
    }
}