package com.example.eshop.dto;

import com.example.eshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * OrderItemDto
 *
 * To show order item for shopping history
 *
 * item_name, price, quantity, img_url
 *
 */
@Getter @Setter
public class OrderItemDto {

  private String itemName;
  private int count;
  private BigDecimal orderPrice; //order total price from orderItem.getTotalPrice()
  private String imgUrl;

  public OrderItemDto(OrderItem orderItem, String imgUrl) {
    this.itemName = orderItem.getItem().getItemName();
    this.count = orderItem.getCount();
    this.orderPrice = orderItem.getTotalPrice();
    this.imgUrl = imgUrl;
  }
}
