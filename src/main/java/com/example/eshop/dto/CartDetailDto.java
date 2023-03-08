package com.example.eshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class CartDetailDto {

  private Long cartItemId;
  private String itemName;
  private BigDecimal price;
  private int count;
  private String imgUrl;

  public CartDetailDto(Long cartItemId, String itemName, BigDecimal price, int count, String imgUrl) {
    this.cartItemId = cartItemId;
    this.itemName = itemName;
    this.price = price;
    this.count = count;
    this.imgUrl = imgUrl;
  }
}
