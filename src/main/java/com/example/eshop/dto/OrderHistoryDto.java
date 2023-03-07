package com.example.eshop.dto;

import com.example.eshop.constant.OrderStatus;
import com.example.eshop.controller.OrderItemDto;
import com.example.eshop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderHistoryDto
 *
 * order_id, item_name, quantity(count), price, total_price, order_date, order_status
 */
@Getter @Setter
public class OrderHistoryDto {

  private Long orderId;
  private String orderDate;
  private OrderStatus orderStatus;
  private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

  public OrderHistoryDto(Order order) {
    this.orderId = order.getId();
    this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy (HH:mm)"));
    this.orderStatus = order.getOrderStatus();
  }

  public void addOrderItemDto(OrderItemDto orderItemDto){
    orderItemDtoList.add(orderItemDto);
  }
}
