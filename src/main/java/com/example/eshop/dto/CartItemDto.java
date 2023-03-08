package com.example.eshop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * CartItemDto
 * item_id, quantity(count)
 */

@Getter @Setter
public class CartItemDto {

  @NotNull(message = "Item (id) is required!")
  private Long itemId;
  @Min(value = 1, message = "Min order quantity is at least 1")
  private int count;
}
