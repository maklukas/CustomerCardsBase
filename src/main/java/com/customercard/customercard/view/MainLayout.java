package com.customercard.customercard.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(value = Lumo.class)
public class MainLayout extends AppLayout {

    public MainLayout() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Customer Cards");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tabs tabs = getTabs();
        addToDrawer(tabs);

        Button toggleButton = toggleButton();

        addToNavbar(toggle, title);

        Div b = new Div();
        b.add(toggleButton);
        ComponentStyle.setDivAlignToRightStyle(b);
        addToNavbar(b);

    }



    public Button toggleButton() {
        Button toggle = new Button();
        toggle.setIcon(VaadinIcon.ADJUST.create());
        toggle.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_CONTRAST);

        toggle.addClickListener(it -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
            } else {
                themeList.add(Lumo.DARK);
            }
        });

        return toggle;
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.USER_HEART, "Customers", CustomerView.class),
                createTab(VaadinIcon.TASKS, "Plan", PlanView.class),
                createTab(VaadinIcon.FLASH, "Colors", ColorsView.class),
                createTab(VaadinIcon.DIAMOND, "Styles", StyleView.class),
                createTab(VaadinIcon.COG, "Methods", MethodView.class)
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private <T extends Component> Tab createTab(VaadinIcon viewIcon, String viewName, Class<T> route) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(route);
        link.setTabIndex(-1);

        return new Tab(link);
    }
}
