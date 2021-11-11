package com.customercard.customercard.view;

import com.customercard.customercard.mapper.CustomerGeneralMapper;
import com.customercard.customercard.model.dto.CustomerGeneralDto;
import com.customercard.customercard.service.CustomerService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Customers | Lashes")
public class CustomerView extends VerticalLayout {

    private final CustomerService service;
    private final CustomerGeneralMapper customerGeneralMapper;

    public CustomerView(CustomerService service, CustomerGeneralMapper customerGeneralMapper) {
        this.service = service;
        this.customerGeneralMapper = customerGeneralMapper;
        setCustomerGrid();
    }

    private void setCustomerGrid() {
        Grid<CustomerGeneralDto> grid = new Grid<>(CustomerGeneralDto.class);
        grid.setItems(customerGeneralMapper.mapModelListToDtoList(service.getAllCustomers()));

        grid.removeColumnByKey("id");

        grid.setColumns("name", "surname", "lastDate", "totalWorks");
        add(grid);
    }


}
