package com.pancake.view;

import com.pancake.dto.ProductDto;
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

    private final TextField name = new TextField("ProductDto Name");
    private final NumberField price = new NumberField("Price");
    private final TextField ingridients = new TextField("Ingridients");
    private final TextArea details = new TextArea("Details");
    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");
    private final Binder<ProductDto> binder = new BeanValidationBinder<>(ProductDto.class);

    public ProductForm() {
        addClassName("product-form");
        price.setMin(0.10);
        price.setMax(100.00);
        name.setRequiredIndicatorVisible(true);
        ingridients.setRequiredIndicatorVisible(true);
        binder.bindInstanceFields(this);
        add(name, price, ingridients, details, createButtonLayout());
    }

    public void setProduct(ProductDto product) {
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
        private final ProductDto product;

        public ProductFormEvent(ProductForm source, ProductDto product) {
            super(source, false);
            this.product = product;
        }

    }

    public static class SaveProductEvent extends ProductFormEvent {
        public SaveProductEvent(ProductForm source, ProductDto product) {
            super(source, product);
        }
    }

    public static class DeleteProductEvent extends ProductFormEvent {
        public DeleteProductEvent(ProductForm source, ProductDto product) {
            super(source, product);
        }
    }

    public static class CancelProductEvent extends ProductFormEvent {
        public CancelProductEvent(ProductForm source, ProductDto product) {
            super(source, product);
        }
    }

}
