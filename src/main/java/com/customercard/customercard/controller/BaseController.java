package com.customercard.customercard.controller;

import com.customercard.customercard.mapper.CustomerGeneralMapper;
import com.customercard.customercard.model.dto.CustomerGeneralDto;
import com.customercard.customercard.service.CustomerService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class BaseController extends VerticalLayout {

    private final CustomerService service;
    private final CustomerGeneralMapper customerGeneralMapper;

    public BaseController(CustomerService service, CustomerGeneralMapper customerGeneralMapper) {
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
