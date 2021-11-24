package com.customercard.customercard.view;

import com.customercard.customercard.model.Dictionary;
import com.customercard.customercard.model.Method;
import com.customercard.customercard.service.MethodService;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "/methods", layout = MainLayout.class)
@PageTitle("Methods | Lashes")
public class MethodView extends DictionaryView {

    @Autowired
    public MethodView(MethodService service, ModelMapper mapper) {
        super(service, mapper);
    }

    @Override
    public Dictionary getObject(String name) {
        return new Method(name);
    }
}
