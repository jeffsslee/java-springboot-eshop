package com.example.eshop.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
/**
 * OrderItem (with Order and Item) >>>>>>>>>>>>>>>>>>>>>>>>
 *
 * OrderItem(s) may belong to one Order : Many(OrderItem) vs One(Order)
 * OrderItem(s) may belong to one Item : Many(OrderItem) vs One(Item)
 */
@Entity
@Table(name = "order_item")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem  extends BaseEntity{

  @Id @GeneratedValue
  @Column(name = "order_item_id")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;
  private BigDecimal orderPrice;  // this is just a unit price...
  private int count;
//  private LocalDateTime regTime;
//  private LocalDateTime updateTime;

  public static OrderItem createOrderItem(Item item, int count){
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(item);
    orderItem.setCount(count);
    orderItem.setOrderPrice(item.getPrice()); //item.getPrice() is just a unit price
    item.removeStock(count);
    // orderItem.setOrder ?? > when : order.addOrderItem(orderItem)
    return orderItem;
  }

  // Total price for current item (list)
  public BigDecimal getTotalPrice(){
    // BigDecimal.... bigDecimal1.multiply(bigDecimal2)
    return orderPrice.multiply(new BigDecimal(count));
  }

  public void cancel(){
    this.getItem().addStock(count);
  }
}
