package com.pancake.view;

import com.pancake.model.Product;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

public class ProductForm extends FormLayout {

    TextField name = new TextField("Product Name");
    NumberField price = new NumberField("Price");
    TextField ingridients = new TextField("Ingridients");
    TextArea details = new TextArea("Details");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    Binder<Product> binder = new BeanValidationBinder<>(Product.class);

    public ProductForm() {
        addClassName("product-form");
        price.setMin(0.10);
        price.setMax(100.00);
        name.setRequiredIndicatorVisible(true);
        ingridients.setRequiredIndicatorVisible(true);
        binder.bindInstanceFields(this);
        add(name, price, ingridients, details, createButtonLayout());
    }

    public void setProduct(Product product) {
        binder.setBean(product);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(e -> validateAndSave());
        delete.addClickListener(e -> fireEvent(new DeleteProductEvent(this, binder.getBean())));
        cancel.addClickListener(e -> fireEvent(new CancelProductEvent(this, binder.getBean())));

        binder.addStatusChangeListener(ev -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, cancel);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveProductEvent(this, binder.getBean()));
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


    public static abstract class ProductFormEvent extends ComponentEvent<ProductForm> {

        @Getter
        private final Product product;

        public ProductFormEvent(ProductForm source, Product product) {
            super(source, false);
            this.product = product;
        }

    }

    public static class SaveProductEvent extends ProductFormEvent {
        public SaveProductEvent(ProductForm source, Product product) {
            super(source, product);
        }
    }

    public static class DeleteProductEvent extends ProductFormEvent {
        public DeleteProductEvent(ProductForm source, Product product) {
            super(source, product);
        }
    }

    public static class CancelProductEvent extends ProductFormEvent {
        public CancelProductEvent(ProductForm source, Product product) {
            super(source, product);
        }
    }

}
