package com.example.eshop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class MainItemDto {

  private Long id;
  private String itemName;
  private String itemDetail;
  private String imgUrl;
  private BigDecimal price;

  @QueryProjection
  public MainItemDto(Long id, String itemName, String itemDetail, String imgUrl, BigDecimal price) {
    this.id = id;
    this.itemName = itemName;
    this.itemDetail = itemDetail;
    this.imgUrl = imgUrl;
    this.price = price;
  }
}
