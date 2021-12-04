package com.customercard.customercard.view;

import com.customercard.customercard.mapper.CustomerGeneralMapper;
import com.customercard.customercard.model.Contact;
import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.Dictionary;
import com.customercard.customercard.model.Lashes;
import com.customercard.customercard.model.dto.CustomerGeneralDto;
import com.customercard.customercard.model.dto.LashesDto;
import com.customercard.customercard.service.CustomerService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
import java.util.Objects;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Customers | Lashes")
public class CustomerView extends VerticalLayout {

    private final CustomerService service;
    private final CustomerGeneralMapper customerGeneralMapper;
    private final ModelMapper mapper;
    private String findValue;
    private Grid<CustomerGeneralDto> theGrid;

    public CustomerView(CustomerService service, CustomerGeneralMapper customerGeneralMapper, ModelMapper mapper) {
        this.service = service;
        this.customerGeneralMapper = customerGeneralMapper;
        this.mapper = mapper;
        add(getFindTextFieldComponent(), plusIconComponent());
        add(getTheGrid());
    }

    //OK
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
            getTheGrid();
        });

        textField.setValueChangeMode(ValueChangeMode.EAGER);

        return textField;
    }

    public Icon plusIconComponent() {
        Icon icon = new Icon(VaadinIcon.PLUS_CIRCLE);
        String mouseOutColor = "#0006c7";
        icon.setColor(mouseOutColor);

        icon.addClickListener(it -> popupCreate(""));

        icon.getElement().addEventListener("mouseover", it -> icon.setColor("#0007f2"));
        icon.getElement().addEventListener("mouseout", it -> icon.setColor(mouseOutColor));

        return icon;
    }

    public void popupCreate(String id) {

        Dialog dialog = new Dialog();
        dialog.open();

        FormLayout formLayout = new FormLayout();
        TextField firstNameField = new TextField("Name");
        firstNameField.setMinLength(1);

        TextField surnameField = new TextField("Surname");
        surnameField.setMinLength(1);

        TextArea commentField = new TextArea("Comment");
        commentField.getStyle().set("maxHeight", "150px");

        TextField phoneNumberField = new TextField("Phone number");
        EmailField emailField = new EmailField("Email");
        emailField.setErrorMessage("Please enter a valid email address");

        TextField streetField = new TextField("Street");
        TextField officeBoxField = new TextField("Office Box");
        TextField cityField = new TextField("City");

        if (!id.equals("")) {
            Customer theCustomer = service.getAll(id, "").get(0);

            firstNameField.setValue(getValueOrReturnEmpty(theCustomer.getName()));
            surnameField.setValue(getValueOrReturnEmpty(theCustomer.getSurname()));
            commentField.setValue(getValueOrReturnEmpty(theCustomer.getComment()));

            if (theCustomer.getContact() != null) {

                phoneNumberField.setValue(getValueOrReturnEmpty(theCustomer.getContact().getPhoneNumber()));
                emailField.setValue(getValueOrReturnEmpty(theCustomer.getContact().getEmailAddress()));
                streetField.setValue(getValueOrReturnEmpty(theCustomer.getContact().getStreet()));
                officeBoxField.setValue(getValueOrReturnEmpty(theCustomer.getContact().getBoxOffice()));
                cityField.setValue(getValueOrReturnEmpty(theCustomer.getContact().getCity()));

            }
        }

        formLayout.add(
                firstNameField,
                surnameField,
                commentField,
                phoneNumberField,
                emailField,
                streetField,
                officeBoxField,
                cityField
                );

        Text description = new Text("Enter the customer data.");
        dialog.add(new Div(description));

        Notification createdNote = new Notification("Saved", 3000);

        dialog.add(new Div(formLayout));

        Button confirmButton = new Button("Confirm", event -> {

            if (!firstNameField.isEmpty() && !surnameField.isEmpty()) {

                Customer customer = new Customer(firstNameField.getValue(), surnameField.getValue());
                customer.setComment(commentField.getValue());
                customer.setContact(new Contact(
                        phoneNumberField.getValue(),
                        emailField.getValue(),
                        streetField.getValue(),
                        cityField.getValue(),
                        officeBoxField.getValue()
                        ));

                if (id.equals("")) {
                    service.create(customer);
                } else {
                    customer.setId(id);
                    service.update(customer);
                }

                createdNote.open();
                dialog.close();
                getTheGrid();
            }
        });

        Shortcuts.addShortcutListener(dialog, confirmButton::click, Key.ENTER);
        Button cancelButton = new Button("Cancel", event -> dialog.close());
        dialog.add(new Div(confirmButton, cancelButton));
    }

    //OK
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

    public Icon getEditIcon(String id) {
        Icon editIcon = new Icon(VaadinIcon.EDIT);
        String mouseOutColor = "#404040";
        editIcon.setColor(mouseOutColor);
        editIcon.addClickListener(it -> popupCreate(id));

        editIcon.getElement().getStyle().set("vertical-align", "right");
        editIcon.getElement().addEventListener("mouseover", it -> editIcon.setColor("#000000"));
        editIcon.getElement().addEventListener("mouseout", it -> editIcon.setColor(mouseOutColor));

        return editIcon;
    }

    //OK
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

    /*TODO finish getTheGrid function:
        * create popup that show lashes list on row click
     */
    private Grid<CustomerGeneralDto> getTheGrid() {
        Grid<CustomerGeneralDto> grid = new Grid<>(CustomerGeneralDto.class);
        grid.setItems(customerGeneralMapper.mapModelListToDtoList(service.getAll(null, findValue)));
        grid.removeColumnByKey("id");

        grid.addItemClickListener(it ->
            openLashesPopup(it.getItem().getId())
        );

        grid.setColumns("name", "surname", "lastDate", "totalWorks");
        grid.addComponentColumn(it -> getEditIcon(it.getId()));
        grid.addComponentColumn(it -> getRemoveIcon(it.getId()));

        if (theGrid != null) {
            int oldId = indexOf(theGrid);
            remove(theGrid);
            addComponentAtIndex(oldId, grid);
        }

        theGrid = grid;
        return grid;
    }

    //TODO make openLashesPopup function - in the popup i should can see grid of lashes
    private void openLashesPopup(String id) {
        Dialog dialog = new Dialog();
       // dialog.setCloseOnEsc(true);
        dialog.setDraggable(true);
        dialog.setResizable(true);
//        dialog.setHeightFull();
//        dialog.setWidthFull();

        dialog.open();

        Text description = new Text("Lashes creations list for the customer.");
        dialog.add(new Div(description));

        dialog.add(new Div(getTheLashesGrid(id)));

        Button closeButton = new Button("Close", event -> dialog.close());
        dialog.add(new Div(closeButton));

        //List<Lashes> lashesList = service.getById(id).getLashesList();
    }

    private Grid<LashesDto> getTheLashesGrid(String id) {
        Grid<LashesDto> grid = new Grid<>(LashesDto.class);
        List<LashesDto> lashesList = mapper.map(service.getAll(id, "").get(0).getLashesList(), new TypeToken<List<LashesDto>>() {
        }.getType());

        grid.setItems(lashesList);
        grid.removeColumnByKey("id");

        grid.setColumns("style", "method", "color", "comment", "date", "nextDate");

        grid.addComponentColumn(it -> getEditIcon(it.getId()));
        grid.addComponentColumn(it -> getRemoveIcon(it.getId()));

        return grid;
    }

    private String getValueOrReturnEmpty(String value) {
        return Objects.requireNonNullElse(value, "");
    }
}
