package com.pancake.view;

import com.pancake.dto.OrderDto;
import com.pancake.dto.ProductDto;
import com.pancake.model.OrderStatus;
import com.pancake.model.OrderType;
import com.pancake.service.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

public class OrderForm extends FormLayout {
    private final ProductService productService;
    private final IntegerField building = new IntegerField("Building");
    private final IntegerField roomNo = new IntegerField("Room");
    private final ComboBox<OrderType> orderType = new ComboBox<>("Order Type");
    private final ComboBox<OrderStatus> status = new ComboBox<>("Order Status");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");

    private final VerticalLayout items = new VerticalLayout();
    private final Button addItemButton = new Button("Add Item");


    private Binder<OrderDto> binder = new BeanValidationBinder<>(OrderDto.class);

    public OrderForm(ProductService productService) {
        this.productService = productService;
        addClassName("order-form");

        binder.bindInstanceFields(this);
        orderType.setItems(OrderType.values());
        status.setItems(OrderStatus.values());
        orderType.setRequired(true);
        status.setRequired(true);
        orderType.addValueChangeListener(event -> {
            building.setRequired(!OrderType.DISCIPLE.equals(event.getValue()));
            roomNo.setRequired(!OrderType.DISCIPLE.equals(event.getValue()));
        });
        addItemButton.addClickListener(e -> addOrderItemRow());
        items.setSizeFull();
        add(new VerticalLayout(new HorizontalLayout(orderType, status), new HorizontalLayout(building, roomNo), items, addItemButton, createButtonLayout()));
    }

    public void setOrder(OrderDto order) {
        binder.setBean(order);
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(e -> validateAndSave());
        delete.addClickListener(e -> fireEvent(new DeleteOrderEvent(this, binder.getBean())));
        cancel.addClickListener(e -> fireEvent(new CancelOrderEvent(this, binder.getBean())));

        binder.addStatusChangeListener(ev -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, cancel);
    }


    private void addOrderItemRow() {
        HorizontalLayout item = new HorizontalLayout();

        ComboBox<ProductDto> product = new ComboBox<>("Product");
        product.setItems(productService.findByTerm(""));
        product.setItemLabelGenerator(ProductDto::getName);

        IntegerField quantityField = new IntegerField("Quantity");
        quantityField.setMin(1);
        quantityField.setValue(1);

        TextField priceField = new TextField("Price");
        priceField.setReadOnly(true);

        TextArea ingridients = new TextArea("Ingridients");
        ingridients.setSizeFull();
        product.addValueChangeListener(event -> {
            ProductDto selectedProduct = event.getValue();
            if (selectedProduct != null) {
                priceField.setValue(String.valueOf(selectedProduct.getPrice() * quantityField.getValue()));
            }
        });

        Button removeButton = new Button("Remove", e -> items.remove(item));
        item.setVerticalComponentAlignment(FlexComponent.Alignment.END, removeButton);

        item.add(new VerticalLayout(new HorizontalLayout(product, quantityField, priceField, removeButton), ingridients));
        items.add(item);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveOrderEvent(this, binder.getBean()));
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }


    public static abstract class OrderFormEvent extends ComponentEvent<OrderForm> {

        @Getter
        private final OrderDto order;

        public OrderFormEvent(OrderForm source, OrderDto order) {
            super(source, false);
            this.order = order;
        }

    }

    public static class SaveOrderEvent extends OrderFormEvent {
        public SaveOrderEvent(OrderForm source, OrderDto order) {
            super(source, order);
        }
    }

    public static class DeleteOrderEvent extends OrderFormEvent {
        public DeleteOrderEvent(OrderForm source, OrderDto order) {
            super(source, order);
        }
    }

    public static class CancelOrderEvent extends OrderFormEvent {
        public CancelOrderEvent(OrderForm source, OrderDto order) {
            super(source, order);
        }
    }

}
