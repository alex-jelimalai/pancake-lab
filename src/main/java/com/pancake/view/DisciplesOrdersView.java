package com.pancake.view;

import com.pancake.dto.OrderDto;
import com.pancake.model.OrderStatus;
import com.pancake.service.OrderService;
import com.pancake.service.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

import static com.pancake.util.FormatUtil.DOLLAR_FORMATTER;


@Route(value = "disciples-orders", layout = MainLayout.class)
@PageTitle("Disciples Dashboard")
public class DisciplesOrdersView extends VerticalLayout {

    private final Grid<OrderDto> grid;
    private final OrderService orderService;
    private final TextField filterText = new TextField();
    private final OrderForm orderForm;

    public DisciplesOrdersView(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        addClassName("list-view");
        setSizeFull();

        this.grid = new Grid<>(OrderDto.class);
        configureGrid();
        HorizontalLayout toolBar = getToolBar();
        orderForm = initOrderForm(orderService, productService);

        add(toolBar, getContent());

        updateList();
        closeEditor();
    }

    private OrderForm initOrderForm(OrderService orderService, ProductService productService) {
        final OrderForm orderForm;
        orderForm = new OrderForm(productService);
        orderForm.addListener(OrderForm.SaveOrderEvent.class, e -> {
            orderService.saveOrder(e.getOrder());
            closeEditor();
            updateList();
        });

        orderForm.addListener(OrderForm.DeleteOrderEvent.class, e -> {
            orderService.deleteOrder(e.getOrder().getId());
            closeEditor();
            updateList();
        });

        orderForm.addListener(OrderForm.CancelOrderEvent.class, e -> closeEditor());
        return orderForm;
    }

    private Div getContent() {
        Div content = new Div(grid, orderForm);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }


    private void closeEditor() {
        orderForm.setOrder(null);
        orderForm.setVisible(false);
        removeClassName("editing");
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addOrderButton = new Button("Add Order");
        addOrderButton.addClickListener(c -> addOrder());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addOrderButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addOrder() {
        grid.asSingleSelect().clear();
        editOrder(new OrderDto());
    }


    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("orderType", "status", "building", "roomNo");
        grid.addColumn(c -> DOLLAR_FORMATTER.format(c.getTotal$())).setHeader("Total $");


        grid.addComponentColumn(c -> {
            Button cancelOrderButton = new Button("Cancel", (e) -> {
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader("Confirm Action");
                dialog.setText("Are you sure you want to cancel the order?");
                dialog.setCancelable(true);
                dialog.setConfirmText("Yes");
                dialog.setCancelText("No");
                dialog.addConfirmListener(de -> {
                    orderService.cancelOrder(c);
                    updateList();
                });
                dialog.open();
            });

            cancelOrderButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            return new HorizontalLayout(
                    new Button("Send to Chief", (e) -> {
                        orderService.markReadyToProcess(c);
                        updateList();
                    }),
                    cancelOrderButton
            );
        }).setHeader("");

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                editOrder(e.getValue());
            } else {
                closeEditor();
            }
        });
    }

    private void editOrder(OrderDto order) {
        orderForm.setOrder(order);
        orderForm.setVisible(true);
        addClassName("editing");
    }

    private void updateList() {
        grid.setItems(orderService.findBy(List.of(OrderStatus.NEW)));
    }


}