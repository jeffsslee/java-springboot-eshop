package com.example.eshop.entity;

import com.example.eshop.constant.ItemSellStatus;
import com.example.eshop.dto.ItemFormDto;
import com.example.eshop.exception.OutOfStockException;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item")
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor @Builder
public class Item  extends BaseEntity{

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
//  private LocalDateTime regTime;
//  private LocalDateTime updateTime;

  public void updateItem(ItemFormDto dto){
    itemName = dto.getItemName();
    price = dto.getPrice();
    stockNumber = dto.getStockNumber();
    itemDetail = dto.getItemDetail();
    itemSellStatus = dto.getItemSellStatus();
  }

  public void removeStock(int stockNumber){
    int restStock = this.stockNumber - stockNumber;
    if(restStock < 0){
      throw new OutOfStockException("Item stock is NOT enough! (current stock quantity : " + this.stockNumber +")" );
    }
    this.stockNumber = restStock;
  }

  public void addStock(int stockNumber){
    this.stockNumber += stockNumber;
  }


}
