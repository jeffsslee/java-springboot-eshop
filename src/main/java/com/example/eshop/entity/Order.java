package com.example.eshop.entity;

import com.example.eshop.constant.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Orders vs Member
 * A member may have many orders : One(Member) vs Many(Orders)
 */
@Entity
@Table(name = "orders") // table name : orders >> cause 'order' is a reserved key word in SQL
@Getter @Setter @ToString(exclude = {"member", "orderItems"})
@NoArgsConstructor @AllArgsConstructor @Builder
public class Order  extends BaseEntity{

  @Id @GeneratedValue
  @Column(name = "order_id")
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;
  private LocalDateTime orderDate;
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;
//  private LocalDateTime regTime;
//  private LocalDateTime updateTime;

  // following field is a "bidirectional relationship" with OrderItem
  @OneToMany(
      mappedBy = "order"  // mappedBy = "order" >> 대상 entity "멤버필드명" 그대로....
      , cascade = CascadeType.ALL
      , orphanRemoval = true
      , fetch = FetchType.LAZY
  )
  private List<OrderItem> orderItemList = new ArrayList<>();

  public void addOrderItem(OrderItem orderItem){
    orderItemList.add(orderItem);
    // Order ... OrderItem : Entities with bidirectional relationship..
    // In Order entity: OrderItem is stored in orderItemList
    // In OrderItem entity: Order is stored in Order(order_id)
    orderItem.setOrder(this);
  }

  public static Order createOrder(Member member, List<OrderItem> orderItems){
    Order order = new Order();
    order.setMember(member);
    for (OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }
    order.setOrderStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());
    return order;
  }


  // Total price for the current order : including each item (list)
  public BigDecimal getTotalPrice(){

    BigDecimal totalPrice = new BigDecimal("0.0");

    for (OrderItem orderItem : orderItemList) {
      totalPrice.add(orderItem.getTotalPrice());
    }
    return totalPrice;
  }

  public void cancelOrder(){
    this.orderStatus = OrderStatus.CANCEL;
    for (OrderItem orderItem : orderItemList) {
      orderItem.cancel();
    }

  }


}
