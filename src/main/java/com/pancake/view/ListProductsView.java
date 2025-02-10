package com.pancake.view;

import com.pancake.model.Product;
import com.pancake.service.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@Route(value = "", layout = MainLayout.class)
@PageTitle("Products")
public class ListProductsView extends VerticalLayout {

    private final Grid<Product> grid;
    private final ProductService productService;
    private final TextField filterText = new TextField();
    private final ProductForm productForm;

    public ListProductsView(ProductService productService) {
        this.productService = productService;
        addClassName("list-view");
        setSizeFull();

        this.grid = new Grid<>(Product.class);
        configureGrid();
        HorizontalLayout toolBar = getToolBar();
        productForm = new ProductForm();
        productForm.addListener(ProductForm.SaveProductEvent.class, e -> {
            productService.saveProduct(e.getProduct());
            closeEditor();
            updateList();
        });

        productForm.addListener(ProductForm.DeleteProductEvent.class, e -> {
            productService.deleteProduct(e.getProduct());
            closeEditor();
            updateList();
        });

        productForm.addListener(ProductForm.CancelProductEvent.class, e -> {
            closeEditor();
        });

        Div content = new Div(grid, productForm);
        content.addClassName("content");
        content.setSizeFull();

        add(toolBar, content);

        updateList();
        closeEditor();
    }


    private void closeEditor() {
        productForm.setProduct(null);
        productForm.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addProductButton = new Button("Add Product");
        addProductButton.addClickListener(c -> addProduct());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addProductButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addProduct() {
        grid.asSingleSelect().clear();
        editProduct(new Product());
    }


    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("name", "price", "ingridients", "details");

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                editProduct(e.getValue());
            } else {
                closeEditor();
            }
        });
    }

    private void editProduct(Product product) {
        productForm.setProduct(product);
        productForm.setVisible(true);
        addClassName("editing");
    }

    private void updateList() {
        String filterTerm = filterText.getValue();
        grid.setItems(productService.findByTerm(filterTerm));
    }


}