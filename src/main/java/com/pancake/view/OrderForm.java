package com.pancake.view;

import com.pancake.dto.OrderDto;
import com.pancake.dto.OrderItemDto;
import com.pancake.dto.ProductDto;
import com.pancake.model.OrderStatus;
import com.pancake.model.OrderType;
import com.pancake.service.ProductService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

import java.util.Optional;

public class OrderForm extends FormLayout {
    private final ProductService productService;
    private final IntegerField building = new IntegerField("Building");
    private final IntegerField roomNo = new IntegerField("Room");
    private final ComboBox<OrderType> orderType = new ComboBox<>("Order Type");
    private final ComboBox<OrderStatus> status = new ComboBox<>("Order Status");

    private final Button save = new Button("Save");
    private final Button delete = new Button("Delete");
    private final Button cancel = new Button("Cancel");

    private final Grid<OrderItemDto> orderItemsGrid = new Grid<>(OrderItemDto.class);
    private GridListDataView<OrderItemDto> orderItemsDataView;
    private final Button addItemButton = new Button("➕ Add Item");

    private final Binder<OrderDto> binder = new BeanValidationBinder<>(OrderDto.class);
    private OrderDto order;

    public OrderForm(ProductService productService) {
        this.productService = productService;
        addClassName("order-form");

        binder.bindInstanceFields(this);
        configureForm();
        configureOrderItemsGrid();
        add(
                new VerticalLayout(
                        new HorizontalLayout(orderType, status),
                        new HorizontalLayout(building, roomNo),
                        orderItemsGrid,
                        addItemButton,
                        createButtonLayout()
                )
        );
    }

    private void configureForm() {
        orderType.setItems(OrderType.values());
        status.setItems(OrderStatus.values());
        orderType.setRequired(true);
        status.setRequired(true);

        orderType.addValueChangeListener(event -> {
            boolean isDisciple = OrderType.DISCIPLE.equals(event.getValue());
            building.setRequired(!isDisciple);
            roomNo.setRequired(!isDisciple);
        });

        addItemButton.addClickListener(e -> addOrderItem());
    }

    private void configureOrderItemsGrid() {
        orderItemsGrid.setColumns();
        orderItemsGrid.addColumn(item -> Optional.ofNullable(item.getProduct()).map(p -> p.getName()).orElse("")).setHeader("Product");
        orderItemsGrid.addColumn(OrderItemDto::getQuantity).setHeader("Quantity");
        orderItemsGrid.addColumn(OrderItemDto::getPrice).setHeader("Price");

        orderItemsGrid.addComponentColumn(item -> {
            Button removeButton = new Button("X", e -> removeOrderItem(item));
            removeButton.getStyle().set("color", "red");
            return removeButton;
        }).setHeader("Actions");
    }

    private void addOrderItem() {
        OrderItemDto newItem = new OrderItemDto();

        Dialog dialog = new Dialog();
        dialog.setWidth(800, Unit.PIXELS);
        IntegerField quantityField = initQuantityField();
        TextField priceField = initPriceField();

        ComboBox<ProductDto> productComboBox = initProductField(quantityField, priceField);

        TextArea ingridients = initIngridientsField();

        Button saveButton = new Button("Save", e -> {
            if (productComboBox.getValue() != null) {
                newItem.setProduct(productComboBox.getValue());
                newItem.setQuantity(quantityField.getValue());
                newItem.setPrice(Double.parseDouble(priceField.getValue()));

                order.getItems().add(newItem);
                orderItemsDataView.refreshAll();
                dialog.close();
            }
        });


        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
        });

        dialog.add(new VerticalLayout(productComboBox, quantityField, priceField, ingridients, new HorizontalLayout(saveButton, cancelButton)));
        dialog.open();
    }

    private static TextArea initIngridientsField() {
        TextArea ingridients = new TextArea("Ingridients");
        ingridients.setSizeFull();
        return ingridients;
    }

    private ComboBox<ProductDto> initProductField(IntegerField quantityField, TextField priceField) {
        ComboBox<ProductDto> productComboBox = new ComboBox<>("Product");
        productComboBox.setItems(productService.findByTerm(""));
        productComboBox.setItemLabelGenerator(ProductDto::getName);
        productComboBox.setSizeFull();
        productComboBox.addValueChangeListener(event -> {
            ProductDto selectedProduct = event.getValue();
            if (selectedProduct != null) {
                double price = selectedProduct.getPrice() * quantityField.getValue();
                priceField.setValue(String.valueOf(price));
            }
        });
        return productComboBox;
    }

    private TextField initPriceField() {
        TextField priceField = new TextField("Price");
        priceField.setReadOnly(true);
        priceField.setSizeFull();
        return priceField;
    }

    private IntegerField initQuantityField() {
        IntegerField quantityField = new IntegerField("Quantity");
        quantityField.setValue(1);
        quantityField.setMin(1);
        quantityField.setMax(100);
        quantityField.setSizeFull();
        return quantityField;
    }

    private void removeOrderItem(OrderItemDto item) {
        order.getItems().remove(item);
        orderItemsDataView.refreshAll();
    }

    public void setOrder(OrderDto order) {
        this.order = order;
        binder.setBean(order);

        if (order != null) {
            orderItemsDataView = orderItemsGrid.setItems(order.getItems());
        }
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
