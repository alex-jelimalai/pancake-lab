package com.pancake.view;

import com.pancake.dto.OrderDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

public class OrderForm extends FormLayout {


    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button cancel = new Button("Cancel");

    Binder<OrderDto> binder = new BeanValidationBinder<>(OrderDto.class);

    public OrderForm() {
        addClassName("order-form");
//        binder.bindInstanceFields(this);//todo
        add(createButtonLayout());
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
