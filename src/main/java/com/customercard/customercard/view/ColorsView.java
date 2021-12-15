package com.customercard.customercard.view;

import com.customercard.customercard.model.Color;
import com.customercard.customercard.model.Dictionary;
import com.customercard.customercard.service.ColorService;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "/colors", layout = MainLayout.class)
@PageTitle("Colors | Lashes")
public class ColorsView extends DictionaryView {

    @Autowired
    public ColorsView(ColorService service, ModelMapper mapper) {
        super(service, mapper);
    }

    @Override
    public Dictionary getObject(String name) {
        return new Color(name);
    }
}