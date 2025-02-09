package com.pancake.view;

import com.pancake.model.Product;
import com.pancake.service.ProductService;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;


@Route
public class MainView extends VerticalLayout {

    private final Grid<Product> grid;
    private final ProductService productService;
    private final TextField filterText = new TextField();
    private final ProductForm productForm;

    public MainView(ProductService productService) {
        this.productService = productService;
        addClassName("list-view");
        setSizeFull();

        this.grid = new Grid<>(Product.class);
        configureGrid();
        configureFilter();

        productForm = new ProductForm();
        Div content = new Div(grid, productForm);
        content.addClassName("content");
        content.setSizeFull();

        add(filterText, content);

        updateList();

    }

    private void configureFilter() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }


    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("name", "price", "ingridients", "recipe");
    }

    private void updateList() {
        String filterTerm = filterText.getValue();
        grid.setItems(productService.findByTerm(filterTerm));
    }


}