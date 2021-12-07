package com.customercard.customercard.view;

import com.customercard.customercard.mapper.CustomerGeneralMapper;
import com.customercard.customercard.model.Contact;
import com.customercard.customercard.model.Customer;
import com.customercard.customercard.model.dto.CustomerGeneralDto;
import com.customercard.customercard.service.CustomerService;
import com.customercard.customercard.service.LashesService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
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
import org.modelmapper.ModelMapper;

import java.util.Objects;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Customers | Lashes")
public class CustomerView extends VerticalLayout {

    private final CustomerService service;
    private final LashesService lashesService;
    private final CustomerGeneralMapper customerGeneralMapper;
    private final ModelMapper mapper;
    private String findValue;
    private Grid<CustomerGeneralDto> theGrid;


    public CustomerView(CustomerService service, CustomerGeneralMapper customerGeneralMapper, ModelMapper mapper, LashesService lashesService) {
        this.service = service;
        this.customerGeneralMapper = customerGeneralMapper;
        this.mapper = mapper;
        this.lashesService = lashesService;
        add(getFindTextFieldComponent(), getCreateButton());
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

    public void popupCreate() {
        popupCreate("");
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

    public Button getCreateButton() {
        Button icon = new Button();
        icon.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        icon.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        icon.addClickListener(it -> popupCreate());

        return icon;
    }


    public Button getRemoveButton(String id) {
        Button removeButton = new Button();
        removeButton.setIcon(VaadinIcon.TRASH.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        removeButton.addClickListener(it -> removePopupCreate(id));
        return removeButton;
    }

    public Button getEditButton(String id) {
        Button editButton = new Button();
        editButton.setIcon(VaadinIcon.EDIT.create());
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);
        editButton.addClickListener(it -> popupCreate(id));
        return editButton;
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


    private Grid<CustomerGeneralDto> getTheGrid() {
        Grid<CustomerGeneralDto> grid = new Grid<>(CustomerGeneralDto.class);
        grid.setItems(customerGeneralMapper.mapModelListToDtoList(service.getAll(null, findValue)));
        grid.removeColumnByKey("id");

        grid.addItemClickListener(it -> createLashesPopup(it.getItem().getId()));

        grid.setColumns("name", "surname", "lastDate", "totalWorks");
        grid.addComponentColumn(it -> getEditButton(it.getId())).setHeader("Edit");
        grid.addComponentColumn(it -> getRemoveButton(it.getId())).setHeader("Remove");

        if (theGrid != null) {
            int oldId = indexOf(theGrid);
            remove(theGrid);
            addComponentAtIndex(oldId, grid);
        }

        theGrid = grid;
        return grid;
    }

    private void createLashesPopup(String id) {

        Dialog lashesDialog = new Dialog();
        lashesDialog.setDraggable(true);
        lashesDialog.setResizable(true);

        lashesDialog.setWidthFull();
        lashesDialog.open();
        LashesView lashesView = new LashesView(lashesService, mapper, service.getById(id));
        lashesDialog.add(lashesView);

        Button closeButton = new Button("Close", event -> lashesDialog.close());
        lashesDialog.add(new Div(closeButton));
    }

    private String getValueOrReturnEmpty(String value) {
        return Objects.requireNonNullElse(value, "");
    }
}
