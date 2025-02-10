package com.pancake.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();

    }

    private void createHeader() {
        H1 logo = new H1("Pancake v1.0");
        logo.addClassName("logo");


        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setWidth("100%");
        header.setClassName("header");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink productsLink = new RouterLink("Products", ListProductsView.class);
        productsLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink ordersLink = new RouterLink("Orders", ListOrdersView.class);
        ordersLink.setHighlightCondition(HighlightConditions.sameLocation());
        addToDrawer(new VerticalLayout(productsLink, ordersLink));
    }

}
