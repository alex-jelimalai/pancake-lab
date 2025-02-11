package com.pancake.view;

import com.pancake.dto.OrderDto;
import com.pancake.model.OrderStatus;
import com.pancake.service.OrderService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

import static com.pancake.util.FormatUtil.DOLLAR_FORMATTER;


@Route(value = "chief-orders", layout = MainLayout.class)
@PageTitle("Chief Dashboard")
public class ChiefOrdersView extends VerticalLayout {

    private final Grid<OrderDto> grid;
    private final OrderService orderService;

    public ChiefOrdersView(OrderService orderService) {
        this.orderService = orderService;
        addClassName("list-view");
        setSizeFull();

        this.grid = new Grid<>(OrderDto.class);
        grid.setSizeFull();
        grid.setColumns("orderType", "status", "building", "roomNo");
        grid.addColumn(c -> DOLLAR_FORMATTER.format(c.getTotal$())).setHeader("Total $");

        grid.addComponentColumn(c -> {
            String text;
            if (c.getStatus().equals(OrderStatus.READY_FOR_PROCESSING)) {
                text = "Process";
            } else if (c.getStatus().equals(OrderStatus.PROCESSING)) {
                text = "Complete";
            } else {
                text = "Deliver";
            }
            return new Button(text, (e) -> {
                if (c.getStatus().equals(OrderStatus.READY_FOR_PROCESSING)) {
                    orderService.processOrder(c);
                } else if (c.getStatus().equals(OrderStatus.PROCESSING)) {
                    orderService.completeOrder(c);
                } else {
                    orderService.deliverOrder(c);
                }
                updateList();
            });
        }).setHeader("");

        TextField filterText = new TextField();
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        add(filterText, getContent());
        updateList();
        closeEditor();
    }


    private Div getContent() {
        Div content = new Div(grid);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }


    private void closeEditor() {
        removeClassName("editing");
    }


    private void updateList() {
        grid.setItems(orderService.findBy(List.of(OrderStatus.COMPLETED, OrderStatus.PROCESSING, OrderStatus.READY_FOR_PROCESSING)));
    }


}