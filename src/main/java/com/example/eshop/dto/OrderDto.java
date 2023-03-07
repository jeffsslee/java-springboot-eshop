package com.example.eshop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class OrderDto {

  @NotNull(message = "Item(id) is required!")
  private Long itemId;

  @Min(value = 1, message = "Min order quantity is 1")
  @Max(value = 999, message = "Max order quantity is 999")
  private int count;
}
