package com.example.eshop.entity;

import com.example.eshop.constant.ItemSellStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
public class Item {

  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "item_id")
  private Long id;
  @Column(nullable = false, length = 50)
  private String itemName;
  // price: BigDecimal
  // price: in Entity, price > BigDecimal or primitive data type, but in DTO use object data type(such as Double or BigDecimal)
  @Column(nullable = false)
  private BigDecimal price;
  @Column(nullable = false)
  private int stockNumber;
  @Lob
  @Column(nullable = false)
  private String itemDetail;
  @Enumerated(EnumType.STRING)
  private ItemSellStatus itemSellStatus;
  private LocalDateTime regTime;
  private LocalDateTime updateTime;
}
