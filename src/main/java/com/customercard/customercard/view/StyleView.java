package com.customercard.customercard.view;

import com.customercard.customercard.model.Dictionary;
import com.customercard.customercard.model.Style;
import com.customercard.customercard.service.StyleService;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "/styles", layout = MainLayout.class)
@PageTitle("Styles | Lashes")
public class StyleView extends DictionaryView {

    private final StyleService service;
    private final ModelMapper mapper;

    @Autowired
    public StyleView(StyleService service, ModelMapper mapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public Dictionary getObject(String name) {
        return new Style(name);
    }

}
